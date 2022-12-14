import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ServerChangeMsg {
	public static void changemsg(ObjectOutputStream outToClient, Protocol requestProtocol) throws Exception{
		Connection con = null;
		Statement stmt = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?useUnicode=true&serverTimezone=UTC";
			String user = "root";
			String passwd = "0000";
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println(con);
			try {
				stmt = con.createStatement();
				String update = "update user set message = \"" + requestProtocol.getMyinfo().getPw() + "\" where id = \"" + requestProtocol.getMyinfo().getId() + "\"";
				int count = stmt.executeUpdate(update);
				System.out.println(requestProtocol.getMyinfo().getPw());
				System.out.println("Update user state message");
				
				HashMap<String, String> header = new HashMap<String, String>();
				header.put("response", "changemsg");
				header.put("responseMsg", "s");
				
				Protocol responseProtocol = new Protocol(header);
				outToClient.writeObject(responseProtocol);
				System.out.println("Send responseProtocol to client");
			}
			catch(SQLException e){
				e.printStackTrace();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		con.close();
	}
}