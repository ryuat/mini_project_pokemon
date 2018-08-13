

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
public class  PokemonStatus extends JFrame implements ActionListener{
	JPanel panel, panel2, panel3, panel4, panel5;
	JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
	JButton btnOk, btnCancel;
	JLabel lblTitle, lblCnt, lblNorth, lblCenter, lblSouth;
	JButton enter = new JButton("퇴원");
	
	JButton[] btnArr = new JButton[8]; // 버튼8개 array 포켓몬 최대개수
	JPanel[] panArr = new JPanel[8];
	int[] m_idArr = new int[8]; // 회복소포켓몬 ID array [버튼인덱스]
	int[] p_idArr = new int[8]; // 포켓몬 도감 ID array  [버튼인덱스]
//	int[] t_p_seqArr = new int[8];
	int[] inNoArr = new int[8]; // membermon의 입원 여부 null인지 아닌지
	String[] pokNameArr = new String[8]; // 회복소포켓몬 ID array 
	Vector<Integer> selectedBtnArr = new Vector<>(); // 선택된 버튼번호 array
	JLabel[][] lblArr = new JLabel[8][3];
	String [] PokImgStrArr = new String[8]; // DB에서 가져온 버튼 선택된 p_image1 저장 배열 
	String [] comTimeArr = new String[8];
	String [] inoutArr = new String[8];
	String [] rsecArr = new String[8];
	String [] rmiArr  = new String[8];
	String [] rhhArr   = new String[8];
	String [] ratioArr = new String[8];
	static private Connection conn;
	static private PreparedStatement pstmt, pstmt2; // 회복완료시간 업데이트 시 동시에 수행하기 때문
	static private ResultSet rs1, rs2;
	
	private int trainerId;
	String smallPokImgDir = "C:\\work\\Project_pokemon\\thm\\"; 
	String cnt;
	
	public PokemonStatus(int trainerId) {
		this.trainerId = trainerId;
		setTitle("입원현황");
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		panel5 = new JPanel();
		panel.setLayout(new GridLayout(4, 4));
		panel2.setLayout(new BorderLayout());
		
		
		lblNorth = new JLabel("");
		lblCenter = new JLabel("");
		lblSouth = new JLabel("");
		
		// DB에서 현재 입원중인 포켓몬 수 가져오기
		lblTitle = new JLabel("입원 현황");
		lblCnt = new JLabel("00마리 입원 가능");
		
		calCnt();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int re = JOptionPane.showConfirmDialog(PokemonStatus.this, "창을 닫으시겠습니까?", "종료", JOptionPane.YES_NO_OPTION);
				if (re == JOptionPane.YES_OPTION) {
					dispose();

				}else {
					setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		// DB에서 p_image1 가져오기
	
		
		String sql = "select  m.m_id m_id, m.t_id t_id, m.p_id p_id, p.p_name2 p_name2, p_image1, m.in_no, \r\n" + 
				"        to_char(complete_time,'yyyy/mm/dd hh24:mi:ss') complete_time, \r\n" + 
				"        inout_status,to_char(in_time,'yyyy/mm/dd hh24:mi:ss') in_time\r\n" + 
				"        , mod(trunc((sysdate-in_time)*24*60*60),60) sec\r\n" + 
				"     ,mod(trunc((sysdate-in_time)*24*60),60) mi\r\n" + 
				"     , mod(trunc((sysdate-in_time)*24),24) hh \r\n" + 
				"     , case when (120 - (sysdate-in_time)*24*60*60 < 0) then 'true'\r\n" + 
				"     else 'false'\r\n" + 
				"     end r_yn\r\n" + 
				"     , mod(trunc(120-(sysdate-in_time)*24*60*60),60) r_sec\r\n" + 
				"     , mod(trunc((120-(sysdate-in_time)*24*60*60)/60),60) r_mi\r\n" + 
				"     , mod(trunc((120-(sysdate-in_time)*24*60*60)/60/60),24) r_hh\r\n" + 
				"     , round((sysdate-in_time)*24*60*60/120*100) ratio\r\n" + 
				"from membermon m, in_history i, pokemon p\r\n" + 
				"where 1=1\r\n" + 
				"and m.in_no = i.in_no(+)\r\n" + 
				"and m.p_id = p.p_id\r\n" + 
				"and m.t_id = ? \r\n" + 
				"order by 1,2,3";
		try {
			accDb();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ""+trainerId);
			rs1 = pstmt.executeQuery();		
			
			for(int i=0;rs1.next();i++) {
				PokImgStrArr[i] = rs1.getString("p_image1"); // 포켓몬 이미지 문자열 배열
				m_idArr[i] = Integer.parseInt(rs1.getString("m_id")); // 회복소 포켓몬 아이디 배열
//				System.out.println("m_idArr[" +i+"]: " + m_idArr[i]);
				p_idArr[i] = Integer.parseInt(rs1.getString("p_id")); // 포켓몬 도감 아이디 배열
				pokNameArr[i] = rs1.getString("p_name2");
				comTimeArr[i] = rs1.getString("complete_time");

				inoutArr[i] = rs1.getString("inout_status");
				rsecArr[i] = rs1.getString("r_sec");
				rmiArr[i] = rs1.getString("r_mi");
				rhhArr[i] = rs1.getString("r_hh");
				ratioArr[i] = rs1.getString("ratio");
				if(rs1.getString("in_no")!=null)
					inNoArr[i] = Integer.parseInt(rs1.getString("in_no"));
//				System.out.println(pokNameArr[i]);
			}
		} catch (Exception e) {
			System.out.println("DB에서 p_image1 가져오기 err: " + e);
		}
		
	
		// 해당 트레이너의 포켓몬을 버튼 8개 생성
		
		for (int i = 0; i < btnArr.length; i++) {
			ImageIcon icon = new ImageIcon(smallPokImgDir+PokImgStrArr[i]);
			btnArr[i] = new JButton(icon);
			
			panArr[i] = new JPanel();
			panArr[i].setLayout(new GridLayout(7, 1));

			if(inoutArr[i] != null && inoutArr[i].equals("in")) {
				if(comTimeArr[i] != null) {
					btnArr[i].setBackground(Color.GREEN);
					btnArr[i].setEnabled(true);
		
					lblArr[i][0] = new JLabel(pokNameArr[i],JLabel.CENTER);
					lblArr[i][0].setFont(new Font("휴먼모음T",Font.BOLD, 20));
					lblArr[i][1] = new JLabel("회복률: 100% ", JLabel.CENTER);
					lblArr[i][1].setFont(new Font("휴먼모음T",Font.CENTER_BASELINE, 15));
					lblArr[i][1].setForeground(Color.GREEN);
					
					lblArr[i][2] = new JLabel("",JLabel.CENTER);
					panArr[i].add("North",lblArr[i][0]);
					panArr[i].add("Center",lblArr[i][1]);
					panArr[i].add("South",lblArr[i][2]);
				}else {
					btnArr[i].setBackground(new Color(255,0,0));
					btnArr[i].setEnabled(false);
					
					lblArr[i][0] = new JLabel(pokNameArr[i],JLabel.CENTER);
					lblArr[i][0].setFont(new Font("휴먼모음T",Font.BOLD, 20));
					lblArr[i][1] = new JLabel("회복률:" + ratioArr[i] +"%", JLabel.CENTER);
					lblArr[i][1].setForeground(Color.RED);
					lblArr[i][1].setFont(new Font("휴먼모음T",Font.CENTER_BASELINE, 16));
					lblArr[i][2] = new JLabel("남은시간:" + rhhArr[i] +":" + rmiArr[i] + ":"+ rsecArr[i] +"",JLabel.CENTER);
					lblArr[i][2].setForeground(Color.RED);
					lblArr[i][2].setFont(new Font("휴먼모음T",Font.BOLD, 14));
					panArr[i].add("North",lblArr[i][0]);
					panArr[i].add("Center",lblArr[i][1]);
					panArr[i].add("South",lblArr[i][2]);
				}
			}else if (pokNameArr[i] != null) {
				btnArr[i].setBackground(Color.white);
				btnArr[i].setEnabled(false);
				
				lblArr[i][0] = new JLabel(pokNameArr[i],JLabel.CENTER);
				lblArr[i][0].setFont(new Font("휴먼모음T",Font.BOLD, 20));
				lblArr[i][1] = new JLabel("활동중",JLabel.CENTER);
				lblArr[i][1].setForeground(Color.BLUE);
				lblArr[i][1].setFont(new Font("휴먼모음T",Font.CENTER_BASELINE, 16));
				
				lblArr[i][2] = new JLabel("");
				
				panArr[i].add(lblArr[i][0]);
				panArr[i].add(lblArr[i][1]);
				panArr[i].add(lblArr[i][2]);
			}else {
				btnArr[i].setBackground(Color.lightGray);
				btnArr[i].setEnabled(false);
				lblArr[i][0] = new JLabel("",JLabel.CENTER);
				lblArr[i][0].setFont(new Font("",Font.BOLD, 20));
				lblArr[i][1] = new JLabel("",JLabel.CENTER);
				lblArr[i][1].setForeground(Color.BLUE);
				lblArr[i][1].setFont(new Font("",Font.CENTER_BASELINE, 16));
				lblArr[i][2] = new JLabel("");
				
				panArr[i].add(lblArr[i][0]);
				panArr[i].add(lblArr[i][1]);
				panArr[i].add(lblArr[i][2]);
			}

			btnArr[i].addActionListener(this);
			panel.add(btnArr[i]);

			panArr[i].setBackground(Color.white);
			panel.add(panArr[i]);
		}
	
		panel2.add("North", lblTitle);
		btnOk = new JButton("확인");
		btnCancel = new JButton("닫기");
		
		btnOk.addActionListener(this);
		btnCancel.addActionListener(this);
		
		panel3.add(btnOk);
		panel3.add(btnCancel);
		add("North",panel2);
		add("Center", panel);
		add("South", panel3);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
//		this.setLocation(0, 0);
//		this.setBounds(600,200,700, 700);
		this.setBounds(600, 250, 700, 700);
//		this.setSize(dimension.width, dimension.height);
		
		setVisible(true);
		calRecv();
		btnDisp();

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Color selectedBtnCol = new Color(174,197,215);
		if (e.getSource() == btnArr[0]) { // 퇴원
			selectedBtnArr.add(0);
			btnArr[0].setEnabled(false);
		} else if (e.getSource() == btnArr[1]) {
			selectedBtnArr.add(1);
			btnArr[1].setEnabled(false);
		} else if (e.getSource() == btnArr[2]) {
			selectedBtnArr.add(2);
			btnArr[2].setEnabled(false);
		} else if (e.getSource() == btnArr[3]) {
			selectedBtnArr.add(3);
			btnArr[3].setEnabled(false);
		} else if (e.getSource() == btnArr[4]) {
			selectedBtnArr.add(4);
			btnArr[4].setEnabled(false);
		} else if (e.getSource() == btnArr[5]) {
			selectedBtnArr.add(5);
			btnArr[5].setEnabled(false);
		} else if (e.getSource() == btnArr[6]) {
			selectedBtnArr.add(6);
			btnArr[6].setEnabled(false);
		} else if (e.getSource() == btnArr[7]) {
			selectedBtnArr.add(7);
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
				int re = JOptionPane.showConfirmDialog(this, "선택된 포켓몬: \n"+selected + "\n\n 퇴원시키겠습니까?" , "퇴원", JOptionPane.YES_NO_OPTION);
				if(re == JOptionPane.YES_OPTION) {
					// 선택된 버튼에 있는 포켓몬을 퇴원: 입원(in_history) 테이블에서 inoustat update 
					for(int i=0;i<selectedBtnArr.size();i++) {
						
						String sql  = "update in_history set inout_status = 'out' where in_no = ?";
						try { // 퇴원1: 입원 update inout = out
//							conn.setAutoCommit(false);
							accDb();
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, "" + inNoArr[selectedBtnArr.get(i)]);
							pstmt.executeUpdate();
						} catch (Exception e2) {
							System.out.println("in_history update inoutstat err: "+ e2);
						}
						try { // 퇴원2: membermon update inno = null
							sql  = "update membermon set in_no = null where m_id = ?";
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, ""+m_idArr[selectedBtnArr.get(i)]);
//							System.out.println(""+m_idArr[selectedBtnArr.get(i)]);
							re = pstmt.executeUpdate();
						} catch (Exception e2) {
							System.out.println("membermon update in_no err: "+ e2);
						}
					}
				}
				calCnt();
				btnDisp(); // 버튼 리프레시
				
			}else {
				JOptionPane.showMessageDialog(this, "퇴원시킬 포켓몬을 선택하세요.");
				return;
			}
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
		// 먼저 회복된 애들 리프레시
		calRecv();
		// DB에서 배열정보 가져오기 2
//		System.out.println("btndisp 호출, 배열정보 가져오기2");
		
		String sql = "select  m.m_id m_id, m.t_id t_id, m.p_id p_id, p.p_name2 p_name2, p_image1, m.in_no, \r\n" + 
				"        to_char(complete_time,'yyyy/mm/dd hh24:mi:ss') complete_time, \r\n" + 
				"        inout_status,to_char(in_time,'yyyy/mm/dd hh24:mi:ss') in_time\r\n" + 
				"        , mod(trunc((sysdate-in_time)*24*60*60),60) sec\r\n" + 
				"     ,mod(trunc((sysdate-in_time)*24*60),60) mi\r\n" + 
				"     , mod(trunc((sysdate-in_time)*24),24) hh \r\n" + 
				"     , case when (120 - (sysdate-in_time)*24*60*60 < 0) then 'true'\r\n" + 
				"     else 'false'\r\n" + 
				"     end r_yn\r\n" + 
				"     , mod(trunc(120-(sysdate-in_time)*24*60*60),60) r_sec\r\n" + 
				"     , mod(trunc((120-(sysdate-in_time)*24*60*60)/60),60) r_mi\r\n" + 
				"     , mod(trunc((120-(sysdate-in_time)*24*60*60)/60/60),24) r_hh\r\n" + 
				"     , round((sysdate-in_time)*24*60*60/120*100) ratio\r\n" + 
				"from membermon m, in_history i, pokemon p\r\n" + 
				"where 1=1\r\n" + 
				"and m.in_no = i.in_no(+)\r\n" + 
				"and m.p_id = p.p_id\r\n" + 
				"and m.t_id = ? \r\n" + 
				"order by 1,2,3";
		try {
			accDb();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ""+trainerId);
			rs1 = pstmt.executeQuery();		
			
			for(int i=0;rs1.next();i++) {
				PokImgStrArr[i] = rs1.getString("p_image1"); // 포켓몬 이미지 문자열 배열
				m_idArr[i] = Integer.parseInt(rs1.getString("m_id")); // 회복소 포켓몬 아이디 배열
//				System.out.println("m_idArr[" +i+"]: " + m_idArr[i]);
				p_idArr[i] = Integer.parseInt(rs1.getString("p_id")); // 포켓몬 도감 아이디 배열
				pokNameArr[i] = rs1.getString("p_name2");
				comTimeArr[i] = rs1.getString("complete_time");
				inoutArr[i] = rs1.getString("inout_status");
				rsecArr[i] = rs1.getString("r_sec");
				rmiArr[i] = rs1.getString("r_mi");
				rhhArr[i] = rs1.getString("r_hh");
				ratioArr[i] = rs1.getString("ratio");
				if(rs1.getString("in_no")!=null)
					inNoArr[i] = Integer.parseInt(rs1.getString("in_no"));
//				System.out.println(pokNameArr[i]);
			}
		} catch (Exception e) {
			System.out.println("DB에서 p_image1 가져오기 err: " + e);
		}
		
		// 배열정보가져오기 2의 정보로 버튼 리프레시
		System.out.println("배열정보가져오기2 의 정보로 버튼 리프레시");
		for (int i = 0; i < btnArr.length; i++) {
			if(inoutArr[i] != null && inoutArr[i].equals("in")) {// 입원 중
			   if(comTimeArr[i] != null) { // 회복완료된 경우(완료시간이 있을 경우)
					btnArr[i].setBackground(Color.GREEN);
					btnArr[i].setEnabled(true);
		
					lblArr[i][0].setText(pokNameArr[i]);
					lblArr[i][0].setFont(new Font("휴먼모음T",Font.BOLD, 20));
				
					lblArr[i][1].setText("회복률: 100%");
					lblArr[i][1].setFont(new Font("휴먼모음T",Font.CENTER_BASELINE, 15));
					lblArr[i][1].setForeground(Color.GREEN);
					
					lblArr[i][2].setText("");
				}else { // 회복이 완료되지 않은 경우
					btnArr[i].setBackground(new Color(255,0,0));
					btnArr[i].setEnabled(false);
				
					lblArr[i][0].setText(pokNameArr[i]);
					lblArr[i][0].setFont(new Font("휴먼모음T",Font.BOLD, 20));
					
					lblArr[i][1].setText("회복률:" + ratioArr[i] +"%");
					lblArr[i][1].setForeground(Color.RED);
					lblArr[i][1].setFont(new Font("휴먼모음T",Font.CENTER_BASELINE, 16));
					
					lblArr[i][2].setText("남은시간:" + rhhArr[i] +":" + rmiArr[i] + ":"+ rsecArr[i] +"");
					lblArr[i][2].setForeground(Color.RED);
					lblArr[i][2].setFont(new Font("휴먼모음T",Font.BOLD, 14));

				}
			}else if (pokNameArr[i] != null) { // 등록된 포켓몬인데 입원 중이지 않은 경우
				btnArr[i].setBackground(Color.white);
				btnArr[i].setEnabled(false);
				
				lblArr[i][0].setText(pokNameArr[i]);
				lblArr[i][0].setFont(new Font("휴먼모음T",Font.BOLD, 20));
				
				lblArr[i][1].setText("활동중");
				lblArr[i][1].setForeground(Color.BLUE);
				lblArr[i][1].setFont(new Font("휴먼모음T",Font.CENTER_BASELINE, 16));
				
				lblArr[i][2].setText("");
			}else { // 버튼이 빈 경우 (8 버튼 중 빈 버튼, 총 8마리를 등록하지 않은 경우에)
				btnArr[i].setBackground(Color.lightGray);
				btnArr[i].setEnabled(false);
				lblArr[i][0] = new JLabel("",JLabel.CENTER);
				lblArr[i][0].setFont(new Font("",Font.BOLD, 20));
				lblArr[i][1] = new JLabel("",JLabel.CENTER);
				lblArr[i][1].setForeground(Color.BLUE);
				lblArr[i][1].setFont(new Font("",Font.CENTER_BASELINE, 16));
				lblArr[i][2] = new JLabel("");
			}
		}
	}
	
	
	static private void calRecv() {
		accDb();
		String sql = "select * from in_history\r\n" + 
				"where complete_time is null and inout_status = 'in'\r\n" + 
				"and 120 - (sysdate-in_time)*24*60*60 < 0";
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs1 = pstmt.executeQuery();
			while(rs1.next()) {
//				System.out.println(rs1.getString("in_no"));
				String sqlU = "update in_history set complete_time = sysdate where in_no = ?";
				pstmt2 = conn.prepareStatement(sqlU);
				pstmt2.setString(1, rs1.getString("in_no"));
				pstmt2.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("calRecv() err: " +e); 
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
	static private void accDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521/orcl", "scott", "tiger");
		}catch(Exception e) {
			System.out.println("accDb() err: " + e);
		}
	}
	
	public static void main(String[] args) {
		new PokemonStatus(1); // 트레이너 번호 매개변수
	}
}
