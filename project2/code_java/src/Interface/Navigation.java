package Interface;

import javax.swing.table.DefaultTableModel;

public interface Navigation {
    int Nav(String start, String end, DefaultTableModel tableModel, DefaultTableModel tableModel2);
    boolean stationExist(String name);
}
