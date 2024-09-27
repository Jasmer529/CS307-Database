package Update;

import Interface.cState;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static IMTRY.controlDB.*;
import static IMTRY.controlDB.stmt;

public class changeSta implements cState {
    public int restart(String name){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!stationExist(name)){
            closeDB();
            return 1;
        }
        //update stations set state = '关闭中' where station_name = 'booomi';

        try {
            stmt = con.prepareStatement("update stations set state = '运营中' where station_name = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            con.commit();
            System.out.println("开车！");
            closeDB();
            return 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to insert new station.");
            closeDB();
            return 3;
        }


    }
    public int stop(String name){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!stationExist(name)){
            closeDB();
            return 1;
        }
        try {
            stmt = con.prepareStatement("update stations set state = '关闭中' where station_name = ?");
            stmt.setString(1, name);
            stmt.executeUpdate();
            con.commit();
            System.out.println("紧急停车！");
            closeDB();
            return 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to insert new station.");
            closeDB();
            return 3;
        }

    }
    public String checkState(String name){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!stationExist(name)){
            closeDB();
            return "ex";
        }
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
            closeDB();
            return ans;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to insert new station.");
            closeDB();
            return null;
        }
    }
    public boolean stationExist(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM stations WHERE station_name = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check station existence!");
            return false;
        }
    }
}
