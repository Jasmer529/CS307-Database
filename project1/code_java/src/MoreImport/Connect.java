package MoreImport;

import IMTRY.Main;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Properties;


public class Connect {
    private static Connection con = null;
    private static PreparedStatement stmt = null;

    private static void openDB(Properties prop) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + prop.getProperty("host") + "/" + prop.getProperty("database");
        try {
            con = DriverManager.getConnection(url, prop);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception ignored) {
            }
        }
    }

    private static Properties loadDBUser() {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(new FileInputStream("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\dbUser.properties")));
            return properties;
        } catch (IOException e) {
            System.err.println("can not find db user file");
            throw new RuntimeException(e);
        }
    }


    public static List<CSVRecord> loadCSVFile() {
        try {
            Reader reader = Files.newBufferedReader(Path.of("ride.csv"));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadData(CSVRecord record) {
        if (con != null) {
            String sql = "INSERT INTO rides (user_ride1_id, start_station, end_station, price, start_time, end_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement pstmt = con.prepareStatement(sql);
                if(record.get(0).length()!=18){
                    return;
                }
                cnt++;
                pstmt.setString(1, record.get(0));
                pstmt.setString(2, record.get(1));
                pstmt.setString(3, record.get(2));
                pstmt.setInt(4, Integer.parseInt(record.get(3)));
                pstmt.setTimestamp(5, Timestamp.valueOf(record.get(4)));
                pstmt.setTimestamp(6, Timestamp.valueOf(record.get(5)));
                pstmt.executeUpdate();
            } catch (SQLException | NumberFormatException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    static int cnt = 0;
    public static void main(String[] args) {
        Properties prop = loadDBUser();
        List<CSVRecord> records = loadCSVFile();
        long start = System.currentTimeMillis();
            openDB(prop);
            for (CSVRecord record : records) {
                if (record.getRecordNumber() == 1)
                    continue; // Skip the header line
                loadData(record);

                if (cnt % 1000 == 0) {
                    System.out.println("insert " + 1000 + " data successfully!");
                }
            }
            closeDB();
            // Commit the transaction

        Main.Allcnt += cnt;
        System.out.println("Ride1's "+ cnt + " records successfully loaded");
        long end = System.currentTimeMillis();

        System.out.println("Connect Loading speed : " + (cnt * 1000L) / (end - start) + " records/s");
    }
}

