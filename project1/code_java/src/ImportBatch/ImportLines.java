package ImportBatch;

import IMTRY.Main;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Properties;
import java.sql.*;

public class ImportLines {
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
            stmt = con.prepareStatement("INSERT INTO lines (line_name,start_time,end_time,intro,mileage,color,first_opening,url) " +
                    "VALUES (?,?,?,?,?,?,?,?);");
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
            Reader reader = Files.newBufferedReader(Path.of("lines.csv"));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadData(CSVRecord record) {
        if (con != null) {
            try {
                //line_name,stations,start_time,end_time,intro,mileage,color,first_opening,url
                String line_name = record.get(0);
                String start_time = record.get(2);
                String end_time = record.get(3);
                String intro = record.get(4);
                String mileageStr = record.get(5); // Get mileage as string
                String color = record.get(6);
                String first_openingStr = record.get(7); // Get first_opening as string
                String url = record.get(8);

                stmt.setString(1, line_name);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime startTime = LocalTime.parse(start_time, formatter);
                Time startTimeSql = Time.valueOf(startTime);

                stmt.setTime(2, startTimeSql);

                LocalTime endTime = LocalTime.parse(end_time, formatter);
                Time endTimeSql = Time.valueOf(endTime);

                stmt.setTime(3, endTimeSql);
                stmt.setString(4, intro);

                // Convert mileage string to BigDecimal
                BigDecimal mileage = new BigDecimal(mileageStr);
                stmt.setBigDecimal(5, mileage);

                stmt.setString(6, color);

                // Use DateTimeFormatterBuilder to specify date format with optional zero padding
                DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-M-d")
                        .optionalStart()
                        .appendPattern("yyyy-MM-dd")
                        .optionalEnd();

                DateTimeFormatter dateFormatter = builder.toFormatter();
                LocalDate first_opening = LocalDate.parse(first_openingStr, dateFormatter);
                Date firstOpeningSql = Date.valueOf(first_opening);
                stmt.setDate(7, firstOpeningSql);

                stmt.setString(8, url);

                stmt.addBatch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = loadDBUser();
        List<CSVRecord> records = loadCSVFile();

        int cnt = 0;
        openDB(prop);
        setPrepareStatement();
        try {
            for (CSVRecord record : records) {
                if (record.getRecordNumber() == 1)
                    continue; // Skip the header line
                loadData(record);
                stmt.executeBatch();
                cnt++;
            }
            // Commit the transaction
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeDB();
        }
        Main.Allcnt += cnt;
        System.out.println("Line's "+ cnt + " records successfully loaded");

    }
}

