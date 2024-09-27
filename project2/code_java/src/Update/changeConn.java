package Update;

import Interface.cConn;

import java.sql.*;
import java.util.Properties;

import static IMTRY.controlDB.*;

public class changeConn implements cConn {

    public int putStationOnLine(String lineName, String stationName, int stationNo) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if (!stationExist(stationName)) {
            System.out.println("The station doesn't exists.");
            return 1;
        }
        if(stationExistInLine(stationName, lineName)){
            System.out.println("The station is already in line");
            return 2;
        }

        try {
            // 根据 lineName 查询对应的 lineNo
            int lineNo = getLineNoByName(lineName);

            // 更新指定位置之后的站点的 station_No
            stmt = con.prepareStatement("UPDATE connection SET station_No = station_No + 1 WHERE line_No = ? AND station_No >= ?;");
            stmt.setInt(1, lineNo);
            stmt.setInt(2, stationNo);
            stmt.executeUpdate();
            con.commit();

            stmt = con.prepareStatement("INSERT INTO connection (line_No, station_No, station_n) VALUES (?, ?, ?);");
            stmt.setInt(1, lineNo);
            stmt.setInt(2, stationNo);
            stmt.setString(3, stationName);
            stmt.executeUpdate();
            con.commit();
            System.out.println("New station inserted successfully!");
            return 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to insert new station.");
            return 3;
        } finally {
            closeDB();
        }
    }

    public int getLineNoByName(String lineName){
        int lineNo = -1;
        try {
            stmt = con.prepareStatement("SELECT id FROM `lines` WHERE line_name = ?");
            stmt.setString(1, lineName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                lineNo = rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to get lineNo by name.");
        }
        return lineNo;
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


    public boolean stationExistInLine(String name, String lineName) {
        try {

            int lineNo = getLineNoByName(lineName);
            stmt = con.prepareStatement("select * from connection where station_n = ? and line_No = ?;");
            stmt.setString(1, name);
            stmt.setInt(2, lineNo);

            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check station existence!");
            return false;
        }
    }

    public boolean LineExist(String name) {
        try {
            stmt = con.prepareStatement("SELECT * FROM `lines` WHERE line_name = ?;");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示line 存在
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check line existence!");
            return false;
        }
    }

    public int deleteStationFromLine(String stationName, String l_name){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);

        if (!stationExist(stationName)) {
            System.out.println("The station doesn't exists.");
            return 1;
        }
        if (!LineExist(l_name)) {
            System.out.println("The line doesn't exists.");
            return 2;
        }
        if(!stationExistInLine(stationName, l_name)){
            System.out.println("The station is not in the line");
            return 3;
        }
        try {
            int lineNo = getLineNoByName(l_name);
            stmt = con.prepareStatement("SELECT * FROM connection WHERE station_n = ? and line_no = ?;");
            stmt.setString(1, stationName);
            stmt.setInt(2, lineNo);
            ResultSet rs = stmt.executeQuery();
            int current = 0;
            if (rs.next()) {
                current = rs.getInt("station_No");
            }
            // 更新指定位置之后的站点的 station_No
            stmt = con.prepareStatement("UPDATE connection SET station_No = station_No - 1 WHERE line_No = ? AND station_No > ?;");
            stmt.setInt(1, lineNo);
            stmt.setInt(2, current);
            stmt.executeUpdate();
            con.commit();

            stmt = con.prepareStatement("delete from connection where line_No = ? and station_n = ?;");
            stmt.setInt(1, lineNo);
            stmt.setString(2, stationName);
            stmt.executeUpdate();
            con.commit();
            System.out.println("delete successfully!");
            return 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to delete.");
            return 4;
        } finally {
            closeDB();
        }

    }
}
