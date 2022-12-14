import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class ServerAdddetail {
	public static void addDetail(ObjectOutputStream outToClient, Protocol requestProtocol) throws Exception {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost/network?useUnicode=true&serverTimezone=UTC";
			String user = "root";
			String passwd = "0000";
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println(con);
			try {
				stmt = con.createStatement();
				String sql = "select id, name, nickname, birth, email, message, state, lastaccess from user where id = \""+ requestProtocol.getUserinfo().getId() +"\"";
				rs = stmt.executeQuery(sql);
				
				User a = null;
				
				while(rs.next()) {
					String _id = rs.getString(1);
					String _name = rs.getString(2);
					String _nickname = rs.getString(3);
					String _birth = rs.getString(4);
					String _email = rs.getString(5);
					String _message = rs.getString(6);
					int _state = rs.getInt(7);
					String _lastaccess = rs.getString(8);
					
					a = new User(_id, _name, _nickname, _birth, _email, _message, _state, _lastaccess);
				}
				
				HashMap<String, String> header = new HashMap<String, String>();
				header.put("response", "adddetail");
				header.put("responseMsg", "s");
				
				Protocol responseProtocol = new Protocol(header, a);
				
				outToClient.writeObject(responseProtocol);
				System.out.println("Send responseProtocol to client");
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}