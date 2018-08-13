import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class QueryPokemon extends JFrame implements ActionListener{
	JPanel panel, panel2;
	JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
	JButton btnOk, btnCancel;
	JLabel lblTitle, lblCnt;
	JButton enter = new JButton("확인");
	
	JButton[] btnArr = new JButton[8]; // 버튼8개 array 포켓몬 최대개수
	int[] pokIdArr = new int[8]; // 회복소포켓몬 ID array 
	String[] pokNameArr = new String[8]; // 회복소포켓몬 ID array 
	Vector<Integer> selectedBtnArr = new Vector<>(); // 선택된 버튼번호 array

	String [] PokImgStrArr = new String[8]; // DB에서 가져온 버튼 선택된 p_image1 저장 배열 
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs1;
	
	String smallPokImgDir = "C:\\work\\Project_pokemon\\thm\\"; 
	public QueryPokemon() {
		setTitle("검색");
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel2 = new JPanel();
		
		panel.setLayout(new GridLayout(2, 4));
		panel2.setLayout(new BorderLayout());
		
		// DB에서 p_image1 가져오기
		accDb();
		String sql = "select * from membermon m inner join pokemon p on (m.m_pok_no = p.p_id) where m_trainer_id = 1 order by m_id";
		try {
			pstmt = conn.prepareStatement(sql);
			rs1 = pstmt.executeQuery();		
			for(int i=0;rs1.next();i++) {
				PokImgStrArr[i] = rs1.getString("p_image1"); // 포켓몬 이미지 문자열 배열
//				pokIdArr[i] = Integer.parseInt(rs1.getString("m_id")); // 회복소 포켓몬 아이디 배열
				pokNameArr[i] = rs1.getString("p_name2");
				System.out.println(pokNameArr[i]);
			}
		} catch (Exception e) {
			System.out.println("DB에서 p_image1 가져오기 err: " + e);
		}
		
	
		// 해당 트레이너의 포켓몬을 버튼 8개에 표시 
		for (int i = 0; i < btnArr.length; i++) {
			ImageIcon icon = new ImageIcon(smallPokImgDir+PokImgStrArr[i]);
			btnArr[i] = new JButton(icon);
			
			pokIdArr[i] = 100 % (i + 1);
			if(PokImgStrArr[i] == null) {
//				btnArr[i].setBackground(Color.lightGray);
				btnArr[i].setEnabled(false);
			}else {
				btnArr[i].setBackground(Color.white);
			}
			btnArr[i].addActionListener(this);
			panel.add(btnArr[i]);
		}
		
		lblTitle = new JLabel("입원시킬 포켓몬");
		lblCnt = new JLabel("00마리 입원 가능");
		panel2.add("North", lblTitle);
		panel2.add("Center", lblCnt);
		btnOk = new JButton("확인");
		btnCancel = new JButton("취소");
		btnOk.addActionListener(this);
		panel2.add("South", btnOk);
//		panel2.add("South", );
	
		add("North",panel2);
		add("Center", panel);
		add("South", btnOk);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(0, 0);
		this.setSize(500, 500);
//		this.setSize(dimension.width, dimension.height);
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Color selectedBtnCol = new Color(174,197,215);
		if (e.getSource() == btnArr[0]) {
			selectedBtnArr.add(0);
//			btnArr[0].setBackground(selectedBtnCol);
			btnArr[0].setEnabled(false);
		} else if (e.getSource() == btnArr[1]) {
			selectedBtnArr.add(1);
//			btnArr[1].setBackground(selectedBtnCol);
			btnArr[1].setEnabled(false);
		} else if (e.getSource() == btnArr[2]) {
			selectedBtnArr.add(2);
//			btnArr[2].setBackground(selectedBtnCol);
			btnArr[2].setEnabled(false);
		} else if (e.getSource() == btnArr[3]) {
			selectedBtnArr.add(3);
//			btnArr[3].setBackground(selectedBtnCol);
			btnArr[3].setEnabled(false);
		} else if (e.getSource() == btnArr[4]) {
			selectedBtnArr.add(4);
//			btnArr[4].setBackground(selectedBtnCol);
			btnArr[4].setEnabled(false);
		} else if (e.getSource() == btnArr[5]) {
			selectedBtnArr.add(5);
//			btnArr[5].setBackground(selectedBtnCol);
			btnArr[5].setEnabled(false);
		} else if (e.getSource() == btnArr[6]) {
			selectedBtnArr.add(6);
//			btnArr[6].setBackground(selectedBtnCol);
			btnArr[6].setEnabled(false);
		} else if (e.getSource() == btnArr[7]) {
			selectedBtnArr.add(7);
//			btnArr[7].setBackground(selectedBtnCol);
			btnArr[7].setEnabled(false);
		} else if (e.getSource() == btnOk) {
			String selected = "";
			
			// 선택번호의 포켓몬 이름을 출력하기 위해 이름집합 문자열 만들기
			for (int i = 0; i < selectedBtnArr.size(); i++) {
				if(i == selectedBtnArr.size()-1) {
					selected += pokNameArr[selectedBtnArr.get(i)];
					break;
				}
				selected += pokNameArr[selectedBtnArr.get(i)] + ", "; // 포켓몬번호[i선택인덱스]
			}
			
			if(selectedBtnArr.size() != 0) { //선택된 포켓몬이 있다면 
				int re = JOptionPane.showConfirmDialog(this, "선택된 포켓몬: \n"+selected + "\n\n 입원?" , "입원", JOptionPane.YES_NO_OPTION);
				if(re == JOptionPane.YES_OPTION) {
					// 입원(in_history) 테이블에 insert
					// 입원 테이블에 필요한 컬럼이 뭐가 있는지 생각
					
					
				}
			}else return;
			// 선택버튼 초기화
			for (int i = 0; i < selectedBtnArr.size(); i++) {	
				btnArr[selectedBtnArr.get(i)].setBackground(Color.white);	
				btnArr[selectedBtnArr.get(i)].setEnabled(true);
			}
		
			selectedBtnArr.removeAllElements();
		}
	}
	
	private void accDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521/orcl", "scott", "tiger");
		}catch(Exception e) {
			System.out.println("accDb() err: " + e);
		}
	}
	
	public static void main(String[] args) {
		new QueryPokemon();
	}
}
