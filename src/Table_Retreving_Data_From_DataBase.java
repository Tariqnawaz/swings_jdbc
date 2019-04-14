
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class Table_Retreving_Data_From_DataBase extends JPanel {

	static Object[][] databaseInfo;
	static Object[] columns = { "accode", "name", "surname", "age", "grpcode" };

	static ResultSet rows;

	static ResultSetMetaData metaData;

	static DefaultTableModel dt = new DefaultTableModel(databaseInfo, columns) {
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

		frame.setTitle("Table");

		frame.setVisible(true);

		frame.setBounds(300, 200, 350, 400);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/start_root", "root", "");

			Statement sqlState = conn.createStatement();

			String selectStuff = "Select accode, name , surname , age ,grpcode from basic";

			rows = sqlState.executeQuery(selectStuff);
			/*
			 * metaData = rows.getMetaData();
			 * 
			 * int numOfCol = metaData.getColumnCount();
			 * 
			 * columns = new String
			 */
			Object[] tempRow;

			while (rows.next()) {
				tempRow = new Object[] { rows.getInt(1), rows.getString(2), rows.getString(3), rows.getInt(4),
						rows.getInt(5) };
				dt.addRow(tempRow);
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}

		table.setRowHeight(table.getRowHeight() + 10);

		table.setFont(new Font("Serif", Font.PLAIN, 20));

		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);

		frame.add(scrollPane, BorderLayout.CENTER);

	}

}
