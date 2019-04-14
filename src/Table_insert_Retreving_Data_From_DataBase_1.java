import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.sql.*;
import java.text.*;

public class Table_insert_Retreving_Data_From_DataBase_1 extends JFrame {

	static JLabel lFirstName, lLastName, lState, lBirthDate;

	static JTextField tfFirstName, tfLastName, tfState, tfBirthDate;

	static java.util.Date dateBirthDate, sqlBirthDate;

	static Object[][] databaseResults;

	static Object[] columns = { "First Name ", "Last Name", "State ", "Birth Date" };

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

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/start_root", "root", "");

			Statement sqlState = conn.createStatement();

			String selectStuff = "SELECT first_name, last_name, state, birth FROM president";

			ResultSet rows = sqlState.executeQuery(selectStuff);

			Object[] tempRow;

			{
				tempRow = new Object[] { rows.getString(1), rows.getString(2), rows.getString(3), rows.getDate(4) };
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

				SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

				try {
					dateBirthDate = dateFormatter.parse(sDate);
					sqlBirthDate = new java.sql.Date(dateBirthDate.getTime());
				} catch (ParseException ex) {
					ex.printStackTrace();
				}
				Object[] president = { sFirstName, sLastName, sState, sqlBirthDate };

				dt.addRow(president);
			}
		});

		JButton removePres = new JButton("Remove President");

		removePres.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dt.removeRow(table.getSelectedRow());

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

		frame.setSize(1200, 500);
		frame.setVisible(true);

	}

}
