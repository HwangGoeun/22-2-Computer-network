import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class ServerAddFriend {
	public static void addFriend(ObjectOutputStream outToClient, Protocol requestProtocol) throws Exception{
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?useUnicode=true&serverTimezone=UTC";
			String user = "root";
			String passwd = "0000";
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println(con);
			
			try {
				String psql = "insert into friends values (?, ?)";
				pstmt = con.prepareStatement(psql);
				
				String f1 = requestProtocol.getMyinfo().getId();
				String f2 = requestProtocol.getUserinfo().getId();
				
				pstmt.setString(1, f1);
				pstmt.setString(2, f2);
				
				int count = pstmt.executeUpdate();
				System.out.println("Complete add friend");
				
				HashMap<String, String> header = new HashMap<String, String>();
				header.put("response", "addfriend");
				header.put("responseMsg", "s");
				
				Protocol responseProtocol = new Protocol(header);
				outToClient.writeObject(responseProtocol);
				System.out.println("Send responseProtocol to client");
			}
			catch(Exception e) {
				e.printStackTrace();
				HashMap<String, String> header = new HashMap<String, String>();
				header.put("response", "addfriend");
				header.put("responseMsg", "s");
				
				Protocol responseProtocol = new Protocol(header);
				outToClient.writeObject(responseProtocol);
				System.out.println("Send fail responseProtocol to client");
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