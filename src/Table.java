import java.awt.*;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class Table extends JPanel {

	JTable jt;

	Table() {

		String[] columns = { "Name", "Age", "Gender" };
		String[][] data = { { "Tariq", "25", "Male" }, { "Hasan", "29", "Male" }, { "Mahek", "17", "FeMale" },
				{ "Shah", "27", "Male" }, { "Farhad", "20", "Male" } };
		jt = new JTable(data, columns) {
			public boolean isCellEditable(int data, int columns) {
				return false;
			}

			public Component prepareRenderer(TableCellRenderer r, int data, int columns) {
				Component c = super.prepareRenderer(r, data, columns);
				if (data % 2 == 0)
					c.setBackground(Color.WHITE);
				else
					c.setBackground(Color.LIGHT_GRAY);

				return c;
			}
		};
		jt.setPreferredScrollableViewportSize(new Dimension(450, 113));
		jt.setFillsViewportHeight(true);
		JScrollPane js = new JScrollPane(jt);
		add(js);
	}

	public static void main(String[] args) {

		Table t = new Table();
		JFrame f = new JFrame("Table");
		// Container cn = getContentPane();
		f.setVisible(true);
		f.setTitle("Table");
		// setBounds(300,200,350,400);
		f.setSize(500, 500);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(t);
		// cn.setLayout(null);
	}

}
