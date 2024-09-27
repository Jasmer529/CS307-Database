package Interface;

import javax.swing.table.DefaultTableModel;

public interface Query {
    String forwardQuery(String station_name, String line_name, int n, DefaultTableModel tableModel);

    String backwardQuery(String station_name, String line_name, int n, DefaultTableModel tableModel);

    int getLineNoByName(String lineName);
}
