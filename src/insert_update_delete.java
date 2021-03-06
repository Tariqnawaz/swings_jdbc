import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;

import java.sql.*;
import java.text.*;

public class insert_update_delete extends JFrame {

	static JLabel lFirstName, lLastName, lState, lBirthDate;

	static JTextField tfFirstName, tfLastName, tfState, tfBirthDate;

	static java.util.Date dateBirthDate, sqlBirthDate;

	static PreparedStatement ps = null;

	static Connection conn = null;

	static ResultSet rows;

	static Object[][] databaseResults;

	static Object[] columns = { "ID", "First Name ", "Last Name", "State ", "Birth Date" };

	static DefaultTableModel dt = new DefaultTableModel(databaseResults, columns) {
		public Class getColumnClass(int column)// this is to check that no of
												// row and column exit and also
												// there is an int value to
												// override
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

	static JTable table = new JTable(dt);

	public static void main(String[] args) {

		JFrame frame = new JFrame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/start_root", "root", "");

			Statement sqlState = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			String selectStuff = "SELECT pres_id, first_name, last_name, state, birth FROM president1";

			rows = sqlState.executeQuery(selectStuff);

			Object[] tempRow;
			while (rows.next()) {
				tempRow = new Object[] { rows.getInt(1), rows.getString(2), rows.getString(3), rows.getString(4),
						rows.getDate(5) };
				dt.addRow(tempRow);
			}
		}

		catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}

		table.setFont(new Font("Serif", Font.PLAIN, 20));

		table.setRowHeight(table.getRowHeight() + 10);

		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);

		frame.add(scrollPane, BorderLayout.CENTER);

		JButton addPres = new JButton("Add President");

		addPres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sFirstName = "", sLastName = "", sState = "", sDate = "";

				sFirstName = tfFirstName.getText();
				sLastName = tfLastName.getText();
				sState = tfState.getText();
				sDate = tfBirthDate.getText();
				int presID = 0;
				// SimpleDateFormat dateFormatter = new
				// SimpleDateFormat("yyyy-MM-dd");
				sqlBirthDate = getADate(sDate);

				try {
					rows.moveToInsertRow();

					rows.updateString("first_name", sFirstName);
					rows.updateString("last_name", sLastName);
					rows.updateString("state", sState);
					rows.updateDate("birth", (Date) sqlBirthDate);

					rows.insertRow();
					rows.updateRow();

				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				try {
					rows.last();
					presID = rows.getInt(1);
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
				/*
				 * try { //dateBirthDate = dateFormatter.parse(sDate);
				 * //sqlBirthDate = new java.sql.Date(dateBirthDate.getTime());
				 * 
				 * ps = conn.
				 * prepareStatement("INSERT INTO president1(first_name, last_name, state, birth) values(?,?,?,?)"
				 * ); ps.setString(1, sFirstName); ps.setString(2, sLastName);
				 * ps.setString(3, sState); ps.setDate(4,(Date) sqlBirthDate );
				 * //ps.setDate(4, new
				 * java.sql.Date(System.currentTimeMillis()));
				 * 
				 * ps.executeUpdate(); JOptionPane.showInputDialog(this,
				 * " Data Inserted "); tfFirstName.setText(" ");
				 * tfLastName.setText(" "); tfState.setText(" ");
				 * tfBirthDate.setText(" ");
				 * 
				 * } catch (SQLException e1) {
				 * 
				 * e1.printStackTrace(); }
				 */
				Object[] president = { presID, sFirstName, sLastName, sState, sqlBirthDate };

				dt.addRow(president);
			}
		});

		JButton removePres = new JButton("Remove President");

		removePres.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dt.removeRow(table.getSelectedRow());

				try {
					rows.absolute(table.getSelectedRow());
					rows.deleteRow();
				} catch (SQLException e1) {

					e1.printStackTrace();
				}
			}
		});

		lFirstName = new JLabel("First Name");
		lLastName = new JLabel("Last Name");
		lState = new JLabel("State");
		lBirthDate = new JLabel("Birth Date");

		tfFirstName = new JTextField(15);
		tfLastName = new JTextField(15);
		tfState = new JTextField(2);
		tfBirthDate = new JTextField("yyyy-MM-dd", 15);

		JPanel inputPanel = new JPanel();
		inputPanel.add(lFirstName);
		inputPanel.add(tfFirstName);
		inputPanel.add(lLastName);
		inputPanel.add(tfLastName);
		inputPanel.add(lState);
		inputPanel.add(tfState);
		inputPanel.add(lBirthDate);
		inputPanel.add(tfBirthDate);

		inputPanel.add(addPres);
		inputPanel.add(removePres);
		frame.add(inputPanel, BorderLayout.SOUTH);
		System.out.println("inside1");
		table.addMouseListener(new MouseAdapter() {
			public void mouseRealeased(MouseEvent me) {
				System.out.println("inside");
				String value = JOptionPane.showInputDialog(null, "Enter Cell Value");

				if (value != null) {
					// table.setValueAt(value, table.getSelectedRow(),
					// table.getSelectedColumn());
					table.setValueAt(value, table.getSelectedRow(), table.getSelectedColumn());

				}
				String updateCol;
				int convertString;
				try {
					rows.absolute(table.getSelectedRow() + 1);

					updateCol = dt.getColumnName(table.getSelectedColumn());
					convertString = Integer.parseInt(updateCol);
					switch (convertString) {
					case 1:
						sqlBirthDate = getADate(value);
						rows.updateDate(convertString, (Date) sqlBirthDate);
						rows.updateRow();
						break;

					default:
						rows.updateString(convertString, value);
						rows.updateRow();
						break;
					}
				}

				catch (SQLException e1) {

					e1.printStackTrace();
				}

			}

		});

		frame.setSize(1200, 500);
		frame.setVisible(true);

	}

	public static java.util.Date getADate(String sDate) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateBirthDate = dateFormatter.parse(sDate);
			sqlBirthDate = new java.sql.Date(dateBirthDate.getTime());
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return sqlBirthDate;
	}
}
