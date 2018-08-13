import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PokemonRegister extends JPanel implements ActionListener {
	String baseDir = "C:\\work\\Project_pokemon\\thm\\";
	
	JFrame frame;
	JTextField txtRname, txtPname;
	JButton btnRname, btnPname, btnPregi, btnPnew, btnPclose;
	JLabel lblPic;
	String sql, sql2, sql3, imgPath, pid;
	File file;
	DefaultTableModel mod;
	
	
	Connection conn;
	PreparedStatement pstmt, pstmt2, pstmt3;
	ResultSet rs, rs2, rs3;
	private int trainerId;
	private String trainerName;

	public PokemonRegister(int t_id, String name) {
		this.trainerId = t_id;
		this.trainerName = name;
		design();
		addListener();
		accDb();
		
		frame = new JFrame("★★★ 포켓몬 등록 명단 ★★★");
		frame.getContentPane().add(this);
		frame.setResizable(false);
		frame.setBounds(750, 300, 400, 400);
		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addListener() {
		btnRname.addActionListener(this);
		btnPname.addActionListener(this);
		btnPregi.addActionListener(this);
		btnPnew.addActionListener(this);
		btnPclose.addActionListener(this);
	}

	private void accDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
		} catch (Exception e) {
			System.out.println("accDb err: " + e);
		}
	}

	private void display() {
		try {
			sql3 = "select * from pokemon where p_id = ?";
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			pstmt2 = conn.prepareStatement(sql3);
			pstmt2.setString(1, pid);
			rs2 = pstmt2.executeQuery();
			rs2.next();
			
			imgPath = rs2.getString("p_image3");
			dispImage();
		} catch (Exception e) {
			System.out.println("pokemon image display err: " + e);
		}
	}

	private void dispImage() {
		if (imgPath != null) {
		
			ImageIcon icon = new ImageIcon(baseDir + imgPath);
			lblPic.setIcon(icon);
			lblPic.setToolTipText("이미지 파일명: " + imgPath);
			if (icon != null)
				lblPic.setText(null);
			else
				lblPic.setText("해당하는 이미지가 없습니다.");
		}
	}

	private void registerDisplay() {
		mod.setNumRows(0); // 표(목록)을 초기화 시켜주는 역할

		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			sql = "insert p_name2 from pokemon";
			pstmt3 = conn.prepareStatement(sql);
			rs3 = pstmt3.executeQuery();

			int cou = 0;
			while (rs3.next()) {
				String[] imsi = { rs3.getString(2) };
				mod.addRow(imsi);
				cou++;
			}
			String[] imsi2 = { null, "등록 한 포켓몬 수: " + cou };
			mod.addRow(imsi2);

		} catch (Exception e) {
			System.out.println("registerDisplay err: " + e);
		} finally {
			try {
				if (rs3 != null)
					rs3.close();
				if (pstmt3 != null)
					pstmt3.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnRname) { // 트레이너 이름 입력 버튼
			if (txtRname.getText().equals("")) {
				txtRname.requestFocus();
				JOptionPane.showMessageDialog(this, "트레이너 이름을 입력하세요.");
				return;
			}
			sql = "select * from trainer where t_name = ?";
			processTrainer(txtRname.getText().trim());
		} else if (e.getSource() == btnPname) { // 포켓몬 이름 입력 버튼
			if (txtPname.getText().equals("")) {
				txtPname.requestFocus();
				JOptionPane.showMessageDialog(this, "포켓몬 이름을 입력하세요");
				return;

			}
			sql3 = "select * from pokemon where p_name2 = ?";
			processPokemon(txtPname.getText().trim());
			display();
		} else if (e.getSource() == btnPregi) { // 포켓몬 등록 버튼
			if(pid ==null) { // 검색이 안됐을 경우
				JOptionPane.showMessageDialog(this, "먼저 등록할 포켓몬을 검색하세요.");
				return;
			}
			sql  = "select count(*) cnt from membermon where t_id = ?";
			
			try {
				pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				pstmt.setString(1, ""+trainerId);
				rs = pstmt.executeQuery();
				rs.next();
				int count = Integer.parseInt(rs.getString("cnt")) ;
//				System.out.println(count);
				if(count > 8) {
					JOptionPane.showMessageDialog(this, "최대 포켓몬볼의 개수를 초과하였습니다(8마리 초과).");
				}
			} catch (Exception e2) {
				System.out.println("count 가져오기 err : " + e2);
			}
			sql  = "select nvl(max(m_id),0) m_id from membermon";
			try { // 마지막 번호 가져오기
				pstmt = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				rs = pstmt.executeQuery();
				int last;
				if(rs.next()) {
					rs.last();
					last = Integer.parseInt(rs.getString("m_id")) ;
				}else {
					last = 0;
				}
					
				System.out.println("last: " + last);
				sql  = "insert into membermon values (?,?,?,null,null)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, (last+1)+"");
				pstmt.setString(2, trainerId+"");
				pstmt.setString(3, pid+"");
				int res = pstmt.executeUpdate();
				if (res >= 0) {
					JOptionPane.showMessageDialog(this, "등록완료");
				}
				
			}catch(Exception e2) {
				System.out.println("마지막번호 + insert membermon err: "  + e2);
			}
			
		} else if (e.getSource() == btnPclose) { // 닫기 버튼 관련
			int res=  JOptionPane.showConfirmDialog(this, "닫으시겠습니까?", "닫기", JOptionPane.YES_NO_OPTION);
			if(res == JOptionPane.YES_OPTION) {
				frame.dispose();
				try {
					if (rs != null)
						rs.close();
					if (rs2 != null)
						rs2.close();
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (Exception e2) {
					System.out.println("닫기 오류: " + e2);
				} finally {
					// ##### main과 연결 #####
					// ##### main과 연결 #####
				}
			}
		
		}
	}

	private void processTrainer(String fData) {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fData);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				txtRname.setText(rs.getString("t_name"));
			} else {
				JOptionPane.showMessageDialog(this, "등록된 트레이너가 아닙니다. 안내센터에 문의해 주세요.");
				txtRname.setText(null);
			}
		} catch (Exception e) {
			System.out.println("processTrainer err: " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

	}

	private void processPokemon(String fData) {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			pstmt = conn.prepareStatement(sql3);
			pstmt.setString(1, fData);

			rs2 = pstmt.executeQuery();

			if (rs2.next()) {
				pid = rs2.getString("p_id");
				txtPname.setText(rs2.getString("p_name2"));
			} else {
				JOptionPane.showMessageDialog(this, "포켓몬 도감에 등록된 포켓몬이 아닙니다. 다시 입력해주세요.");
				txtPname.setText(null);
			}
		} catch (Exception e) {
			System.out.println("processPokemon err: " + e);
		} finally {
			try {
				if (rs2 != null)
					rs2.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public void design() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// ---------- 트레이너 정보 패널
		JPanel trainerPn = new JPanel(new GridLayout(2, 2));
		trainerPn.setBorder(new TitledBorder(new TitledBorder("트레이너 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.LEFT));
		JPanel tPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		txtRname = new JTextField(trainerName, 5);
		txtRname.setEditable(false);

		btnRname = new JButton("입력");
		btnRname.setMargin(new Insets(0, 3, 0, 3));

		tPn1.add(new JLabel("이름: "));
		tPn1.add(txtRname);
		tPn1.add(btnRname);

		trainerPn.add(tPn1);

		this.add(trainerPn);

		// ---------- 전체 패널 frame
		JPanel pokemonPn = new JPanel(new GridLayout(2, 2));
		pokemonPn.setBorder(new TitledBorder(new TitledBorder("포켓몬 등록 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.LEFT));

		// 포켓몬 등록 패널
		JPanel pPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		txtPname = new JTextField("", 5);
		btnPname = new JButton("검색");
		btnPname.setMargin(new Insets(0, 3, 0, 3));

		pPn1.add(new JLabel("이름: "));
		pPn1.add(txtPname);
		pPn1.add(btnPname);

		pokemonPn.add(pPn1);

		// ---------- 포켓몬 이미지 출력 패널
		JPanel pPn2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		lblPic = new JLabel(imgPath);

		pPn2.add(new JLabel(""));
		pPn2.add(lblPic);

		pokemonPn.add(pPn2);

		this.add(pokemonPn);

		// ---------- 등록 완료 버튼 관련
		JPanel bottomPn = new JPanel();
		bottomPn.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		btnPregi = new JButton("등록 확인");
		btnPnew = new JButton("새로 입력");
		btnPclose = new JButton("닫기");

		bottomPn.add(btnPregi);
//		bottomPn.add(btnPnew);
		JLabel lbl = new JLabel("     "); // 버튼 사이에 공백을 부여
		bottomPn.add(lbl);
		bottomPn.add(btnPclose);

		this.add(bottomPn);
//		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// ---------- 등록한 포켓몬 목록 관련
//		String[][] data = new String[0][1];
//		String[] cols = { "번호", "이름" };
//		mod = new DefaultTableModel(data, cols) { // 테이블의 내용을 수정하지 못하게 하는 역할
//			public boolean isCellEditable(int rowIndex, int mColIndex) {
//				return false;
//			}
//
//		};

//		JTable tab = new JTable(mod);
//		tab.getColumnModel().getColumn(0).setPreferredWidth(5);
//		tab.getColumnModel().getColumn(1).setPreferredWidth(10);
//		tab.setSelectionBackground(Color.LIGHT_GRAY);
//		JScrollPane pa = new JScrollPane(tab);
//		this.add(pa);
	}

	public static void main(String[] args) {
		PokemonRegister register = new PokemonRegister(1, "한지우");
		

	}

}
