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

public class ImportCard {
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
            stmt = con.prepareStatement("INSERT INTO cards (code, money, create_time) " +
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
            Reader reader = Files.newBufferedReader(Path.of("cards.csv"));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            return csvParser.getRecords();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadData(CSVRecord record) {
        if (con != null) {
            try {
                String code = record.get(0);
                double money = Double.parseDouble(record.get(1));
                String createTime = record.get(2);

                stmt.setString(1, code);
                stmt.setDouble(2, money);
                // Assuming createTime is in the format of 'yyyy-MM-dd HH:mm:ss'
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date parsedDate = dateFormat.parse(createTime);
                java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                stmt.setTimestamp(3, timestamp);

                stmt.addBatch();
            } catch (SQLException | ParseException ex) {
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
        System.out.println("Card's "+ cnt + " records successfully loaded");

    }
}


