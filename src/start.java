import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.*;

import javax.swing.*;

import com.mysql.jdbc.Statement;

public class start extends JFrame implements ActionListener {

	JFrame f;
	static JLabel l1, l2, l3;
	static JTextField t1, t2, t3;
	JButton b1, b2, b3, b4;
	static int count;

	Connection con = null;
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs;

	start() {

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

		t1 = new JTextField();
		t1.setBounds(130, 30, 180, 30);
		cn.add(t1);

		t2 = new JTextField("");
		t2.setBounds(130, 60, 180, 30);
		cn.add(t2);

		t3 = new JTextField("");
		t3.setBounds(130, 90, 180, 30);
		cn.add(t3);

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

		b1.addActionListener(this);

		b2.addActionListener(this);

		b3.addActionListener(this);

		b4.addActionListener(this);

	}

	public static void main(String[] args) {
		start s = new start();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(b1)) {
			connect();
			String name = t1.getText();
			String surname = t2.getText();

			int age = Integer.parseInt(t3.getText());
			int age1 = age;

			final int grpcode = 10;

			try {
				count = 1;
				st = (Statement) con.createStatement();
				String str;
				str = "SELECT accode ,name , surname, age ,grpcode FROM basic";
				rs = st.executeQuery(str);
				while (rs.next()) {
					count = count + 1;
					// count++;
				}
				int count1 = count;
				ps = con.prepareStatement(
						"INSERT INTO basic (accode,name , surname , age , grpcode) values(?,?,?,?,?)");
				ps.setInt(1, count1);
				ps.setString(2, name);
				ps.setString(3, surname);
				ps.setInt(4, age1);

				ps.setInt(5, grpcode);

				ps.executeUpdate();
				JOptionPane.showMessageDialog(this, " Data Inserted ");
				t1.setText(" ");
				t2.setText(" ");
				t3.setText(" ");

			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			} finally {
				close();

			}
		} else if (e.getSource().equals(b2)) {
			try {
				connect();
				int count1 = count;
				st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String str;
				String jk = "where accode= '" + count1 + "'";
				str = "SELECT name ,surname ,age ,grpcode FROM basic " + jk;
				rs = st.executeQuery(str);
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			}
		} else if (e.getSource().equals(b3)) {
			try {
				connect();
				int count1 = count;
				st = (Statement) con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				String str;
				// String jk = "where accode= '"+count1+"'";
				str = "SELECT accode ,name ,surname ,age ,grpcode FROM basic WHERE accode = '" + count + "' ";
				rs = st.executeQuery(str);
				while (rs.next()) {
					System.out.println("inside select");
					int acc = rs.getInt("accode");
					String name = rs.getString("name");
					String surname = rs.getString("surname");
					int age = rs.getInt("age");
					final int grpcode = 10;

					t1.setText(name);
					t2.setText(surname);
					t3.setText(" " + age);
					// t3.setText(" " +age);

				} /*
					 * else { JOptionPane.showMessageDialog(
					 * this,"Sorry no more date exits" ); t1.setText(" ");
					 * t2.setText(" "); t3.setText(" ");
					 * 
					 * try{ close(); } catch(Exception j) {
					 * System.out.println("Error : " +j.getMessage()); } }
					 */
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			}
		} else if (e.getSource().equals(b4)) {
			connect();
			// total();
			String name = t1.getText();
			String surname = t2.getText();
			int age = Integer.parseInt(t3.getText());
			// total();

			try {
				String sql = "UPDATE basic_1 set name = '" + name + "', surname = '" + surname + "', age = '" + age
						+ "' ";
				ps = con.prepareStatement(sql);
				ps.execute();
				JOptionPane.showMessageDialog(null, "updated");
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, " Error : " + e1.getMessage());
			}
		}

	}

	public void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/start_root", "root", "");
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

}
