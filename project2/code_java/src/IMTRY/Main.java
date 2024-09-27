package IMTRY;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import ImportBatch.ImportBus;
import ImportBatch.ImportConnB;
import ImportBatch.ImportConnection;
import ImportBatch.ImportOutInfo;
import ImportBatch.ImportBusInfo;
import ImportBatch.ImportCard;
import ImportBatch.ImportLines;
import ImportBatch.ImportRide1;
import ImportBatch.ImportRide2;
import ImportBatch.ImportStations;
import ImportBatch.ImportPassenger;

public class Main {
    public static int Allcnt = 0;
    public static void main(String[] args) {


        long start = System.currentTimeMillis();
        ImportCard.main(new String[0]);
        ImportPassenger.main(new String[0]);
        ImportStations.main(new String[0]);
        ImportRide1.main(new String[0]);
        ImportRide2.main(new String[0]);
        ImportLines.main(new String[0]);
        ImportConnection.main(new String[0]);

        ImportOutInfo.main(new String[0]);
        ImportBusInfo.main(new String[0]);
        ImportBus.main(new String[0]);
        ImportConnB.main(new String[0]);
        long end = System.currentTimeMillis();
        System.out.println(Allcnt + " records successfully loaded");
        System.out.println("Loading speed: " + (Allcnt * 1000L) / (end - start) + " records/s");
    }

}
