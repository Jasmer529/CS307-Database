package ImportBatch;

import IMTRY.Main;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class ImportRide1 {
    private static final int BATCH_SIZE = 1000;
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
            if (con != null) {
                con.setAutoCommit(false);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void setPrepareStatement() {
        try {
            stmt = con.prepareStatement("INSERT INTO rides (user_ride1_id ,start_station,end_station,price,start_time,end_time) " +
                    "VALUES (?,?,?,?,?,?);");
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
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
            try {
                // user,start_station,end_station,price,start_time,end_time
                String user = record.get(0);
                // Check if user has 18 characters
                if (user.length() != 18) {
                    return; // Skip this record
                }
                cnt++;
                String start_station = record.get(1);
                String end_station = record.get(2);
                int price = Integer.parseInt(record.get(3));
                String start_time = record.get(4);
                String end_time = record.get(5);

                stmt.setString(1, user);
                stmt.setString(2, start_station);
                stmt.setString(3, end_station);
                stmt.setInt(4, price);

                // Convert start_time and end_time to java.sql.Timestamp
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date parsedStartTime = dateFormat.parse(start_time);
                java.sql.Timestamp startTimeStamp = new java.sql.Timestamp(parsedStartTime.getTime());
                stmt.setTimestamp(5, startTimeStamp);

                java.util.Date parsedEndTime = dateFormat.parse(end_time);
                java.sql.Timestamp endTimeStamp = new java.sql.Timestamp(parsedEndTime.getTime());
                stmt.setTimestamp(6, endTimeStamp);

                stmt.addBatch();
            } catch (SQLException | ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    static int cnt = 0;
    public static void main(String[] args) {
        Properties prop = loadDBUser();
        List<CSVRecord> records = loadCSVFile();

        openDB(prop);
        setPrepareStatement();
        try {
            for (CSVRecord record : records) {
                if (record.getRecordNumber() == 1)
                    continue; // Skip the header line
                loadData(record);
                stmt.executeBatch();
            }


            // Commit the transaction
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeDB();
        }

        Main.Allcnt += cnt;
        System.out.println("Ride1's "+ cnt + " records successfully loaded");

    }
}

