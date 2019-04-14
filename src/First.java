import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.*;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class First extends JFrame implements ActionListener {

	JFrame f;
	JLabel l1, l2, l3, l4;
	JTextField t1, t2, t3, t4;
	JButton b1, b2, b3, b4, b5;

	java.util.Date dateBirthDate, sqlBirthDate;

	Connection con = null;
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs;

	First() {

		Container cn = getContentPane();
		setVisible(true);
		setTitle("Customer");
		setBounds(300, 200, 350, 400);
		cn.setLayout(null);

		l1 = new JLabel("          NAME :");
		l1.setBounds(40, 20, 100, 50);
		cn.add(l1);

		l2 = new JLabel("SUR NAME :");
		l2.setBounds(40, 50, 500, 50);
		cn.add(l2);

		l3 = new JLabel("            AGE :");
		l3.setBounds(40, 80, 500, 50);
		cn.add(l3);

		l4 = new JLabel("       DATE :");
		l4.setBounds(40, 110, 500, 50);
		cn.add(l4);

		t1 = new JTextField();
		t1.setBounds(130, 30, 180, 30);
		cn.add(t1);

		t2 = new JTextField("");
		t2.setBounds(130, 60, 180, 30);
		cn.add(t2);

		t3 = new JTextField("");
		t3.setBounds(130, 90, 180, 30);
		cn.add(t3);

		t4 = new JTextField("");
		t4.setBounds(130, 120, 180, 30);
		cn.add(t4);

		b1 = new JButton("SAVE");
		b1.setBounds(40, 280, 80, 25);
		b1.setBackground(Color.GREEN);
		cn.add(b1);

		b2 = new JButton("see");
		b2.setBounds(140, 280, 80, 25);
		b2.setBackground(Color.GREEN);
		cn.add(b2);

		b3 = new JButton("next");
		b3.setBounds(240, 280, 80, 25);
		b3.setBackground(Color.GREEN);
		cn.add(b3);

		b4 = new JButton("update");
		b4.setBounds(40, 320, 80, 25);
		b4.setBackground(Color.GREEN);
		cn.add(b4);

		b5 = new JButton("delete");
		b5.setBounds(140, 320, 80, 25);
		b5.setBackground(Color.GREEN);
		cn.add(b5);

		b1.addActionListener(this);

		b2.addActionListener(this);

		b3.addActionListener(this);

		b4.addActionListener(this);

		b5.addActionListener(this);
	}

	public static void main(String[] args) {
		First s = new First();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(b1)) {
			connect();
			String name = t1.getText();
			String surname = t2.getText();
			String date = t4.getText();
			int age = Integer.parseInt(t3.getText());
			int age1 = age;
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			// final int grpcode = 10;

			try { /*
					 * int count = -1; st = (Statement) con.createStatement();
					 * String str; str = "SELECT COUNT(*) FROM basic"; rs =
					 * st.executeQuery(str); while(rs.next()){ count =
					 * rs.getInt(1); //count++; }
					 */

				dateBirthDate = dateFormatter.parse(date);
				sqlBirthDate = new java.sql.Date(dateBirthDate.getTime());
				ps = (PreparedStatement) con
						.prepareStatement("INSERT INTO basic_1 (name , surname , age ,date) values(?,?,?,?)");

				ps.setString(1, name);
				ps.setString(2, surname);
				ps.setInt(3, age1);
				ps.setDate(4, (Date) sqlBirthDate);

				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, " Data Inserted ");
				t1.setText(" ");
				t2.setText(" ");
				t3.setText(" ");
				t4.setText(" ");

			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			} catch (ParseException ex) {
				ex.printStackTrace();
			} finally {
				close();

			}
		} else if (e.getSource().equals(b2)) {
			total();
		} else if (e.getSource().equals(b3)) {
			try {
				if (rs.next()) {
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					int age = rs.getInt("age");

					t1.setText(name);
					t2.setText(surname);
					t3.setText(" " + age);

				} else {
					JOptionPane.showMessageDialog(this, "Sorry no more date exits");
					t1.setText(" ");
					t2.setText(" ");
					t3.setText(" ");

					try {
						close();
					} catch (Exception j) {
						System.out.println("Error : " + j.getMessage());
					}
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			}
		} else if (e.getSource().equals(b4)) {
			connect();
			total();
			String name = t1.getText();
			String surname = t2.getText();
			int age = Integer.parseInt(t3.getText());
			total();

			try {
				String sql = "UPDATE basic_1 set name = '" + name + "', surname = '" + surname + "', age = '" + age
						+ "' ";
				ps = (PreparedStatement) con.prepareStatement(sql);
				ps.execute();
				JOptionPane.showMessageDialog(null, "updated");
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			}
		} else if (e.getSource().equals(b5)) {
			connect();
			// total();
			String name = t1.getText();
			String surname = t2.getText();
			// String age = t3.getText();
			int age = Integer.parseInt(t3.getText());
			// total();

			try {
				// if(rs.next())
				if (age != 0)
					ps = (PreparedStatement) con.prepareStatement("Delete form basic_1 where accode = accode");

				ps.setString(1, name);
				ps.setString(2, surname);
				ps.setInt(3, age);
				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, "Deleted Successfully");
				// ps.execute();
				// JOptionPane.showMessageDialog(null,"updated");

			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			}
		}
	}

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/start_root", "root", "");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error :" + ex.getMessage());
		}
	}

	public void close() {
		try {
			con.close();
			st.close();
			ps.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error :" + ex.getMessage());
		}
	}

	public void total() {
		try {
			connect();
			st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String str;
			str = "SELECT name ,surname ,age FROM basic_1 WHERE accode =accode ";
			rs = st.executeQuery(str);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error :" + ex.getMessage());
		}
	}
}
