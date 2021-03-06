package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

import app.Mediator;
import app.model.User;
import app.states.RequestTypes;

public class NetworkMediatorImpl implements NetworkMediator {
	private Logger logger;
	public static final int LOGIN_PENDING = 0;
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILED = 2;
	
	public static boolean running = true;
	private static int loginStatus = LOGIN_PENDING;

	private final int MIN_FILE_SIZE = 256;
	private final int MAX_FILE_SIZE = 2048;
	private final int CHUNK_SIZE = 32;
	private final long NETWORK_DELAY = 175;

	private Mediator med;

	public NetworkMediatorImpl(Mediator med) {
		this.med = med;
	}

	public void initLogger() {
		logger = Logger.getLogger(NetworkMediatorImpl.class.getName());
	}

	/**
	 * Initiates the connection to the remote socket
	 */
	private void connect(SelectionKey key) throws IOException {

		SocketChannel socketChannel = (SocketChannel) key.channel();
		try {
			socketChannel.finishConnect();
		} catch (IOException e) {
			logger.fatal("[NETWORK]: Failed to connect to destination!");
			running = false;
			
			return;
		}

		key.interestOps(SelectionKey.OP_WRITE);
	}

	private void write(SelectionKey key) throws IOException {

		ByteBuffer buf = (ByteBuffer) key.attachment();
		SocketChannel socketChannel = (SocketChannel) key.channel();

		while (socketChannel.write(buf) > 0)
			;

		if (!buf.hasRemaining()) {
			socketChannel.close();
			running = false;
		}
	}

	/**
	 * Serializes a generic network status update class
	 * 
	 * @param nn
	 *            Network status update class
	 * @return serialized form of the class
	 */
	private byte[] marshal(NetworkNotification nn) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(nn);
			oos.flush();
			oos.close();
			return baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		}

		throw new RuntimeException(
				"[NETWORK]: Unable to convert event into raw byte data: " + nn);
	}

	public byte[] makeChunk() {
		byte[] chunk = new byte[CHUNK_SIZE];
		new Random().nextBytes(chunk);
		
		return chunk;
	}

	/**
	 * Generates a randomly sized ByteBuffer and sends it
	 * to the buyer, CHUNK_SIZE at a time
	 */
	public boolean doTransferProduct(String product, User user) {
		
		NetworkNotification nn;
		Random rnd = new Random();
		String sender = med.getUserName();

		int noOfChunks = rnd.nextInt((MAX_FILE_SIZE - MIN_FILE_SIZE) / CHUNK_SIZE) +
										MIN_FILE_SIZE / CHUNK_SIZE;

		med.updateGui(RequestTypes.REQUEST_INITIAL_TRANSFER, user.getName(),
				product, noOfChunks);
		
		/* first send the initial request, with total file size */
		nn = new NetworkNotification(RequestTypes.REQUEST_INITIAL_TRANSFER,
						sender, product, noOfChunks);
		
		if (!doNetworkSend(user.getIp(), user.getPort(), nn)) {
			logger.error("[NETWORK]: Failed to send chunk to " + user);
			return false;
		}

		try {
			Thread.sleep(NETWORK_DELAY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 1; i <= noOfChunks; i++) {
			if (med.getRelevantUsers().get(user.getName()) == null) {
				med.updateGui(RequestTypes.REQUEST_LOGOUT, user.getName(),
						product, -1);
				break;
			}
			med.updateGui(RequestTypes.REQUEST_TRANSFER, user.getName(),
					product, i);
			
			nn = new NetworkNotification(RequestTypes.REQUEST_TRANSFER,
						sender, product, i, makeChunk());

			if (!doNetworkSend(user.getIp(), user.getPort(), nn)) {
				logger.error("[NETWORK]: Failed to send chunk to " + user);
				return false;
			}

			try {
				Thread.sleep(NETWORK_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public void setLoginFailed() {
		loginStatus = LOGIN_FAILED;
	}
	
	public void setLoginSuccess() {
		loginStatus = LOGIN_SUCCESS;
	}

//	public boolean fetchRelevantBuyers(User user) {
// 		
// 		NetworkNotification nn = new NetworkNotification(
// 				RequestTypes.REQUEST_RELEVANT_BUYERS, user);
// 		
// 		logger.debug("[NETWORK]: Fetching users...");
//		
//		if (!doNetworkSend(LOGIN_SERVER_IP, LOGIN_SERVER_PORT, nn)) {
//			logger.error("[NETWORK]: Failed to send relevant buyers request");
//			return false;
//		}
//
//		return true;
//	}
	
//	public boolean fetchRelevantSellers(User user) {
//		
//		NetworkNotification nn = new NetworkNotification(
//				RequestTypes.REQUEST_RELEVANT_SELLERS, user);
//		
//		nn.setProduct(user.getProducts().get(0));
//		
//		logger.debug("[NETWORK]: Fetching users...");
//		
//		if (!doNetworkSend(LOGIN_SERVER_IP, LOGIN_SERVER_PORT, nn)) {
//			logger.warn("[NETWORK]: Failed to send relevant seller request");
//			return false;
//		}
//		
//		return true;
//	}
	
	/**
	 * Handles a generic PopUp menu request
	 * 
	 * Sends a notification over the network to each of the given destinations
	 */
	public boolean sendNotifications(int action, String userName, String product,
			int price, Vector<User> destinations) {
		
		for (User user : destinations) {
			
			if (action == RequestTypes.REQUEST_INITIAL_TRANSFER) {
				return doTransferProduct(product, user);
			}
			logger.debug("[NETWORK]: Sending to dest: " + user);
			
			NetworkNotification nn = new NetworkNotification(action, userName,
					product, price);
			
			if (!doNetworkSend(user.getIp(), user.getPort(), nn)) {
				logger.warn("[NETWORK]: Network send failed!");
				return false;
			}
		}
	
		return true;
	}
	
	public boolean sendNotifications(int action, String userName, String ip, int port,
							String product, Vector<User> destinations) {
		
		for (User user : destinations) {
			
			logger.debug("[NETWORK]: Sending to dest: " + user);
			
			NetworkNotification nn = new NetworkNotification(action, userName,
					product, -1);

			nn.setIp(ip);
			nn.setPort(port);
			nn.setProduct(product);

			if (!doNetworkSend(user.getIp(), user.getPort(), nn)) {
				logger.warn("[NETWORK]: Network send failed!");
				return false;
			}
		}
	
		return true;	
	}

	/**
	 * Submits a login notification (only called in Login Server mode)
	 */
	public boolean sendLoginNotification(int action, String ip, int port, User user) {
		
		if (action == RequestTypes.REQUEST_LOGOUT)
			logger.debug("User " + user.getName() + " just logged out!");
		
		NetworkNotification nn = new NetworkNotification(action, user);
		
		if (!doNetworkSend(ip, port, nn)) {
			logger.warn("[Login Server]: Send Login ACK failed!");
			return false;
		}

		return true;
	}
	
	/**
	 * Sends a serialized NetworkNotification class to the specified destination
	 * 
	 * @param ip destination IP
	 * @param port destination port
	 * @param nn the notification to be serialized and sent
	 * 
	 * @return true on success, false on failure
	 */
	private boolean doNetworkSend(String ip, int port, NetworkNotification nn) {
		
		Selector selector = null;
		SocketChannel socketChannel = null;

		try {
			selector = Selector.open();

			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(ip, port));

			byte[] classBytes;

			try {
				classBytes = marshal(nn);

			} catch (RuntimeException e) {
				e.printStackTrace();
				return false;
			}

			ByteBuffer buf = ByteBuffer.allocate(classBytes.length);

			buf.clear();
			buf.put(classBytes);
			buf.flip();

			socketChannel.register(selector, SelectionKey.OP_CONNECT, buf);

			logger.debug("[NETWORK]: Sending object");

			while (running) {
				selector.select();

				for (Iterator<SelectionKey> it = selector.selectedKeys()
						.iterator(); it.hasNext();) {
					SelectionKey key = it.next();
					it.remove();

					if (key.isConnectable())
						connect(key);
					else if (key.isWritable())
						write(key);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			
			running = true;
			return false;

		} finally {
			if (selector != null)
				try {
					selector.close();
				} catch (IOException e) {
				}

			if (socketChannel != null)
				try {
					socketChannel.close();
				} catch (IOException e) {
				}
		}

		running = true;
		return true;
	}
	
	public void startServer(String ip, int port) {
		new NetworkServer(ip, port, med).start();
	}
}
