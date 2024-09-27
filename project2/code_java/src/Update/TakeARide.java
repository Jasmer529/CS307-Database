package Update;

import Interface.TARide;

import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

import static IMTRY.controlDB.*;

//上车前 判断这个人是不是乘客，车站是不是
public class TakeARide implements TARide {
    public int UpSubwayPass(String pass_id, String station_n, int type) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        LocalDateTime currentTime = LocalDateTime.now();
        if(!stationExist(station_n)){
            closeDB();
            return 1;
        }
        if(!passExist(pass_id)){
            closeDB();
            return 2;
        }
        if(!checkState(station_n).equals("运营中")){
            closeDB();
            return 4;
        }
        try {
            if(type == 3){
                stmt = con.prepareStatement("insert into OnSubPass(id_number, start_station, start_time, type)" +
                        "values (?, ?, ?, ?);");
                stmt.setString(1, pass_id);
                stmt.setString(2, station_n);
                stmt.setObject(3, currentTime);
                stmt.setObject(4, "common");
                stmt.executeUpdate();
                con.commit();
                System.out.println("人上普通车！");
            }else if(type == 7){
                stmt = con.prepareStatement("insert into OnSubPass(id_number, start_station, start_time, type)" +
                        "values (?, ?, ?, ?);");
                stmt.setString(1, pass_id);
                stmt.setString(2, station_n);
                stmt.setObject(3, currentTime);
                stmt.setObject(4, "business");
                stmt.executeUpdate();
                con.commit();
                System.out.println("人上商务车！");
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 65;
        }
        closeDB();
        return 0;
    }

    public int UpSubwayCard(String card_id, String station_n, int type) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        LocalDateTime currentTime = LocalDateTime.now();
        if(!stationExist(station_n)){
            closeDB();
            return 1;
        }
        if(!cardExist(card_id)){
            closeDB();
            return 3;
        }
        if(!checkState(station_n).equals("运营中")){
            closeDB();
            return 4;
        }
        try {
            if(type == 3){
                stmt = con.prepareStatement("insert into OnSubCard(card_number, start_station, start_time, type)" +
                        "values (?, ?, ?, ?);");
                stmt.setString(1, card_id);
                stmt.setString(2, station_n);
                stmt.setObject(3, currentTime);
                stmt.setObject(4, "common");
                stmt.executeUpdate();
                con.commit();
                System.out.println("卡上普通车！");
            }else if(type == 7){
                stmt = con.prepareStatement("insert into OnSubCard(card_number, start_station, start_time, type)" +
                        "values (?, ?, ?, ?);");
                stmt.setString(1, card_id);
                stmt.setString(2, station_n);
                stmt.setObject(3, currentTime);
                stmt.setObject(4, "business");
                stmt.executeUpdate();
                con.commit();
                System.out.println("卡上商务车！");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return 65;
        }
        closeDB();
        return 0;
    }

    public int DownSubwayPass(String pass_id, String station_n){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);

        // 检查车站是否存在
        if(!stationExist(station_n)){
            closeDB();
            return 1;
        }

        // 检查乘客是否存在
        if(!passExist(pass_id)){
            closeDB();
            return 2;
        }

        // 检查乘客是否在地铁上
        if(!passExistinSub(pass_id)){
            closeDB();
            return 3;
        }

        if(!checkState(station_n).equals("运营中")){
            closeDB();
            return 4;
        }

        try {
            // 获取乘客的上车信息
            stmt = con.prepareStatement("select start_station, start_time, type from OnSubPass where id_number = ?;");
            stmt.setString(1, pass_id);
            ResultSet rs = stmt.executeQuery();

            String startStation = "";
            String startTime = "";
            String type = "";
            while (rs.next()) {
                startStation = rs.getString("start_station");
                startTime = rs.getString("start_time");
                type = rs.getString("type");
            }
            rs.close();

            // 删除乘客的上车信息
            stmt = con.prepareStatement("delete from OnSubPass where id_number = ?;");
            stmt.setString(1, pass_id);
            stmt.executeUpdate();
            con.commit();

            // 查询乘车费用
            int price = 0;
            if(type.equals("common")){
                stmt = con.prepareStatement("select * from price where start_station in (select chinese_name from stations where station_name = ?) and\n" +
                        "                          end_station in (select chinese_name from stations where station_name = ?);");
                stmt.setString(1, startStation);
                stmt.setString(2, station_n);
                ResultSet rs2 = stmt.executeQuery();
                con.commit();
                if(rs2.next()){
                    price = rs2.getInt("price");
                }
                rs2.close();
            }else {
                stmt = con.prepareStatement("select * from priceB where start_station in (select chinese_name from stations where station_name = ?) and\n" +
                        "                          end_station in (select chinese_name from stations where station_name = ?);");
                stmt.setString(1, startStation);
                stmt.setString(2, station_n);
                ResultSet rs2 = stmt.executeQuery();
                con.commit();
                if(rs2.next()){
                    price = rs2.getInt("priceB");
                }
                rs2.close();
            }

            // 记录下车信息
            LocalDateTime currentTime = LocalDateTime.now();
            stmt = con.prepareStatement("insert into rides(user_ride1_id, start_station, end_station, price, start_time, end_time)" +
                    "values (?, ?, ?, ?, ?, ?);");
            stmt.setString(1, pass_id);
            stmt.setString(2, startStation);
            stmt.setString(3, station_n);
            stmt.setInt(4, price);
            stmt.setObject(5, startTime);
            stmt.setObject(6, currentTime);
            stmt.executeUpdate();
            con.commit();

            System.out.println("下车");
            return price;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("出现异常");
        } finally {
            closeDB();
        }
        return 0;
    }

    public int DownSubwayCard(String card_id, String station_n){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!stationExist(station_n)){
            closeDB();
            return 1;
        }
        if(!cardExist(card_id)){
            closeDB();
            return 2;
        }
        if(!cardExistinSub(card_id)){
            closeDB();
            return 3;
        }

        if(!checkState(station_n).equals("运营中")){
            closeDB();
            return 4;
        }

        try {
            stmt = con.prepareStatement("select start_station, start_time, type from OnSubCard where card_number = ?;");
            stmt.setString(1, card_id);
            ResultSet rs = stmt.executeQuery();

            String startStation = "";
            String startTime = "";
            String type = "";
            while (rs.next()) {
                startStation = rs.getString("start_station");
                startTime = rs.getString("start_time");
                type = rs.getString("type");
            }
            rs.close();

            int price = 0;
            if(type.equals("common")){
                stmt = con.prepareStatement("select * from price where start_station in (select chinese_name from stations where station_name = ?) and\n" +
                        "                          end_station in (select chinese_name from stations where station_name = ?);");
                stmt.setString(1, startStation);
                stmt.setString(2, station_n);
                ResultSet rs2 = stmt.executeQuery();
                con.commit();
                if(rs2.next()){
                    price = rs2.getInt("price");
                }
                rs2.close();
            }else {
                stmt = con.prepareStatement("select * from priceB where start_station in (select chinese_name from stations where station_name = ?) and\n" +
                        "                          end_station in (select chinese_name from stations where station_name = ?);");
                stmt.setString(1, startStation);
                stmt.setString(2, station_n);
                ResultSet rs2 = stmt.executeQuery();
                con.commit();
                if(rs2.next()){
                    price = rs2.getInt("priceB");
                }
                rs2.close();
            }

            //看一下钱够不够
            stmt = con.prepareStatement("select money from cards where code = ?;");
            stmt.setString(1, card_id);
            ResultSet rs3 = stmt.executeQuery();
            con.commit();
            if(rs3.next()){
                if(rs3.getInt("money") < price){
                    closeDB();
                    return 9;
                }
            }
            System.out.println("扣钱");


            // 删除乘客的上车信息
            stmt = con.prepareStatement("delete from OnSubCard where card_number = ?;");
            stmt.setString(1, card_id);
            stmt.executeUpdate();
            con.commit();


            //update cards set money = money - 6 where code = '881000488';
            stmt = con.prepareStatement("update cards set money = money - ? where code = ?;");
            stmt.setInt(1, price);
            stmt.setString(2, card_id);
            stmt.executeUpdate();
            System.out.println("扣钱");
            con.commit();

            // 记录下车信息
            LocalDateTime currentTime = LocalDateTime.now();
            stmt = con.prepareStatement("insert into rides2(user_ride2_id, start_station, end_station, price, start_time, end_time)" +
                    "values (?, ?, ?, ?, ?, ?);");
            stmt.setString(1, card_id);
            stmt.setString(2, startStation);
            stmt.setString(3, station_n);
            stmt.setInt(4, price);
            stmt.setObject(5, startTime);
            stmt.setObject(6, currentTime);
            stmt.executeUpdate();
            con.commit();
            System.out.println("下车");
            return price;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("灭查到");
            System.out.println("----------------------------------");
        }
        closeDB();
        return 0;

    }

    public void currentPass(DefaultTableModel tableModel){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        try {
            stmt = con.prepareStatement("select * from OnSubPass;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                String id = rs.getString("id_number");
                String startStation = rs.getString("start_station");
                String startTime = rs.getString("start_time");
                String type = rs.getString("type");
                tableModel.addRow(new Object[]{id, startStation, startTime, type});
            }
            con.commit();
            System.out.println("查一下车上的人");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("灭查到");
            System.out.println("----------------------------------");
        }
        closeDB();
    }
    public void currentCard(DefaultTableModel tableModel){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        try {
            stmt = con.prepareStatement("select * from OnSubCard;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                String id = rs.getString("card_number");
                String startStation = rs.getString("start_station");
                String startTime = rs.getString("start_time");
                String type = rs.getString("type");
                tableModel.addRow(new Object[]{id, startStation, startTime, type});
            }
            con.commit();
            System.out.println("查一下车上的卡");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("灭查到");
            System.out.println("----------------------------------");
        }
        closeDB();
    }
    public  boolean passExistinSub(String name){
        try {
            stmt = con.prepareStatement("SELECT * FROM onSubPass WHERE id_number = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            con.commit();
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check pass existence!");
            return false;
        }
    }
    public  boolean cardExistinSub(String name){
        try {
            stmt = con.prepareStatement("SELECT * FROM onSubCard WHERE card_number = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            con.commit();
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check card existence!");
            return false;
        }
    }
    public boolean stationExist(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM stations WHERE station_name = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            con.commit();
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check station existence!");
            return false;
        }
    }
    public boolean passExist(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM passengers WHERE id_number = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            con.commit();
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check station existence!");
            return false;
        }
    }
    public boolean cardExist(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM cards WHERE code = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            con.commit();
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check station existence!");
            return false;
        }
    }

    public String checkState(String name){
        try {
            stmt = con.prepareStatement("select state from stations where station_name = ?;");
            stmt.setString(1, name);
            con.commit();
            ResultSet rs = stmt.executeQuery();
            String ans = "";
            if(rs.next()){
                ans = rs.getString("state");
            }
            System.out.println("查查状态！");
            return ans;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to insert new station.");
            return null;
        }
    }
}
