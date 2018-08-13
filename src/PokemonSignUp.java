import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PokemonSignUp extends JFrame{
	private String baseDir = "C:\\work\\Project_pokemon\\singup\\";
	private JFrame frame;
	private JRadioButton blue, gold, gray, green, orange, pink, rainbow, red;
	private JButton btnphotoup, btnsignup;
	private ImageIcon title, name, id, password, address, telephone, badges, trainer;
	private JLabel header, jname, jid, jpassword, jaddress, jtelephone, jbadges, trainerPic;
	private JTextField txtName, txtId, txtAddress, txtTelephone;
	private JPasswordField txtPassword;
	private String imgName, valuePassword;
	private String valueBlue, valueGold, valueGray, valueGreen, valueOrange, valuePink, valueRainbow, valueRed;
	private int last = 0; 

	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs; 

	public PokemonSignUp() {
		design();
		accDb(); 
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int re = JOptionPane.showConfirmDialog(PokemonSignUp.this, "종료하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
				if (re == JOptionPane.YES_OPTION) {
					dispose();					
				}else {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});
		 
	}
	
	public void accDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			
			
		} catch (Exception e) {
			System.out.println("accDb err : " + e.getMessage());
		}
	}
	
	
	public void design() {
		frame = new JFrame();
		frame.setBounds(650,300,550, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Pokemon Center");
		String titlebar = baseDir+"ball.png";
		Image img = Toolkit.getDefaultToolkit().getImage(titlebar);
		frame.setIconImage(img);
		
		String hd = baseDir+"header.PNG";
		ImageIcon title = new ImageIcon(hd);
		JLabel header = new JLabel();
		header.setIcon(title);
		header.setBounds(0, 0, 600, 80);
		frame.getContentPane().add(header); 
		
		String nm = baseDir+"name.PNG";
		ImageIcon name = new ImageIcon(nm);
		JLabel jname = new JLabel();
		jname.setIcon(name);
		jname.setBounds(14, 70, 100, 80);
		frame.getContentPane().add(jname);
		txtName = new JTextField();
		txtName.setFont(new Font("Arial", Font.PLAIN, 14));
		txtName.setBounds(90, 100, 230, 25);
		txtName.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtName);
		txtName.setColumns(10);
		
		
		String i = baseDir+"id.PNG";
		ImageIcon id = new ImageIcon(i);
		JLabel jid = new JLabel();
		jid.setIcon(id);
		jid.setBounds(14, 100, 100, 80);
		frame.getContentPane().add(jid);
		txtId = new JTextField();
		txtId.setFont(new Font("Arial", Font.PLAIN, 14));
		txtId.setBounds(55, 130, 265, 25);
		txtId.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtId);
		txtId.setColumns(10);
		
		String p = baseDir+"password.PNG";
		ImageIcon password = new ImageIcon(p);
		JLabel jpassword = new JLabel();
		jpassword.setIcon(password);
		jpassword.setBounds(14, 130, 120, 80);
		frame.getContentPane().add(jpassword);
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
		txtPassword.setBounds(120, 160, 200, 25);
		txtPassword.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);
		
		
		String add = baseDir + "address.PNG";
		ImageIcon address = new ImageIcon(add);
		JLabel jaddress = new JLabel();
		jaddress.setIcon(address);
		jaddress.setBounds(18, 160, 120, 80);
		frame.getContentPane().add(jaddress);
		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Arial", Font.PLAIN, 14));
		txtAddress.setBounds(105, 190, 215, 25);
		txtAddress.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtAddress);
		txtAddress.setColumns(10);
		
		String tel = baseDir+"tel.PNG";
		ImageIcon telephone = new ImageIcon(tel);
		JLabel jtelephone = new JLabel();
		jtelephone.setIcon(telephone);
		jtelephone.setBounds(14, 190, 120, 80);
		frame.getContentPane().add(jtelephone);
		txtTelephone = new JTextField();
		txtTelephone.setFont(new Font("Arial", Font.PLAIN, 14));
		txtTelephone.setBounds(60, 220, 260, 25);
		txtTelephone.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtTelephone);
		txtTelephone.setColumns(10);
		
		String bad = baseDir+"badges.PNG";
		String iblue = baseDir+"blue.png";
		ImageIcon badges = new ImageIcon(bad);
		JLabel jbadges = new JLabel();
		jbadges.setIcon(badges);
		jbadges.setBounds(14, 220, 120, 80);
		frame.getContentPane().add(jbadges);
		JCheckBox blue = new JCheckBox("Blue");
		blue.setBounds(22, 280, 50, 30);
		frame.getContentPane().add(blue);
		JCheckBox gold = new JCheckBox("Gold");
		gold.setBounds(82, 280, 50, 30);
		frame.getContentPane().add(gold);
		JCheckBox gray = new JCheckBox("Gray");
		gray.setBounds(142, 280, 53, 30);
		frame.getContentPane().add(gray);
		JCheckBox green = new JCheckBox("Green");
		green.setBounds(202, 280, 60, 30);
		frame.getContentPane().add(green);
		JCheckBox orange = new JCheckBox("Orange");
		orange.setBounds(22, 320, 70, 30);
		frame.getContentPane().add(orange);
		JCheckBox pink = new JCheckBox("Pink");
		pink.setBounds(100, 320, 56, 30);
		frame.getContentPane().add(pink);
		JCheckBox rainbow = new JCheckBox("Rainbow");
		rainbow.setBounds(164, 320, 78, 30);
		frame.getContentPane().add(rainbow);
		JCheckBox red = new JCheckBox("Red");
		red.setBounds(250, 320, 56, 30);
		frame.getContentPane().add(red);
		
		String tp = baseDir+"trainer.png";
		ImageIcon trainer = new ImageIcon(tp);
		JLabel jtrainer = new JLabel();
		jtrainer.setIcon(trainer);
		jtrainer.setBounds(385, 110, 400, 90);
		frame.getContentPane().add(jtrainer); 

		String photo = baseDir+"button.PNG";
		ImageIcon up = new ImageIcon(photo);
		JButton btnphotoup = new JButton();
		btnphotoup.setIcon(up);
		btnphotoup.setBounds(370, 204, 120, 40);
		frame.getContentPane().add(btnphotoup);
		
		//사진 업로드 버튼 눌렀을 때 
		btnphotoup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				File file;
				String imgPath;
				JFileChooser chooser = new JFileChooser("C:\\work\\jsou\\");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = chooser.showOpenDialog(null);
				if(result == JFileChooser.CANCEL_OPTION) {
					file = null;
					imgPath = "";
					imgName = imgPath;
				} else {
					file = chooser.getSelectedFile();
					imgPath = file.getName();
					String dir = "C:\\work\\jsou\\Pokemon\\src\\pimage\\";  //******주소 변경 필요한지 확인 
					ImageIcon icon = new ImageIcon(dir + imgPath);
					jtrainer.setIcon(icon);
					imgName = imgPath;
					
				}
			}
		});
	
		String s = baseDir + "btnsignup.PNG";
		ImageIcon sup = new ImageIcon(s);
		JButton btnsignup = new JButton();
		btnsignup.setIcon(sup);
		btnsignup.setBounds(360, 280, 140, 50);
		frame.getContentPane().add(btnsignup);
		
		//회원가입 확인 버튼 누른후 SQL로 데이터 보내는 작업 
		btnsignup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				init();
				
				try {
					if(txtId.getText().equals("") || txtPassword.getText().equals("") || txtName.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "필수사항을 입력하지 않았습니다", "회원가입 에러", JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						System.out.println(last);
						String sql = "insert into trainer values(?,?,?,?,?,?,?,null)";	
						pstmt = conn.prepareStatement(sql);
						
						pstmt.setString(1, (last+1)+ "");

						pstmt.setString(2, txtId.getText());
						pstmt.setString(3, txtPassword.getText());
						pstmt.setString(4, txtName.getText());
		
						pstmt.setString(5, txtAddress.getText());
						pstmt.setString(6, imgName);
						pstmt.setString(7, txtTelephone.getText());

					//체크박스 눌렀을때 정보를 SQL로 보내기						
						pstmt.executeUpdate();

						System.out.println("test");		
						conn.close();
						pstmt.close();
					
						//회원가입 후 로그인 화면으로 넘어가는 화면 창 보이기
						//insert the class name of the main page (XXXXX) here 
						PokemonLogin info = new PokemonLogin(); 
						PokemonLogin.main(null);
						frame.dispose();
					}
				} catch (Exception e2) {
					System.out.println("actionPerformed err2: " + e2);
				} finally {
					
					
				}
				
				
				
				
			}
		});
		
		
		
		
	}
	
	public void init() {
		try {
			String sql  = "select nvl(max(t_no),0) t_no from trainer";
			 // 마지막 번호 가져오기
			pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				rs.last();
				last = Integer.parseInt(rs.getString("t_no")) ;
				System.out.println(last);
			}else {
				
				last = 0;
			}			
		} catch (Exception e) {
			System.out.println("signup init err : " + e.getMessage());
		} finally {
			
		}
	}
	
	public static void main(String[] args) {
		PokemonSignUp su = new PokemonSignUp();
		su.frame.setVisible(true);

	}

}
