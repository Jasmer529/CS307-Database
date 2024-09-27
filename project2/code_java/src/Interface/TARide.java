package Interface;

import javax.swing.table.DefaultTableModel;

public interface TARide {
    int UpSubwayPass(String pass_id, String station_n, int type);
    int UpSubwayCard(String card_id, String station_n, int type);
   // void DownSubwayPass();
   // void DownSubwayCard();
    int DownSubwayPass(String pass_id, String station_n);
    int DownSubwayCard(String card_id, String station_n);

    void currentPass(DefaultTableModel tableModel);
    void currentCard(DefaultTableModel tableModel);
    boolean passExistinSub(String name);
    boolean cardExistinSub(String name);

    boolean stationExist(String name);
    boolean cardExist(String name);
    boolean passExist(String name);
    String checkState(String name);
}
