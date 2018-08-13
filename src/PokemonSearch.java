import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class PokemonSearch extends JPanel implements ActionListener {
	String baseDir = "C:\\work\\Project_pokemon\\img\\";
	
	JTextField txtSname, txtStype1, txtStype2, txtSattack, txtSdefense, txtSspattack, txtSspdefense, txtShp, txtSspeed;
	JButton btnSname;
	JLabel lblPic;
	DefaultTableModel mod;
	String sql, imgPath, pid;
	File file;

	private int trainerId;
	Connection conn;
	PreparedStatement pstmt1, pstmt2, pstmt3;
	ResultSet rs1, rs2, rs3;

	public PokemonSearch(int t_no) {
//		PokemonSearch search = new PokemonSearch(1);
		JFrame frame = new JFrame("★★★ 포켓몬 도감 ★★★");
		frame.getContentPane().add(this);
		frame.setResizable(false);
		frame.setBounds(600, 250, 700, 700);
	
		this.trainerId = t_no;
		design();
		addListner();
		accDb();
		pokemonDisplay();
		
		frame.setVisible(true);
	}

	private void addListner() {
		btnSname.addActionListener(this);
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
			sql = "select * from pokemon where p_id = ?";
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setString(1, pid);
			rs2 = pstmt2.executeQuery();
			rs2.next();

			imgPath = rs2.getString("p_image1");
			dispImage();

		} catch (Exception e) {
			System.out.println("image display err: " + e);
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

	private void pokemonDisplay() {
	
		mod.setNumRows(0); // 표(목록)을 초기화 시켜주는 역할

		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			sql = "select p_id, p_name2, p_type1, p_type2,  hp, speed, attack, defense, sp_attack, sp_defense from pokemon";
			pstmt3 = conn.prepareStatement(sql);
			rs3 = pstmt3.executeQuery();

			int cou = 0;
			while (rs3.next()) {
				String[] imsi = { rs3.getString(1), rs3.getString(2), rs3.getString(3), rs3.getString(4),
						rs3.getString(5), rs3.getString(6), rs3.getString(7), rs3.getString(8), rs3.getString(9),
						rs3.getString(10) };
				mod.addRow(imsi);
				cou++;
			}
			String[] imsi2 = { null, "도감 내 포켓몬 수: " + cou };
			mod.addRow(imsi2);

		} catch (Exception e) {
			System.out.println("pokemonDisplay err: " + e);
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
		if (e.getSource() == btnSname) { // 포켓몬 검색 버튼
			if (txtSname.getText().equals("")) {
				txtSname.requestFocus();
				JOptionPane.showMessageDialog(this, "포켓몬의 이름을 정확하게 입력해주세요.");
				return;
			}
			sql = "select * from pokemon where p_name2 = ?";
			processPokemon(txtSname.getText().trim());
			display();

		}
	}

	private void processPokemon(String fData) {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.47:1521:orcl", "scott", "tiger");
			pstmt1 = conn.prepareStatement(sql);
			pstmt1.setString(1, fData);

			rs1 = pstmt1.executeQuery();

			if (rs1.next()) {
				pid = rs1.getString("p_id");
				txtSname.setText(rs1.getString("p_name2"));
				txtStype1.setText(rs1.getString("p_type1"));
				txtStype2.setText(rs1.getString("p_type2"));
				txtShp.setText(rs1.getString("hp"));
				txtSspeed.setText(rs1.getString("speed"));
				txtSattack.setText(rs1.getString("attack"));
				txtSdefense.setText(rs1.getString("defense"));
				txtSspattack.setText(rs1.getString("sp_attack"));
				txtSspdefense.setText(rs1.getString("sp_defense"));
			} else {
				JOptionPane.showMessageDialog(this, "도감에 등록된 포켓몬이 아닙니다.");
				txtSname.setText(null); // 메세지 창을 깔끔히 비워주기 위해서
				txtStype1.setText(null);
				txtStype2.setText(null);
				txtShp.setText(null);
				txtSspeed.setText(null);
				txtSattack.setText(null);
				txtSdefense.setText(null);
				txtSspattack.setText(null);
				txtSspdefense.setText(null);
			}
		} catch (Exception e) {
			System.out.println("processPokemon err: " + e);
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (pstmt1 != null)
					pstmt1.close();
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public void design() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// 포켓몬 정보 패널
		JPanel pokemonPn = new JPanel(new GridLayout(5, 4));
		pokemonPn.setBorder(new TitledBorder(new TitledBorder("포켓몬 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.LEFT));
		JPanel pPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pPn2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pPn3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pPn4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel pPn5 = new JPanel(new FlowLayout(FlowLayout.LEFT));

		txtSname = new JTextField("", 10);
		txtStype1 = new JTextField("", 6);
		txtStype2 = new JTextField("", 6);
		txtSattack = new JTextField("", 4);
		txtSdefense = new JTextField("", 4);
		txtSspattack = new JTextField("", 4);
		txtSspdefense = new JTextField("", 4);
		txtShp = new JTextField("", 4);
		txtSspeed = new JTextField("", 4);

		txtStype1.setEditable(false);
		txtStype2.setEditable(false);
		txtSattack.setEditable(false);
		txtSdefense.setEditable(false);
		txtSspattack.setEditable(false);
		txtSspdefense.setEditable(false);
		txtShp.setEditable(false);
		txtSspeed.setEditable(false);

		btnSname = new JButton("검색");
		btnSname.setMargin(new Insets(0, 3, 0, 3));

		pPn1.add(new JLabel("포켓몬 이름: "));
		pPn1.add(txtSname);
		pPn1.add(btnSname);

		pPn2.add(new JLabel("Type 1: "));
		pPn2.add(txtStype1);
		pPn2.add(new JLabel("          Type 2: "));
		pPn2.add(txtStype2);

		pPn3.add(new JLabel("HP: "));
		pPn3.add(txtShp);
		pPn3.add(new JLabel("          Speed: "));
		pPn3.add(txtSspeed);

		pPn4.add(new JLabel("Attack: "));
		pPn4.add(txtSattack);
		pPn4.add(new JLabel("          Defense: "));
		pPn4.add(txtSdefense);

		pPn5.add(new JLabel("Sp_Attack: "));
		pPn5.add(txtSspattack);
		pPn5.add(new JLabel("          Sp_Defense: "));
		pPn5.add(txtSspdefense);

		pokemonPn.add(pPn1);
		pokemonPn.add(pPn2);
		pokemonPn.add(pPn3);
		pokemonPn.add(pPn4);
		pokemonPn.add(pPn5);

		this.add(pokemonPn);

		// ---------- 이미지 패널 관련 -----------
		JPanel picPn = new JPanel(new BorderLayout());
		lblPic = new JLabel(BorderLayout.CENTER);
		picPn.add(lblPic, BorderLayout.CENTER);

		this.add(picPn, BorderLayout.CENTER);
		this.setSize(400, 400);

		// ---------- 포켓몬 목록 관련 -----------
		String[][] data = new String[0][9];
		String[] cols = { "식별", "이름", "타입1", "타입2", "체력", "민첩", "공격", "방어", "s_공격", "s_방어" };
		mod = new DefaultTableModel(data, cols) { // 테이블의 내용을 수정 불가하게 하는 역할
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		JTable tab = new JTable(mod);
		tab.getColumnModel().getColumn(0).setPreferredWidth(5);
		tab.getColumnModel().getColumn(1).setPreferredWidth(10);
		tab.getColumnModel().getColumn(2).setPreferredWidth(5);
		tab.getColumnModel().getColumn(3).setPreferredWidth(5);
		tab.getColumnModel().getColumn(4).setPreferredWidth(5);
		tab.getColumnModel().getColumn(5).setPreferredWidth(5);
		tab.getColumnModel().getColumn(6).setPreferredWidth(5);
		tab.getColumnModel().getColumn(7).setPreferredWidth(5);
		tab.getColumnModel().getColumn(8).setPreferredWidth(5);
		tab.getColumnModel().getColumn(9).setPreferredWidth(5);
		tab.setSelectionBackground(Color.lightGray);
		JScrollPane pa = new JScrollPane(tab);
		this.add(pa);
	}

	public static void main(String[] args) {
		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
