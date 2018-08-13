import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PokemonCenter extends JFrame{
	public JFrame frame;
	public JButton signin, signup;
	public ImageIcon btnSignin, btnSignup, pikachu;
	public JLabel photo; 
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	private String baseDir = "C:\\work\\Project_pokemon\\singup\\";
	
	public PokemonCenter() {
		design(); 
	}
	
	public void design() {
		frame = new JFrame();
		frame.setBounds(650,300,550, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setTitle("Pokemon Center");
		String titlebar = baseDir + "ball.png\\";
		Image img = Toolkit.getDefaultToolkit().getImage(titlebar);
		frame.setIconImage(img);
		
		String pika = baseDir + "photo.png\\";
		ImageIcon pikachu = new ImageIcon(pika);
		JLabel photo = new JLabel();
		photo.setIcon(pikachu);
		photo.setBounds(44, 60, 200, 200);
		frame.getContentPane().add(photo);
		
		
		String signin = baseDir+"btnsignin.PNG\\";
		ImageIcon si = new ImageIcon(signin);
		JButton btnSignin = new JButton();
		btnSignin.setIcon(si);
		btnSignin.setBounds(230, 20, 150, 280);
		frame.getContentPane().add(btnSignin);
		btnSignin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PokemonLogin pl = new PokemonLogin();
				PokemonLogin.main(null);
				frame.dispose();
			}
		});
		
		String signup = baseDir+"btnsignup.PNG\\";
		ImageIcon su = new ImageIcon(signup);
		JButton btnSignup = new JButton();
		btnSignup.setIcon(su);
		btnSignup.setBounds(380, 20, 150, 280);
		frame.getContentPane().add(btnSignup);
		btnSignup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PokemonSignUp ps = new PokemonSignUp();
				PokemonSignUp.main(null);
				frame.dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		PokemonCenter pc = new PokemonCenter();
		pc.frame.setVisible(true);
	}

}
