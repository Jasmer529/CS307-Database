package Update;

import Interface.Navigation;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static IMTRY.controlDB.*;

public class Naviga implements Navigation {
    public int Nav(String start, String end, DefaultTableModel tableModel, DefaultTableModel tableModel2) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!stationExist(start)){
            closeDB();
            return 1;
        }
        if(!stationExist(end)){
            closeDB();
            return 1;
        }
        int r = 0;
        try {
            stmt = con.prepareStatement("SELECT s.chinese_name AS station_name,\n" +
                    "       l.line_name AS line_name\n" +
                    "FROM connection c\n" +
                    "         INNER JOIN (SELECT line_No, station_No\n" +
                    "                     FROM connection\n" +
                    "                     WHERE station_n = ?) AS first_station_lines\n" +
                    "                    ON c.line_No = first_station_lines.line_No\n" +
                    "         INNER JOIN (SELECT line_No, station_No\n" +
                    "                     FROM connection\n" +
                    "                     WHERE station_n = ?) AS second_station_lines ON c.line_No = second_station_lines.line_No\n" +
                    "         JOIN stations s ON c.station_n = s.station_name\n" +
                    "         JOIN `lines` l ON c.line_No = l.id\n" +
                    "WHERE (c.station_No <= first_station_lines.station_No AND c.station_No >= second_station_lines.station_No)\n" +
                    "   OR (c.station_No >= first_station_lines.station_No AND c.station_No <= second_station_lines.station_No)\n" +
                    "ORDER BY c.station_No;");
            stmt.setString(1, start);
            stmt.setString(2, end);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                rs = stmt.executeQuery();
                while (rs.next())
                {
                    String stationName = rs.getString("station_name");
                    String lineName = rs.getString("line_name");
                    tableModel.addRow(new Object[]{stationName, lineName});
                    r = 3;
                }
                con.commit();
            }else {
                stmt = con.prepareStatement("SELECT st.chinese_name                                                        AS '初始站',\n" +
                        "       l.line_name                                                            AS '线路1',\n" +
                        "       ABS(c.station_No - c1.station_No)                                      AS '站数1',\n" +
                        "       st1.chinese_name                                                       AS '换乘站1',\n" +
                        "       l1.line_name                                                           AS '线路2',\n" +
                        "       ABS(c2.station_No - c3.station_No)                                     AS '站数2',\n" +
                        "       NULL                                                                   AS '换乘站2',\n" +
                        "       NULL                                                                   AS '线路3',\n" +
                        "       NULL                                                                   AS '站数3',\n" +
                        "       st3.chinese_name                                                       AS '目的地',\n" +
                        "       ABS(c.station_No - c1.station_No) + ABS(c2.station_No - c3.station_No) AS '总站数'\n" +
                        "FROM connection c\n" +
                        "         JOIN connection c1 ON (c.line_No = c1.line_No)\n" +
                        "         JOIN stations st ON (c.station_n = st.station_name)\n" +
                        "         JOIN stations st1 ON (c1.station_n = st1.station_name)\n" +
                        "         JOIN connection c2 ON (c2.line_No != c1.line_No AND c2.station_n = c1.station_n)\n" +
                        "         JOIN connection c3 ON (c2.line_No = c3.line_No)\n" +
                        "         JOIN stations st2 ON (c2.station_n = st2.station_name)\n" +
                        "         JOIN stations st3 ON (c3.station_n = st3.station_name)\n" +
                        "         JOIN `lines` l ON (c.line_No = l.id)\n" +
                        "         JOIN `lines` l1 ON (c2.line_No = l1.id)\n" +
                        "WHERE st.station_name = ?\n" +
                        "  AND st3.station_name = ?\n" +
                        "  AND st2.station_name != ?\n" +
                        "  AND st1.station_name != st3.station_name\n" +
                        "  AND st.station_name != st1.station_name\n"+
                        "UNION ALL\n" +
                        "SELECT st.chinese_name                                                        AS '初始站',\n" +
                        "       l.line_name                                                            AS '线路1',\n" +
                        "       ABS(c.station_No - c1.station_No)                                      AS '站数1',\n" +
                        "       st1.chinese_name                                                       AS '换乘站1',\n" +
                        "       l1.line_name                                                           AS '线路2',\n" +
                        "       ABS(c2.station_No - c3.station_No)                                     AS '站数2',\n" +
                        "       st3.chinese_name                                                       AS '换乘站2',\n" +
                        "       l2.line_name                                                           AS '线路3',\n" +
                        "       ABS(c4.station_No - c5.station_No)                                     AS '站数3',\n" +
                        "       st6.chinese_name                                                       AS '目的地',\n" +
                        "       ABS(c.station_No - c1.station_No) + ABS(c2.station_No - c3.station_No) +\n" +
                        "       ABS(c4.station_No - c5.station_No)                                     AS '总站数'\n" +
                        "FROM connection c\n" +
                        "         JOIN connection c1 ON (c.line_No = c1.line_No)\n" +
                        "         JOIN stations st ON (c.station_n = st.station_name)\n" +
                        "         JOIN stations st1 ON (c1.station_n = st1.station_name)\n" +
                        "         JOIN connection c2 ON (c2.line_No != c1.line_No AND c2.station_n = c1.station_n)\n" +
                        "         JOIN connection c3 ON (c2.line_No = c3.line_No)\n" +
                        "         JOIN stations st2 ON (c2.station_n = st2.station_name)\n" +
                        "         JOIN stations st3 ON (c3.station_n = st3.station_name)\n" +
                        "         JOIN connection c4 ON (c4.line_No != c3.line_No AND c4.station_n = c3.station_n)\n" +
                        "         JOIN connection c5 ON (c4.line_No = c5.line_No)\n" +
                        "         JOIN stations st5 ON (c4.station_n = st5.station_name)\n" +
                        "         JOIN stations st6 ON (c5.station_n = st6.station_name)\n" +
                        "         JOIN `lines` l ON (c.line_No = l.id)\n" +
                        "         JOIN `lines` l1 ON (c2.line_No = l1.id)\n" +
                        "         JOIN `lines` l2 ON (c4.line_No = l2.id)\n" +
                        "WHERE st.station_name = ?\n" +
                        "  AND st6.station_name = ?\n" +
                        "  AND st5.station_name != ?\n" +
                        "  AND st1.station_name != st3.station_name\n" +
                        "  AND st.station_name != st1.station_name\n"+
                        "ORDER BY 总站数;");
                stmt.setString(1, start);
                stmt.setString(2, end);
                stmt.setString(3, end);
                stmt.setString(4, start);
                stmt.setString(5, end);
                stmt.setString(6, end);
                ResultSet rs2 = stmt.executeQuery();
                while (rs2.next())
                {
                    String stationName = rs2.getString("初始站");

                    String lineName1 = rs2.getString("线路1");
                    int num1 = rs2.getInt("站数1");
                    String stationName2 = rs2.getString("换乘站1");
                    String lineName2 = rs2.getString("线路2");
                    int num2 = rs2.getInt("站数2");
                    String stationName3 = rs2.getString("换乘站2");
                    String lineName3 = rs2.getString("线路3");
                    int num3 = rs2.getInt("站数3");
                    String sta3 = rs2.getString("目的地");
                    int num = rs2.getInt("总站数");
                    tableModel2.addRow(new Object[]{stationName, lineName1, num1, stationName2, lineName2, num2, stationName3, lineName3,  num3,  sta3, num});
                    r = 4;
                }
                con.commit();
            }
            System.out.println("查线路");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("灭查到");
        }
        closeDB();
        return r;
    }
    public boolean stationExist(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM stations WHERE station_name = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            con.commit();
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check station existence!");
            return false;
        }
    }
}
