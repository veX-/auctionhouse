package app;

import gui.GUIMediator;
import wsc.WSClientMediator;
import network.NetworkMediator;

public class Mediator implements GUIMediator, NetworkMediator, WSClientMediator {
	
	/* GUI related methods */
	
	public boolean register(String username) {
		
		return true;
	}
	
	public boolean logIn(String username, String password) {
		
		return true;
	}
	
	public boolean logOut() {
		
		return true;
	}
	
	/* Network layer methods */
	
	public boolean sendItem(String name, int value) {
		
		return true;
	}
	
	/* Web Service Client related methods */
}
