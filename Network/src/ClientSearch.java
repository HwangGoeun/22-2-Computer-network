import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientSearch {
	public static Protocol search(String myid, String userid) throws Exception {
		ObjectOutputStream outToServer = null;
		ObjectInputStream inFromServer = null;
		Socket socket = null;
		Scanner sc = new Scanner(System.in);
		Protocol responseProtocol = null;
		
		HashMap<String, String> header = new HashMap<>();
		header.put("request", "search");
		
		try {
			socket = new Socket("localhost", 7777);
			outToServer = new ObjectOutputStream(socket.getOutputStream());
			inFromServer = new ObjectInputStream(socket.getInputStream());
			User my = new User(myid);
			User user = new User(userid);
			Protocol requestProtocol = new Protocol(header, my, user);
			outToServer.writeObject(requestProtocol);
			
			responseProtocol = (Protocol) inFromServer.readObject();
			System.out.println(responseProtocol);
			for(int i = 0; i < responseProtocol.getSearch().size(); i++) {
				System.out.println(responseProtocol.getSearch().get(i).getId() + "[" + 
						responseProtocol.getSearch().get(i).getNickname() + "]");
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		return responseProtocol;
	}
}