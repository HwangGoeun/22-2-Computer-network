import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.management.modelmbean.XMLParseException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainGUI extends JFrame {
    
	Protocol myProtocol;
	
    /* ------------------------------------------------- */
	// GUI
    
    JLabel nickname = new JLabel("nickname");
	JLabel statemsg = new JLabel("statemsg");
	JButton searchFriends = new JButton("search friends");
	JButton editStatemsg = new JButton("Edit stateMessage");
	JLabel onlineL = new JLabel("online");
	JLabel offlineL = new JLabel("offline");
	JLabel api = new JLabel();
	JLabel label_apiIcon = new JLabel();
	JLabel label_apiComment = new JLabel();

	JList onlinelist = new JList();
	JList offlinelist = new JList();
	JMenuItem i1;
	JMenuItem i2;
	
	int onIndex;
	
	Font font = new Font("맑은 고딕",Font.PLAIN,30);
	Font font2 = new Font("맑은 고딕",Font.PLAIN,15);
	Font font3 = new Font("맑은 고딕",Font.PLAIN,10);
	
	JPanel userPanel = new JPanel();
	JPanel searchPanel = new JPanel();
	JPanel onlinePanel = new JPanel();
	JPanel offlinePanel = new JPanel();
	JPanel friendPanel = new JPanel();
	JPanel apiPanel = new JPanel();
	JPanel online = new JPanel();
	JPanel offline = new JPanel();
	
	MainGUI(Protocol protocol) {
	
		myProtocol = protocol;
		setBoard();
		
		nickname.setText(myProtocol.getMyinfo().getNickname());
		statemsg.setText(myProtocol.getMyinfo().getMessage());
		userPanel.add(nickname);
		userPanel.add(statemsg);
		userPanel.setLayout(new GridLayout(2,0));
		userPanel.setBorder(new LineBorder(Color.black));
		userPanel.setBackground(Color.white);
		
		searchPanel.add(searchFriends);
		searchPanel.add(editStatemsg);
		searchPanel.setBorder(new LineBorder(Color.black));
		searchPanel.setBackground(Color.white);
		friendPanel.setBackground(Color.white);
		friendPanel.add(userPanel);
		
		
		online.add(onlinelist);
		online.setBackground(Color.PINK);
		onlinePanel.setLayout(new BorderLayout());
		onlinePanel.add(onlineL,BorderLayout.NORTH);
		onlinePanel.add(online,BorderLayout.CENTER);
	
		onlinePanel.setBackground(Color.white);
		friendPanel.add(onlinePanel);
		
		offline.add(offlinelist);
		offline.setBackground(Color.PINK);
		offlinePanel.setLayout(new BorderLayout());
		offlinePanel.add(offlineL,BorderLayout.NORTH);
		offlinePanel.add(offline,BorderLayout.CENTER);
		offlinePanel.setBackground(Color.white);
		friendPanel.add(offlinePanel);
		
		i1 = new JMenuItem("상세보기");
		i2 = new JMenuItem("채팅하기");
		
		i1.addActionListener(new MenuActionListener());
		i2.addActionListener(new MenuActionListener());
		
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(i1);
		popupMenu.add(i2);
		
		friendPanel.setLayout(new GridLayout(3,0));
		friendPanel.setBorder(new LineBorder(Color.black));
		
		api.setText("현재 기온 : " + string_temp);
		label_apiIcon.setText(weather_icon);
		label_apiComment.setText(weather_comment);
		label_apiComment.setFont(font3);
		apiPanel.add(api);
		apiPanel.add(label_apiIcon);
		apiPanel.add(label_apiComment);
		apiPanel.setBorder(new LineBorder(Color.black));
		apiPanel.setBackground(Color.white);
		
		ButtonListener search = new ButtonListener();
		
		searchFriends.addActionListener(search);
		editStatemsg.addActionListener(search);
		setTitle("main");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		
		contentPane.setLayout(new BorderLayout(10,20));
		contentPane.setBackground(Color.white);
		contentPane.add(searchPanel,BorderLayout.SOUTH);
		contentPane.add(friendPanel,BorderLayout.CENTER);
		contentPane.add(apiPanel,BorderLayout.NORTH);
		
		setSize(450,650);
		setVisible(true);
		
		addWindowListener(new WindowListener() {
		      
	        public void windowClosing(WindowEvent e) {
	        	try {
	        		ClientUpdateLasttime.updateLstime(myProtocol);
	        	}
	        	catch(Exception ee) {
	        		ee.printStackTrace();
	        	}
	        }
	    
	        public void windowOpened(WindowEvent e) { }
	        public void windowClosed(WindowEvent e) { }    
	        public void windowIconified(WindowEvent e) { }
	        public void windowDeiconified(WindowEvent e) { }
	        public void windowActivated(WindowEvent e) { }
	        public void windowDeactivated(WindowEvent e) { }
	    
	     }
	   );
		
		
		onlinelist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON3) {
					JList c = (JList) e.getComponent();
					int x = e.getX();
					int y = e.getY();
					if(!onlinelist.isSelectionEmpty()&&onlinelist.locationToIndex(e.getPoint()) == onlinelist.getSelectedIndex()) {
						int count = c.getModel().getSize();
						int cal = count * 18;
						if(y<=cal) {
							popupMenu.show(onlinelist, x, y);
						}
						
						onIndex = onlinelist.getSelectedIndex();
						
					}
				}
			}
		});
		

		
		offlinelist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == e.BUTTON3) {
					JList c = (JList) e.getComponent();
					int x = e.getX();
					int y = e.getY();
					if(!offlinelist.isSelectionEmpty()&&offlinelist.locationToIndex(e.getPoint()) == offlinelist.getSelectedIndex()) {
						int count = c.getModel().getSize();
						int cal = count * 18;
						if(y<=cal) {
							popupMenu.show(offlinelist, x, y);
						}
					}
				}
			}
		});
	}
	
	class ButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			JButton b = (JButton)e.getSource();
			//친구찾기
			if(b.getText().equals("search friends")) {
				SearchGUI gotoSearch = new SearchGUI();
			}else if(b.getText().equals("Edit stateMessage")) {
				EditStatemsgGUI gotoEdit = new EditStatemsgGUI(myProtocol);
			}
		}
	}
	
	//online & offline
	//ArrayList<User> friendList = new ArrayList<>();
	//arraylist 크기만큼 for문 돌려서 getState 0,1로 분류	
	public void setBoard() {

		nickname.setText(myProtocol.getMyinfo().getName());
		nickname.setFont(font);
		nickname.setHorizontalAlignment(nickname.CENTER);
		
		statemsg.setFont(font2);
		statemsg.setHorizontalAlignment(nickname.CENTER);
		
		ArrayList<User> friendList = new ArrayList<>();
		friendList = myProtocol.getFriend();
		
		ArrayList<User> onlineList = new ArrayList<>();
		
		ArrayList<User> offlineList = new ArrayList<>();
		
		
		for(int i = 0;i<friendList.size();i++) {
			if(friendList.get(i).getState() == 0) {
				offlineList.add(friendList.get(i));
			}else if(friendList.get(i).getState() == 1){
				onlineList.add(friendList.get(i));
			}
		}

		String[] name = new String[5];
		
		for(int i=0;i<onlineList.size();i++) {
			name[i] = onlineList.get(i).getNickname()+"  " + onlineList.get(i).getMessage()+"  "+onlineList.get(i).getLastAccess();
		}
		onlinelist.setListData(name);
		
		
		
		String[] name2 = new String[5];
		
		for(int i=0;i<offlineList.size();i++) {
			name2[i] = offlineList.get(i).getNickname()+"  "+offlineList.get(i).getMessage()+"  "+offlineList.get(i).getLastAccess();
			
		}
		offlinelist.setListData(name2);
	}
	
	// end of GUI
	/* ------------------------------------------------- */
	
	class MenuActionListener implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			int index = e.getID();
			String cmd = e.getActionCommand(); 
			switch(cmd) { // 메뉴 아이템의 종류 구분
				case "상세보기" :
					
					break;
				case "채팅하기" :
					
					break;
			}
		}
	}
	
	// openAPI
	static String string_temp = "0";
	static String weather_icon = "";
	static String weather_comment = "";
	
	// 열거형으로 정의 후 사용
    enum WeatherValue {
        PTY, REH, RN1, T1H, UUU, VEC, VVV, WSD
    }
    
    static {
    	try {
    		// 입력받을 weather 객체
            Weather weather = new Weather();

            // 변수 설정
            String apiURL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
            String authKey = "3LgvkCr64Q%2Fm%2FgORnKAtjlfOL%2BqDYB2q4j2RrJVQqX0PWBPJyteTSjFEJzX1rvGFo2XbODNAxs5hwkMcyNdZYA%3D%3D";
            
            // 구하고자 하는 시간과 좌표 대입
     		String nx = "62";
     		String ny = "124";
//     		String baseDateTime = timeCalc(date, time, idx);
     		String baseDate = "20221214";
     		String baseTime = "0900";
     		
     		StringBuilder urlBuilder = new StringBuilder(apiURL);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + authKey);
            urlBuilder.append("&" + URLEncoder.encode("numOfRows=10", "UTF-8"));    // 숫자 표
            urlBuilder.append("&" + URLEncoder.encode("pageNo=1", "UTF-8"));    // 페이지 수
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /* 조회하고싶은 날짜*/
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); //경도
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); //위도

            URL url = new URL(urlBuilder.toString());
//            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
//            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            String result = sb.toString();
            
            // 문자열 Document 로 변경해서 List 형태로 가져와서 객체에 파싱함.
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(result));
            Document document = db.parse(is);

                document.getDocumentElement().normalize();
                NodeList nList = document.getElementsByTagName("item");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        String category = eElement.getElementsByTagName("category").item(0).getTextContent();
                        double value = Double.parseDouble(eElement.getElementsByTagName("obsrValue").item(0).getTextContent());

                        WeatherValue weatherValue = WeatherValue.valueOf(category);
                        switch (weatherValue) {
    	                    case PTY:
    	                        weather.setPTY(value);
    	                        break;
    	                    case REH:
    	                        weather.setREH(value);
    	                        break;
    	                    case RN1:
    	                        weather.setRN1(value);
    	                        break;
    	                    case T1H:
    	                        weather.setT1H(value);
    	                        break;
    	                    case UUU:
    	                        weather.setUUU(value);
    	                        break;
    	                    case VEC:
    	                        weather.setVEC(value);
    	                        break;
    	                    case VVV:
    	                        weather.setVVV(value);
    	                        break;
    	                    case WSD:
    	                        weather.setWSD(value);
    	                        break;
    	                    default:
    	                        throw new XMLParseException();
    	                }
    	            }
    	        }
                
            // icon
            double double_temp = weather.getT1H();
            if (double_temp < 5) {
            	weather_icon = "🌀";
            	weather_comment = " 바깥이 추우니 따뜻한 옷 챙겨입어요.";
            }
            else if (double_temp < 15) {
            	weather_icon = "☁";
            	weather_comment = " 선선한 가을날씨에요.";
            }
            else if (double_temp < 25) {
            	weather_icon = "🌤";
            	weather_comment = " 조금 더울 수 잇어요.";
            }
            else if (double_temp >= 25) {
            	weather_icon = "☀";
            	weather_comment = " 바깥 날씨가 더우니 건강 조심하세요.";
            }
            if (weather.getPTY() == 1 || weather.getPTY() == 2) {
            	weather_icon = "☔";
            	weather_comment = " 비가 오니 우산을 챙기세요.";
            }
            if (weather.getPTY() == 3) {
            	weather_icon = "⛄";
            	weather_comment = " 눈이 내리니 미끄러지지 않게 조심해요.";
            }
                
            string_temp = Double.toString(weather.getT1H());
            System.out.println("temp : " + string_temp);
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
	public static void main(String[] args) {

	}
	
	/* ------------------------------------------------- */
}