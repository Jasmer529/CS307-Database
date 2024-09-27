
package ImportBatch;


import IMTRY.Main;
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


//line_No, line_n, stationNo, station_n

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class ImportConnection {
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
            stmt = con.prepareStatement("INSERT INTO connection (line_No, station_No, station_n) " +
                    "VALUES (?,?,?);");
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
            Reader reader = Files.newBufferedReader(Path.of("connection.csv"));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadData(CSVRecord record) {
        if (con != null) {
            try {
                //line_No, line_n, station_No, station_n
                int line_No = Integer.parseInt(record.get(0));
                int station_No = Integer.parseInt(record.get(2));
                String station_n = record.get(3);

                stmt.setInt(1, line_No);
                stmt.setInt(2, station_No);
                stmt.setString(3, station_n);

                stmt.addBatch();
            } catch (SQLException | NumberFormatException ex) {
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

            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeDB();
        }
        Main.Allcnt += cnt;
        System.out.println("Connection's "+ cnt + " records successfully loaded");

    }
}

