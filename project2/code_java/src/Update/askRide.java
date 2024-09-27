package Update;

import Interface.AskR;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static IMTRY.controlDB.*;

public class askRide implements AskR {
    public int askRide1(String name, String staN, String time1, String time2, DefaultTableModel tableModel){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!staN.equals("") && !stationExist(staN)){
            closeDB();
            return 1;
        }
        int r = 0;
        try {
            if(name.equals("") && !staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides\n" +
                        "WHERE (start_station = ? or end_station = ?) and\n" +
                        "      (start_time >= ? and end_time <= ?);");
                stmt.setString(1, staN);
                stmt.setString(2, staN);
                stmt.setString(3, time1);
                stmt.setString(4, time2);
            }else if(name.equals("")  && staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides\n" +
                        "WHERE  (start_time >= ? and end_time <= ?);");
                stmt.setString(1, time1);
                stmt.setString(2, time2);
            }else if(name.equals("")  && !staN.equals("") && time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides\n" +
                        "WHERE (start_station = ? or end_station = ?);");
                stmt.setString(1, staN);
                stmt.setString(2, staN);
            }else if(!name.equals("") && staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides\n" +
                        "WHERE user_ride1_id IN (SELECT id_number FROM passengers WHERE name = ?)\n" +
                        "  and (start_time >= ? and end_time <= ?);");
                stmt.setString(1, name);
                stmt.setString(2, time1);
                stmt.setString(3, time2);
            }else if(!name.equals("")  && !staN.equals("") && time1.equals("")){
                stmt = con.prepareStatement("SELECT *\n" +
                        "FROM rides\n" +
                        "WHERE user_ride1_id IN (SELECT id_number FROM passengers WHERE name = ?)\n" +
                        "  and (start_station = ? or end_station = ?);");
                stmt.setString(1, name);
                stmt.setString(2, staN);
                stmt.setString(3, staN);
            }else if(!name.equals("")  && staN.equals("") && time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides " +
                        "WHERE user_ride1_id IN (SELECT id_number FROM passengers WHERE name = ?);");
                stmt.setString(1, name);
            }else if(!name.equals("")  && !staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides\n" +
                        "WHERE user_ride1_id IN (SELECT id_number FROM passengers WHERE name = ?)\n" +
                        "  and (start_station = ? or end_station = ?) and \n" +
                        "      (start_time >= ? and end_time <= ?);");
                stmt.setString(1, name);
                stmt.setString(2, staN);
                stmt.setString(3, staN);
                stmt.setString(4, time1);
                stmt.setString(5, time2);
            }else {
                return 11;//全为null
            }
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                rs = stmt.executeQuery();
                while (rs.next())
                {
                    int id = rs.getInt("id");
                    String id1 = rs.getString("user_ride1_id");
                    String stationName1 = rs.getString("start_station");
                    String stationName2 = rs.getString("end_station");
                    int price = rs.getInt("price");
                    String start = rs.getString("start_time");
                    String end = rs.getString("end_time");
                    tableModel.addRow(new Object[]{id, id1, stationName1, stationName2, price, start, end});
                    r = 3;//有 显示表格
                }
                con.commit();
            }else {
                r = 2;//没有记录
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check rides1 existence!");
        }
        return r;
    }
    public int askRide2(String ci, String staN, String time1, String time2, DefaultTableModel tableModel){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!staN.equals("") && !stationExist(staN)){
            closeDB();
            return 1;
        }
        int r = 0;
        try {
            if(ci.equals("") && !staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides2\n" +
                        "WHERE (start_station = ? or end_station = ?) and\n" +
                        "      (start_time >= ? and end_time <= ?);");
                stmt.setString(1, staN);
                stmt.setString(2, staN);
                stmt.setString(3, time1);
                stmt.setString(4, time2);
            }else if(ci.equals("") && staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides2\n" +
                        "WHERE  (start_time >= ? and end_time <= ?);");
                stmt.setString(1, time1);
                stmt.setString(2, time2);
            }else if(ci.equals("") && !staN.equals("") && time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides2\n" +
                        "WHERE (start_station = ? or end_station = ?);");
                stmt.setString(1, staN);
                stmt.setString(2, staN);
            }else if(!ci.equals("") && staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides2\n" +
                        "WHERE user_ride2_id = ?\n" +
                        "  and (start_time >= ? and end_time <= ?);");
                stmt.setString(1, ci);
                stmt.setString(2, time1);
                stmt.setString(3, time2);
            }else if(!ci.equals("") && !staN.equals("") && time1.equals("")){
                stmt = con.prepareStatement("SELECT *\n" +
                        "FROM rides2\n" +
                        "WHERE user_ride2_id = ?\n" +
                        "  and (start_station = ? or end_station = ?);");
                stmt.setString(1, ci);
                stmt.setString(2, staN);
                stmt.setString(3, staN);
            }else if(!ci.equals("") && staN.equals("") && time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides2 " +
                        "WHERE user_ride2_id = ?;");
                stmt.setString(1, ci);
            }else if(!ci.equals("") && !staN.equals("") && !time1.equals("")){
                stmt = con.prepareStatement("SELECT * FROM rides2\n" +
                                "WHERE user_ride2_id = ?\n"+
                        "  and (start_station = ? or end_station = ?) and \n" +
                        "      (start_time >= ? and end_time <= ?);");
                stmt.setString(1, ci);
                stmt.setString(2, staN);
                stmt.setString(3, staN);
                stmt.setString(4, time1);
                stmt.setString(5, time2);
            }else {
                return 11;//全为null
            }
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                rs = stmt.executeQuery();
                while (rs.next())
                {
                    int id = rs.getInt("id");
                    String id2 = rs.getString("user_ride2_id");
                    String stationName1 = rs.getString("start_station");
                    String stationName2 = rs.getString("end_station");
                    int price = rs.getInt("price");
                    String start = rs.getString("start_time");
                    String end = rs.getString("end_time");
                    tableModel.addRow(new Object[]{id, id2, stationName1, stationName2, price, start, end});
                    r = 3;//有 显示表格
                }
                con.commit();
            }else {
                r = 2;//没有记录
            }
            con.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check rides2 existence!");
        }
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
