package Interface;

import javax.swing.table.DefaultTableModel;

public interface AskR {
    int askRide1(String name, String staN, String time1, String time2, DefaultTableModel tableModel);
    int askRide2(String id, String staN, String time1, String time2, DefaultTableModel tableModel);
    boolean stationExist(String name);
}
