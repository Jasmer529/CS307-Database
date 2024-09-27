package Update;

import Interface.sAMD;

import java.sql.SQLException;
import java.util.Properties;

import static IMTRY.controlDB.*;

public class stationAMD implements sAMD {

    public void add_a_station(String name, String district, String intro, String chinese_name) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);

        try {
            stmt = con.prepareStatement("insert into stations(station_name, district, intro, chinese_name, state)"+
                    "values (?, ?, ?, ?, ?);");
            stmt.setString(1, name);
            stmt.setString(2, district);
            stmt.setString(3, intro);
            stmt.setString(4, chinese_name);
            stmt.setString(5, "建设中");
            stmt.executeUpdate();
            con.commit();
            System.out.println("Add successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The station already exists!");
            System.out.println("----------------------------------");
        }
        closeDB();
    }

    public void modify_a_station(String nameI, String name, String district, String intro, String chinese_name) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        try {
            stmt = con.prepareStatement("update stations set station_name = ?, district = ?, intro = ?, chinese_name = ? where station_name = ?;\n");
            stmt.setString(1, name);
            stmt.setString(2, district);
            stmt.setString(3, intro);
            stmt.setString(4, chinese_name);
            stmt.setString(5, nameI);
            stmt.executeUpdate();
            con.commit();
            System.out.println("Modify successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The station doesn't exists!");
            System.out.println("----------------------------------");
        }

        closeDB(); 
    }

    public void delete_a_station(String name) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        System.out.println("Please input the name of the station you want to delete:");
        try {
            stmt = con.prepareStatement("delete from stations where station_name = ?;");
            stmt.setString(1, name);
            stmt.executeUpdate();
            con.commit();
            System.out.println("delete successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The station doesn't exists!");
            System.out.println("----------------------------------");
        }
        closeDB();
    }
}
