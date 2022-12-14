import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerSearch {
	public static void search(ObjectOutputStream outToClient, Protocol requestProtocol) throws Exception {
		ArrayList<User> searchUser = new ArrayList<>();
		
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
				String sql = "select u2.id, u2.name, u2.nickname, u2.birth, u2.email from user as u1 inner join friends as f on u1.id = f.f1 inner join user as u2 on f.f2 = u2.id where u1.id = \"" + requestProtocol.getMyinfo().getId() + "\" AND u2.id LIKE \"%" + requestProtocol.getUserinfo().getId() + "%\"";
				rs = stmt.executeQuery(sql);
				
				while(rs.next()) {
					String _id = rs.getString(1);
					String _name = rs.getString(2);
					String _nickname = rs.getString(3);
					String _birth = rs.getString(4);
					String _email = rs.getString(5);
					
					User a = new User(_id, _name, _nickname, _birth, _email);
					searchUser.add(a);
				}
				
				HashMap<String, String> header = new HashMap<String, String>();
				header.put("response", "search");
				header.put("responseMsg", "s");
				
				Protocol responseProtocol = new Protocol(header, searchUser);
				
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

		con.close();
	}
}