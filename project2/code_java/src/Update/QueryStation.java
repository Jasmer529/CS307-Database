package Update;

import Interface.Query;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Properties;
import static IMTRY.controlDB.*;
public class QueryStation implements Query {
    public String forwardQuery(String station_name, String line_name, int n, DefaultTableModel tableModel){

        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        int lineNo = getLineNoByName(line_name);
        try {

            stmt = con.prepareStatement("select station_No from connection where line_No = ? and station_n = ?;");
            stmt.setInt(1, lineNo);
            stmt.setString(2, station_name);
            ResultSet rs = stmt.executeQuery();
            String ans = "";
            if (rs.next()) {
                ans = rs.getString("station_No");
            }else {
                ans = "Nothing";
            }

            stmt = con.prepareStatement("select * from connection where (station_No - ?) = ?  and line_No = ?;");
            stmt.setString(1, ans);
            stmt.setInt(2, n);
            //stmt.setString(3, ans);
            stmt.setInt(3, lineNo);

            ResultSet rs2 = stmt.executeQuery();
            if(rs2.next()){
                rs2 = stmt.executeQuery();
                while (rs2.next())
                {
                    String num = rs2.getString("line_No");
                    String sn = rs2.getString("station_No");
                    String sn2 = rs2.getString("station_n");

                    tableModel.addRow(new Object[]{num, sn, sn2});
                }
                con.commit();
            }

            return ans;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to get lineNo by name.");
        }finally {
            closeDB();
        }
        return "Nothing";
    }

    public String backwardQuery(String station_name, String line_name, int n, DefaultTableModel tableModel){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        int lineNo = getLineNoByName(line_name);
        try {
            stmt = con.prepareStatement("select station_No from connection where line_No = ? and station_n = ?;");
            stmt.setInt(1, lineNo);
            stmt.setString(2, station_name);
            ResultSet rs = stmt.executeQuery();
            String ans = "";
            if (rs.next()) {
                ans = rs.getString("station_No");
            }else {
                ans = "Nothing";
            }

            int fun = -n;

            //select count(station_n) as count from connection where line_No = 1 group by line_No;
            stmt = con.prepareStatement("select * from connection where (station_No - ?) = ?  and line_No = ?;");
            stmt.setString(1, ans);
            stmt.setInt(2, fun);

            stmt.setInt(3, lineNo);

            ResultSet rs2 = stmt.executeQuery();
            if(rs2.next()){
                rs2 = stmt.executeQuery();
                while (rs2.next())
                {
                    String num = rs2.getString("line_No");
                    String sn = rs2.getString("station_No");
                    String sn2 = rs2.getString("station_n");
                    tableModel.addRow(new Object[]{num, sn, sn2});
                }
                con.commit();
            }
            return "n";
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to get lineNo by name.");
        }
        return "Nothing";
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
}
