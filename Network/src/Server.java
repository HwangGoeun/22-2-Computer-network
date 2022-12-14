import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.sql.*;

public class Server {
	
	private static class Capitalizer implements Runnable{
		private Socket socket;
		
		Capitalizer(Socket socket){
			this.socket = socket;
		}
		
		@Override
		public void run() {
			ObjectOutputStream outToClient = null;
			ObjectInputStream inFromClient = null;		
			User client = null;
			System.out.println("User connect IP: " + socket.getInetAddress() + " Port: " + socket.getPort());
			try {
				outToClient = new ObjectOutputStream(socket.getOutputStream());
				inFromClient = new ObjectInputStream(socket.getInputStream());
				
				while(true) {
					try {
						Protocol requestProtocol = (Protocol) inFromClient.readObject();
						if(requestProtocol.getHeader().get("request").equalsIgnoreCase("login")) {
							ServerLogin.ServerLogin(outToClient, inFromClient, requestProtocol);
						}
						else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("register")) {
							ServerSignup.signup(outToClient, requestProtocol);
						}
						else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("search")) {
							ServerSearch.search(outToClient, requestProtocol);
						} // end of search
						else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("addfriend")) {
							ServerAddFriend.addFriend(outToClient, requestProtocol);
						}
						else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("adddetail")) {
							ServerAdddetail.addDetail(outToClient, requestProtocol);
						}
						else if(requestProtocol.getHeader().get("request").equalsIgnoreCase("changemsg")) {
							ServerChangeMsg.changemsg(outToClient, requestProtocol);
						}
					}
					catch(ClassNotFoundException e) {
						e.printStackTrace();
					}
					catch(Exception e) {

					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(socket != null) socket.close();
					
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		int nPort = 7777;
	
		ServerSocket listener = new ServerSocket(nPort);
		System.out.println("Start Server & Waiting Connect (Port# = " + nPort + ")");
		System.out.println("<------------------------------------------------------------>");
		System.out.println();
		
		ExecutorService pool = Executors.newFixedThreadPool(20);
		while(true) {
			Socket socket = listener.accept();
			pool.execute(new Capitalizer(socket));
		}
	}
}