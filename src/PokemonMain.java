import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class PokemonMain extends JFrame implements ActionListener
{

	JLabel lbl;
	JButton btn1, btn2, btn3, btn4;
	ImageIcon icon1, icon2, icon3, icon4;
	private int trainerId;
	String trainerName;
	public PokemonMain(int t_no, String name) {
		this.trainerId = t_no;
		this.trainerName = name;
		setTitle("Pokemon");
		
		//버튼 추가 
		addBtn();
		String dir1 = "C:\\work\\icon\\btn1.png";
		String dir2 = "C:\\work\\icon\\btn2.png";
		String dir3 = "C:\\work\\icon\\btn3.png";
		String dir4 = "C:\\work\\icon\\btn4.png";
		
		icon1 = new ImageIcon(dir1);
		icon2 = new ImageIcon(dir2);
		icon3 = new ImageIcon(dir3);
		icon4 = new ImageIcon(dir4);
		
		btn1 = new JButton(icon1);
		btn2 = new JButton(icon2);
		btn3 = new JButton(icon3);
		btn4 = new JButton(icon4);
		
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		btn4.addActionListener(this);
		
		add(btn1);	add(btn2);	add(btn3);	add(btn4);
		
		
//		imageAdd(); //버튼에 사진 입히기
		
		setLayout(new GridLayout(2, 2));
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(0, 0);
		this.setSize(500, 500);
		
		this.setSize(dimension.width, dimension.height);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int re = JOptionPane.showConfirmDialog(PokemonMain.this, "로그아웃하시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
				if (re == JOptionPane.YES_OPTION) {
					dispose();
//					PokemonCenter pc = new PokemonCenter();
//					pc.frame.setVisible(true);
					
				}else {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
	}
	
	public void addBtn() {
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn1) {
			new PokemonIn(trainerId);
		}else if (e.getSource() == btn2) {
			new PokemonSearch(trainerId);
		}else if (e.getSource() == btn3) {
			new PokemonRegister(trainerId, trainerName);
		}else if (e.getSource() == btn4) {
			new PokemonStatus(trainerId);
		}
	}
	public static void main(String[] args) {
		new PokemonMain(1, "한지우");
	}
}
