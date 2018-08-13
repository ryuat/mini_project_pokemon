import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.omg.PortableServer.ServantRetentionPolicyValue;

public class PokemonLogin {
	private JFrame frame;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JFrame frmLoginSystem;
	private ImageIcon heading, icon1, icon2;
	String password, username;
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	// Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					PokemonLogin window = new PokemonLogin();
					window.frame.setVisible(true);	
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}
	
	// Create the application
	
	public PokemonLogin() {
		initialize();
		accDb();
		
		
	} 
	
	// Initialize the application
	private void accDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			
			
		} catch (Exception e) {
			System.out.println("accDb err : " + e.getMessage());
		}
	}
	
	private void init() {
		try {
			String sql = "select * from trainer where t_id = ? order by t_no asc";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  txtUsername.getText());
			rs = pstmt.executeQuery();
		} catch (Exception e) {
			System.out.println("login init err : " + e.getMessage());
		} finally {
			
		}
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(650, 300, 550, 350);
//		frame.setBounds(650,300,550, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Pokemon Center Login");
		String titlebar = "C:\\work\\Project_pokemon\\login_img\\ball.png\\";
		Image img = Toolkit.getDefaultToolkit().getImage(titlebar);
		frame.setIconImage(img);
		
		
		String headings = "C:\\work\\Project_pokemon\\login_img\\ICON.PNG\\";
		ImageIcon heading = new ImageIcon(headings); 
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(heading);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 30));
		lblNewLabel.setBounds(150, 10, 200, 100);
		frame.getContentPane().add(lblNewLabel);
		
		
		String icon1dir = "C:\\work\\Project_pokemon\\login_img\\signin.PNG\\";
		ImageIcon icon1 = new ImageIcon(icon1dir); 
		JLabel lblUsername = new JLabel();
		lblUsername.setIcon(icon1);
		lblUsername.setBounds(32, 154, 172, 37);
		frame.getContentPane().add(lblUsername);
		
		String icon2dir = "C:\\work\\Project_pokemon\\login_img\\signin2.PNG\\";
		ImageIcon icon2 = new ImageIcon(icon2dir);
		JLabel lblPassword = new JLabel();
		lblPassword.setIcon(icon2);
		lblPassword.setFont(new Font("굴림", Font.BOLD, 14));
		lblPassword.setBounds(32, 230, 172, 43);
		frame.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
		txtUsername.setBounds(170, 160, 210, 30);
		txtUsername.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
		txtPassword.setBounds(170, 240, 210, 30);
		txtPassword.setBackground(Color.decode("#DCDCDC"));
		frame.getContentPane().add(txtPassword);
		
		String login = "C:\\work\\Project_pokemon\\login_img\\btn.PNG\\";
		ImageIcon lg = new ImageIcon(login);
		JButton btnLogin = new JButton();
		btnLogin.setIcon(lg);
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				if(e.getSource() == btnLogin) { 
					init();
					password = txtPassword.getText(); 
					username = txtUsername.getText();
					
					//회원가입한 회원들의 ID 와 PASSWORD 확인
						try {
							if(rs.next()) {
								String id = rs.getString("t_id");						
								String pw = rs.getString("t_passwd");
								String no = rs.getString("t_no");
								String name = rs.getString("t_name");
								if(username == null || username.equals("")) return;
								if(password == null || password.equals("")) return;
		
								if (username.equalsIgnoreCase(id) && password.equalsIgnoreCase(pw)) {
								txtPassword.setText("");
								txtUsername.setText("");
								
								//로그인 후 넘어가는 화면 창 보이기
								//insert the class name of the main page (XXXXX) here 
//								PokemonSignUp info = new PokemonSignUp(); 
//								PokemonSignUp.main(null);
								new PokemonMain(Integer.parseInt(no), name);
								frame.dispose();
								} else {
									JOptionPane.showMessageDialog(null, "비밀번호를 잘못 입력하셨습니다", "로그인 에러", JOptionPane.ERROR_MESSAGE);
									txtPassword.setText("");
									txtUsername.setText("");
									return;
								}	
							}else {
								JOptionPane.showMessageDialog(null, "존재하지 않는 아이디입니다.");
							}
							
						} catch (Exception e2) {
							System.out.println("login err: " + e2);
						} finally {
							
						}	
				}
			}
		});
		btnLogin.setBounds(400, 120, 110, 180);
		frame.getContentPane().add(btnLogin);
	}
}
