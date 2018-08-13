
import java.awt.*;
import java.awt.BorderLayout;
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

import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class TrainerSearch extends JPanel implements ActionListener {
	JTextField txtTname, txtTbadge1, txtTbadge2, txtTbadge3, txtTbadge4, txtTbadge5, txtTbadge6, txtTbadge7, txtTbadge8,
			txtTmon1, txtTmon2, txtTmon3, txtTmon4, txtTmon5, txtTmon6, txtTmon7, txtTmon8;
	JButton btnTname;
	JLabel lblPic, lblPic2;
	JPanel picPn, picPn2;
	File file;

	Connection conn;
	PreparedStatement pstmt, pstmt2;
	ResultSet rs, rs2;
	String sql, imgPath, tid;

	private int t_no;
	public TrainerSearch(int t_no) {
		this.t_no = t_no;
		design();
		addListner();
		accDb();
		display();
		JFrame frame = new JFrame("★★★ 트레이너 검색 ★★★");
		frame.getContentPane().add(this);
		frame.setResizable(false);
		frame.setBounds(600, 250, 700, 700);
		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addListner() {
		btnTname.addActionListener(this);
	}

	private void accDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
		} catch (Exception e) {
			System.out.println("accDb err: " + e);
		}
	}

	private void display() {
		try {
			sql = "select * from trainer where t_no = ?";
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			pstmt2 = conn.prepareStatement((sql), ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pstmt2.setString(1, tid);
			rs2 = pstmt2.executeQuery();
			rs2.next();

			imgPath = rs2.getString("t_image");

			dispImage();

		} catch (Exception e) {
			System.out.println("show image display err: " + e);
		}
	}

	private void dispImage() {
		if (imgPath != null) {
			String dir = "C:\\Users\\kitcoop\\Desktop\\trainer_img\\";
			ImageIcon icon = new ImageIcon(dir + imgPath);
			lblPic.setIcon(icon);
			lblPic.setToolTipText("이미지 파일명: " + imgPath);
			if (icon != null)
				lblPic.setText(null);
			else
				lblPic.setText("해당하는 이미지가 없습니다.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnTname) { // 트레이너 검색 버튼
			if (txtTname.getText().equals("")) {
				txtTname.requestFocus();
				JOptionPane.showMessageDialog(this, "트레이너의 이름을 정확하게 입력해주세요.");
				return;
			}
			sql = "select * from trainer where t_name = ?";
			processTrainer(txtTname.getText().trim());
			display();
		}

	}

	private void processTrainer(String fData) {
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "tiger");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, fData);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				tid = rs.getString("t_no");
				txtTbadge1.setText(rs.getString("t_badge1"));
				txtTbadge2.setText(rs.getString("t_badge2"));
				txtTbadge3.setText(rs.getString("t_badge3"));
				txtTbadge4.setText(rs.getString("t_badge4"));
				txtTbadge5.setText(rs.getString("t_badge5"));
				txtTbadge6.setText(rs.getString("t_badge6"));
				txtTbadge7.setText(rs.getString("t_badge7"));
				txtTbadge8.setText(rs.getString("t_badge8"));

				txtTmon1.setText(rs.getString("t_mon1"));
				txtTmon2.setText(rs.getString("t_mon2"));
				txtTmon3.setText(rs.getString("t_mon3"));
				txtTmon4.setText(rs.getString("t_mon4"));
				txtTmon5.setText(rs.getString("t_mon5"));
				txtTmon6.setText(rs.getString("t_mon6"));
				txtTmon7.setText(rs.getString("t_mon7"));
				txtTmon8.setText(rs.getString("t_mon8"));
			} else {
				JOptionPane.showMessageDialog(this, "등록되지 않거나 올바르지 않은 트레이너명입니다.");
				txtTbadge1.setText(null); // 출력창을 비워주는 역할
				txtTbadge2.setText(null);
				txtTbadge3.setText(null);
				txtTbadge4.setText(null);
				txtTbadge5.setText(null);
				txtTbadge6.setText(null);
				txtTbadge7.setText(null);
				txtTbadge8.setText(null);
				txtTmon1.setText(null);
				txtTmon2.setText(null);
				txtTmon3.setText(null);
				txtTmon4.setText(null);
				txtTmon5.setText(null);
				txtTmon6.setText(null);
				txtTmon7.setText(null);
				txtTmon8.setText(null);
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

	public void design() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// ---------- 이미지 패널 관련
		picPn = new JPanel(new BorderLayout());
		lblPic = new JLabel(BorderLayout.CENTER);

		picPn.add(lblPic, BorderLayout.CENTER);
		this.add(picPn, BorderLayout.CENTER);

		// ----------- 트레이너 검색 and 정보 패널
		JPanel trainerPn = new JPanel(new GridLayout(6, 6));
		trainerPn.setBorder(new TitledBorder(new TitledBorder("트레이너 정보"), "", TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.LEFT));
		JPanel tPn1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel tPn2 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 배지 1행
		JPanel tPn3 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 배지 2행
		JPanel tPn4 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 몬스터 1행
		JPanel tPn5 = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 몬스터 2행

		txtTname = new JTextField("", 6);
		btnTname = new JButton("검색");

		txtTbadge1 = new JTextField("", 2);
		txtTbadge2 = new JTextField("", 2);
		txtTbadge3 = new JTextField("", 2);
		txtTbadge4 = new JTextField("", 2);
		txtTbadge5 = new JTextField("", 2);
		txtTbadge6 = new JTextField("", 2);
		txtTbadge7 = new JTextField("", 2);
		txtTbadge8 = new JTextField("", 2);
		txtTmon1 = new JTextField("", 4);
		txtTmon2 = new JTextField("", 4);
		txtTmon3 = new JTextField("", 4);
		txtTmon4 = new JTextField("", 4);
		txtTmon5 = new JTextField("", 4);
		txtTmon6 = new JTextField("", 4);
		txtTmon7 = new JTextField("", 4);
		txtTmon8 = new JTextField("", 4);
		btnTname.setMargin(new Insets(0, 3, 0, 3));

		txtTbadge1.setEditable(false);
		txtTbadge2.setEditable(false);
		txtTbadge3.setEditable(false);
		txtTbadge4.setEditable(false);
		txtTbadge5.setEditable(false);
		txtTbadge6.setEditable(false);
		txtTbadge7.setEditable(false);
		txtTbadge8.setEditable(false);
		txtTmon1.setEditable(false);
		txtTmon2.setEditable(false);
		txtTmon3.setEditable(false);
		txtTmon4.setEditable(false);
		txtTmon5.setEditable(false);
		txtTmon6.setEditable(false);
		txtTmon7.setEditable(false);
		txtTmon8.setEditable(false);

		tPn1.add(new JLabel("트레이너 이름: "));
		tPn1.add(txtTname);
		tPn1.add(btnTname);

		tPn2.add(new JLabel("배지 1: "));
		tPn2.add(txtTbadge1);
		tPn2.add(new JLabel("배지 2: "));
		tPn2.add(txtTbadge2);
		tPn2.add(new JLabel("배지 3: "));
		tPn2.add(txtTbadge3);
		tPn2.add(new JLabel("배지 4: "));
		tPn2.add(txtTbadge4);

		tPn3.add(new JLabel("배지 5: "));
		tPn3.add(txtTbadge5);
		tPn3.add(new JLabel("배지 6: "));
		tPn3.add(txtTbadge6);
		tPn3.add(new JLabel("배지 7: "));
		tPn3.add(txtTbadge7);
		tPn3.add(new JLabel("배지 8: "));
		tPn3.add(txtTbadge8);

		tPn4.add(new JLabel("몬스터 1: "));
		tPn4.add(txtTmon1);
		tPn4.add(new JLabel("몬스터 2: "));
		tPn4.add(txtTmon2);
		tPn4.add(new JLabel("몬스터 3: "));
		tPn4.add(txtTmon3);
		tPn4.add(new JLabel("몬스터 4: "));
		tPn4.add(txtTmon4);

		tPn5.add(new JLabel("몬스터 5: "));
		tPn5.add(txtTmon5);
		tPn5.add(new JLabel("몬스터 6: "));
		tPn5.add(txtTmon6);
		tPn5.add(new JLabel("몬스터 7: "));
		tPn5.add(txtTmon7);
		tPn5.add(new JLabel("몬스터 8: "));
		tPn5.add(txtTmon8);

		trainerPn.add(tPn1);
		trainerPn.add(tPn2);
		trainerPn.add(tPn3);
		trainerPn.add(tPn4);
		trainerPn.add(tPn5);

		this.add(trainerPn);
	}

	public static void main(String[] args) {
	
	}

}
