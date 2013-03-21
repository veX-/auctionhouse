package gui;

public interface GUIMediator {
	public boolean register(String username);
	public boolean logIn(String username, String password);
	public boolean logOut();
	
	
}
