
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.Statement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Table_insert_Retreving_Data_From_DataBase extends JPanel implements ActionListener {

	static JFrame frame;
	JLabel l1, l2, l3;
	JTextField t1, t2, t3;
	JButton b1;

	static DefaultTableModel dt;
	Connection con = null;
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs;

	Object[][] databaseInfo;
	Object[] columns = { "accode", "name", "surname", "age", "grpcode" };

	Table_insert_Retreving_Data_From_DataBase() {

		JFrame frame = new JFrame();

		frame.setTitle("Table1");

		frame.setVisible(true);

		frame.setBounds(300, 200, 550, 400);

		frame.setLayout(null);

		l1 = new JLabel("          NAME :");
		l1.setBounds(40, 20, 100, 50);
		frame.add(l1);

		l2 = new JLabel("SUR NAME :");
		l2.setBounds(40, 50, 500, 50);
		frame.add(l2);

		l3 = new JLabel("            AGE :");
		l3.setBounds(40, 80, 500, 50);
		frame.add(l3);

		t1 = new JTextField();
		t1.setBounds(130, 30, 180, 30);
		frame.add(t1);

		t2 = new JTextField("");
		t2.setBounds(130, 60, 180, 30);
		frame.add(t2);

		t3 = new JTextField("");
		t3.setBounds(130, 90, 180, 30);
		frame.add(t3);

		b1 = new JButton("SAVE");
		b1.setBounds(40, 280, 80, 25);
		b1.setBackground(Color.GREEN);
		frame.add(b1);

		dt = new DefaultTableModel(databaseInfo, columns) {
			public Class getColumnClass(int column)// this is to check that no
													// of row and column exit
													// and also there is an int
													// value to override
			{
				Class returnValue;

				if ((column >= 0) && (column < getColumnCount())) {
					returnValue = getValueAt(0, column).getClass();
				} else {
					returnValue = Object.class;
				}
				return returnValue;
			}
		};

		JTable table = new JTable(dt);
		table.setBounds(10, 120, 520, 80);
		table.setRowHeight(table.getRowHeight() + 10);

		table.setFont(new Font("Serif", Font.PLAIN, 20));

		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);

		b1.addActionListener(this);

	}

	public static void main(String[] args) {

		Table_insert_Retreving_Data_From_DataBase ti = new Table_insert_Retreving_Data_From_DataBase();
		JTable table = new JTable(dt);
		table.setBounds(10, 120, 520, 80);
		table.setRowHeight(table.getRowHeight() + 10);

		table.setFont(new Font("Serif", Font.PLAIN, 20));

		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(b1)) {
			connect();
			String name = t1.getText();
			String surname = t2.getText();

			int age = Integer.parseInt(t3.getText());
			int age1 = age;

			final int grpcode = 10;

			try {
				int count = -1;
				st = (Statement) con.createStatement();
				String str;
				str = "Select accode, name , surname , age ,grpcode from basic";
				rs = st.executeQuery(str);
				Object[] tempRow;
				while (rs.next()) {
					tempRow = new Object[] { rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4),
							rs.getInt(5) };
					dt.addRow(tempRow);
					// count = rs.getInt(1);
					count++;
				}
				ps = con.prepareStatement(
						"INSERT INTO basic (accode,name , surname , age , grpcode) values(?,?,?,?,?)");
				ps.setInt(1, count);
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
