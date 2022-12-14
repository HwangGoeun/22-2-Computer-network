import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;

public class Client{
	public static void main(String[] args) {
		Socket socket = null;
		ObjectOutputStream outToServer = null;
		ObjectInputStream inFromServer = null;
		
		try {
			socket = ConnectApp.connectApp();
			outToServer = new ObjectOutputStream(socket.getOutputStream());
			inFromServer = new ObjectInputStream(socket.getInputStream());
			new GUILogin(inFromServer, outToServer);
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}