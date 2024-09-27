package Update;

import Interface.lAMD;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Properties;
import static IMTRY.controlDB.*;

public class lineAMD implements lAMD {

    public void add_a_line(String line_name,String start_time,String end_time,String intro, String mileage, String color, String first_opening, String url) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);

        try {
            stmt = con.prepareStatement("insert into `lines`(line_name, start_time, end_time, intro, mileage, color, first_opening, url)"+
                    "values (?, ?, ?, ?, ?, ?, ?, ?);");

            stmt.setString(1, line_name);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTime = LocalTime.parse(start_time, formatter);
            Time startTimeSql = Time.valueOf(startTime);

            stmt.setTime(2, startTimeSql);
            LocalTime endTime = LocalTime.parse(end_time, formatter);
            Time endTimeSql = Time.valueOf(endTime);
            stmt.setTime(3, endTimeSql);
            stmt.setString(4, intro);
            BigDecimal mileage1 = new BigDecimal(mileage);
            stmt.setBigDecimal(5, mileage1);
            stmt.setString(6, color);
            // Use DateTimeFormatterBuilder to specify date format with optional zero padding
            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-M-d")
                    .optionalStart()
                    .appendPattern("yyyy-MM-dd")
                    .optionalEnd();

            DateTimeFormatter dateFormatter = builder.toFormatter();
            LocalDate first_opening1 = LocalDate.parse(first_opening, dateFormatter);
            Date firstOpeningSql = Date.valueOf(first_opening1);
            stmt.setDate(7, firstOpeningSql);

            stmt.setString(8, url);
            stmt.executeUpdate();
            con.commit();
            System.out.println("Add successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The line already exists!");
            System.out.println("----------------------------------");
        }
        closeDB();
    }

    public void modify_a_line(String nameI, String line_name,String start_time,String end_time,String intro, String mileage, String color, String first_opening, String url) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        try {
            stmt = con.prepareStatement("update `lines` set line_name = ?, start_time = ?, end_time = ?, intro = ?, mileage = ?, color = ?, first_opening = ?, url = ? where line_name = ?;\n");
            stmt.setString(1, line_name);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime startTime = LocalTime.parse(start_time, formatter);
            Time startTimeSql = Time.valueOf(startTime);

            stmt.setTime(2, startTimeSql);
            LocalTime endTime = LocalTime.parse(end_time, formatter);
            Time endTimeSql = Time.valueOf(endTime);
            stmt.setTime(3, endTimeSql);
            stmt.setString(4, intro);
            BigDecimal mileage1 = new BigDecimal(mileage);
            stmt.setBigDecimal(5, mileage1);
            stmt.setString(6, color);
            // Use DateTimeFormatterBuilder to specify date format with optional zero padding
            DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-M-d")
                    .optionalStart()
                    .appendPattern("yyyy-MM-dd")
                    .optionalEnd();

            DateTimeFormatter dateFormatter = builder.toFormatter();
            LocalDate first_opening1 = LocalDate.parse(first_opening, dateFormatter);
            Date firstOpeningSql = Date.valueOf(first_opening1);
            stmt.setDate(7, firstOpeningSql);

            stmt.setString(8, url);
            stmt.setString(9, nameI);
            stmt.executeUpdate();
            con.commit();
            System.out.println("Modify successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The line doesn't exists!");
            System.out.println("----------------------------------");
        }

        closeDB();
    }

    public void delete_a_line(String name) {
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        System.out.println("Please input the name of the line you want to delete:");
        try {
            stmt = con.prepareStatement("delete from `lines` where line_name = ?;");
            stmt.setString(1, name);
            stmt.executeUpdate();
            con.commit();
            System.out.println("delete successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The line doesn't exists!");
            System.out.println("----------------------------------");
        }
        closeDB();
    }
}
