import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable{
	private String id;
	private String pw;
	private String name;
	private String nickname;
	private String birth;
	private String email;
	private String message;
	private int state;
	private String lastAccess;
	
	private int type;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	// detail info
	User(String id, String name, String nickname, String birth, String email, String message, int state, String lastaccess){
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.birth = birth;
		this.email = email;
		this.message = message;
		this.state = state;
		this.lastAccess = lastaccess;
	}
	
	
	// search user
	User(String id){
		this.id = id;
	}
	
	User(String id, String name, String nickname, String birth, String email){
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.birth = birth;
		this.email = email;
	}
	
	
	
	// login
	User(String id, String pw) throws Exception{
		this.id = id;
		this.pw = pw;
	}
	
	User(String nickname, String message, int state){
		this.nickname = nickname;
		this.message = message;
		this.state = state;
	}
	
	User(String nickname, String message, int state, String lastAccess){
		this.nickname = nickname;
		this.message = message;
		this.state = state;
		this.lastAccess = lastAccess;
	}
	
	// sign up
	User(String id, String pw, String name, String nickname, String birth, String email) throws Exception{
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.nickname = nickname;
		this.birth = birth;
		this.email = email;
		this.message = null;
		this.state = 0;
		this.lastAccess = "";
	}
	
	
	public String getId() {
		return this.id;
	}
	
	public String getPw() {
		return this.pw;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	
	public String getBirth() {
		return this.birth;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getState() {
		return this.state;
	}
	
	public String getLastAccess() {
		return this.lastAccess;
	}
	
	///
	public void setId(String id) {
		this.id = id;
	}
	///
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public ObjectInputStream getIn() {
		return in;
	}

	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
}