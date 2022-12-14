import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class ServerSignup {
	public static void signup(ObjectOutputStream outToClient, Protocol requestProtocol) throws Exception{
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
				String insert = "insert into user(`id`,`pw`,`name`,`nickname`,`birth`,`email`,`state`,`lastaccess`) values(?, ?, ?, ?, ?, ?, 0, default)";
				pstmt = con.prepareStatement(insert);
				
				String _id = requestProtocol.getMyinfo().getId();
				String _pw = requestProtocol.getMyinfo().getPw();
				String _name = requestProtocol.getMyinfo().getName();
				String _nickname = requestProtocol.getMyinfo().getNickname();
				String _birth = requestProtocol.getMyinfo().getBirth();
				String _email = requestProtocol.getMyinfo().getEmail();
				
				pstmt.setString(1, _id);
				pstmt.setString(2, _pw);
				pstmt.setString(3, _name);
				pstmt.setString(4, _nickname);
				pstmt.setString(5, _birth);
				pstmt.setString(6, _email);
				
				int count = pstmt.executeUpdate();
				System.out.println("INSERT ROW : " + count);
				
				HashMap<String, String> header = new HashMap<>();
				header.put("response", "register");
				Protocol p = new Protocol(header);
				outToClient.writeObject(p);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		con.close();	
	}
}