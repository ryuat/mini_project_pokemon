import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Executable;
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

/* login창 -> pokemonMAin 창 -> In 창에 trainerid를 던져준다.  */
public class PokemonIn extends JFrame implements ActionListener{
	JPanel panel, panel2, panel3;
	JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
	JButton btnOk, btnCancel;
	JLabel lblTitle, lblCnt;
	JButton enter = new JButton("확인");
	
	JButton[] btnArr = new JButton[8]; // 버튼8개 array 포켓몬 최대개수
	int[] m_idArr = new int[8]; // 회복소포켓몬 ID array [버튼인덱스]
	int[] p_idArr = new int[8]; // 포켓몬 도감 ID array  [버튼인덱스]
//	int[] t_p_seqArr = new int[8];
	int[] inNoArr = new int[8]; // membermon의 입원 여부 null인지 아닌지
	String[] pokNameArr = new String[8]; // 회복소포켓몬 ID array 
	Vector<Integer> selectedBtnArr = new Vector<>(); // 선택된 버튼번호 array

	String [] PokImgStrArr = new String[8]; // DB에서 가져온 버튼 선택된 p_image1 저장 배열 
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs1;
	
	private int trainerId;
	String smallPokImgDir = "C:\\work\\Project_pokemon\\thm\\"; 
	String cnt;
	public PokemonIn(int trainerId) {
		this.trainerId = trainerId;
		setTitle("입원");
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel.setLayout(new GridLayout(2, 4));
		panel2.setLayout(new BorderLayout());
		
		// DB에서 현재 입원중인 포켓몬 수 가져오기
		lblTitle = new JLabel("입원시킬 포켓몬");
		lblCnt = new JLabel("00마리 입원 가능");
		calCnt();
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int re = JOptionPane.showConfirmDialog(PokemonIn.this, "창을 닫으시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
				if (re == JOptionPane.YES_OPTION) {
					dispose();
				}else {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		// DB에서 p_image1 가져오기
	
		accDb();
		String sql = "select * from membermon m inner join pokemon p on (m.p_id = p.p_id) where m.t_id = ? order by 1,2,3";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ""+trainerId);
			rs1 = pstmt.executeQuery();		
			
			for(int i=0;rs1.next();i++) {
				PokImgStrArr[i] = rs1.getString("p_image1"); // 포켓몬 이미지 문자열 배열
				m_idArr[i] = Integer.parseInt(rs1.getString("m_id")); // 회복소 포켓몬 아이디 배열
//				System.out.println("m_idArr[" +i+"]: " + m_idArr[i]);
				p_idArr[i] = Integer.parseInt(rs1.getString("p_id")); // 포켓몬 도감 아이디 배열
				pokNameArr[i] = rs1.getString("p_name2");
				if(rs1.getString("in_no")!=null)
					inNoArr[i] = Integer.parseInt(rs1.getString("in_no"));
//				System.out.println(pokNameArr[i]);
			}
		} catch (Exception e) {
			System.out.println("DB에서 p_image1 가져오기 err: " + e);
		}
		
	
		// 해당 트레이너의 포켓몬을 버튼 8개에 표시 
		for (int i = 0; i < btnArr.length; i++) {
			ImageIcon icon = new ImageIcon(smallPokImgDir+PokImgStrArr[i]);
			btnArr[i] = new JButton(icon);
			if(PokImgStrArr[i] == null) {
//				btnArr[i].setBackground(Color.lightGray);
				btnArr[i].setEnabled(false);
			}else if(inNoArr[i] != 0){
				btnArr[i].setBackground(new Color(255,0,0));
				btnArr[i].setEnabled(false);
			}else {
				btnArr[i].setBackground(Color.white);
			}
//			System.out.println("inoArr[" + i + "]: " + inNoArr[i]);
			
			btnArr[i].addActionListener(this);
			panel.add(btnArr[i]);
		}
		
		
		panel2.add("North", lblTitle);
		panel2.add("Center", lblCnt);
		btnOk = new JButton("확인");
		btnCancel = new JButton("취소");
		
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
		
		panel3.add(btnOk);
		panel3.add(btnCancel);
		add("North",panel2);
		add("Center", panel);
		add("South", panel3);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(0, 0);
		this.setBounds(600,200,700, 700);
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
				// 회복소 인원수 체크
				int total = Integer.parseInt(cnt)+selectedBtnArr.size();
//				System.out.println(total);
				if(total>50) {
					JOptionPane.showMessageDialog(this, "회복소가 꽉 찼습니다. " +(50-Integer.parseInt(cnt))+"자리밖에 남지 않았습니다.");
					for (int i = 0; i < selectedBtnArr.size(); i++) {
						btnArr[selectedBtnArr.get(i)].setEnabled(true); // 포켓몬번호[i선택인덱스]
					}
					selectedBtnArr.removeAllElements(); 
					return;
				}
				
				int re = JOptionPane.showConfirmDialog(this, "선택된 포켓몬: \n"+selected + "\n\n 입원시키겠습니까?" , "입원", JOptionPane.YES_NO_OPTION);
				if(re == JOptionPane.YES_OPTION) {
					// 선택된 버튼에 있는 포켓몬을 입원(in_history) 테이블에 insert
					for(int i=0;i<selectedBtnArr.size();i++) {
						String sql  = "insert into in_history values ((select nvl(max(in_no),0)+1 from in_history),   ?,   ?,  ?, null, sysdate, null,'in')";
						try { // 입원 insert
//							conn.setAutoCommit(false);
							accDb();
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, "" + m_idArr[selectedBtnArr.get(i)]);
							pstmt.setString(2, "" + trainerId);
							pstmt.setString(3, "" + p_idArr[selectedBtnArr.get(i)]);
							pstmt.executeUpdate();
						} catch (Exception e2) {
							System.out.println("In table insert err: "+ e2);
						}
						try {
							sql  = "update membermon set in_no = (select nvl(max(in_no),0) from in_history) where m_id = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, ""+m_idArr[selectedBtnArr.get(i)]);
							re = pstmt.executeUpdate();
						} catch (Exception e2) {
							System.out.println("In table insert err: "+ e2);
						}
					}
				}else { // 버튼 클릭 후 취소 누를 경우
					
				}
				btnDisp(); // 버튼 리프레시
				calCnt();
			}else return;
			
			// 선택된버튼배열 초기화
			selectedBtnArr.removeAllElements();
		} else if (e.getSource() == btnCancel) {
			int re= JOptionPane.showConfirmDialog(this, "창을 닫으시겠습니까?","종료" , JOptionPane.YES_NO_OPTION);
			if(re == JOptionPane.YES_OPTION) {
				dispose();
			}
		}
	}

	private void btnDisp() { // 버튼 리프레시

		
		String sql = "select * from membermon m inner join pokemon p on (m.p_id = p.p_id) where m.t_id = ? order by 1,2,3";
		try {
			accDb();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ""+trainerId);
			rs1 = pstmt.executeQuery();
			for(int i=0;rs1.next();i++) {
				if(rs1.getString("p_image1") == null) { // 포켓몬 이미지 문자열 배열
					btnArr[i].setEnabled(false);
				}else if(rs1.getString("in_no")!=null){
					btnArr[i].setBackground(new Color(255,0,0));
					btnArr[i].setEnabled(false);
				}else {
					btnArr[i].setBackground(Color.white);
					btnArr[i].setEnabled(true);
				}
			}
		} catch (Exception e2) {
			System.out.println("선택버튼 초기화 err : " + e2);
		} finally {
			try{
				rs1.close();
				pstmt.close();
				conn.close();
			}catch(Exception e3) {
				
			}
		}
	}
	
	private void calCnt() {
//		System.out.println("calCnt 호출");
		String sql = "select count(*) from in_history where complete_time is null";
		try {
			accDb();
			pstmt = conn.prepareStatement(sql);
			rs1 = pstmt.executeQuery();
			rs1.next();
			cnt = rs1.getString(1);
			lblCnt.setText(50-Integer.parseInt(cnt) + "마리 입원 가능");
		} catch (Exception e) {
			System.out.println("calCnt() err: " + e);
		}finally {
			try{
				rs1.close();
				pstmt.close();
				conn.close();
			}catch(Exception e3) {	
			}
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
		new PokemonIn(1); // 트레이너 번호 매개변수
	}
}
