package Update;
import Interface.PCnew;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

import static IMTRY.controlDB.*;

//成为乘客，办卡，给卡充值。。。。
public class PasCas implements PCnew {
    public void BeAPassenger(String name, String id, String phone, String gender, String district){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(id.length()!= 18){
            return;
        }
        try {
            stmt = con.prepareStatement("insert into passengers(name, id_number, phone_number, gender, district)"+
                    "values (?, ?, ?, ?, ?);");
            stmt.setString(1, name);
            stmt.setString(2, id);
            stmt.setString(3, phone);
            stmt.setString(4, gender);
            stmt.setString(5, district);
            stmt.executeUpdate();
            con.commit();
            System.out.println("Add successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The passenger already exists!");
            System.out.println("----------------------------------");
        }
        closeDB();
    }
    public String TakeACard(double money){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        LocalDateTime currentTime = LocalDateTime.now();

        try {
            stmt = con.prepareStatement("select code from cards order by code limit 1;");
            ResultSet rs = stmt.executeQuery();
            long code1 = 0;
            if (rs.next()) {
                code1 = rs.getLong("code");
            }
            long code2 = code1 - 1;
            String code = Long.toString(code2);
            stmt = con.prepareStatement("insert into cards(code, money, create_time)"+
                    "values (?, ?, ?);");
            stmt.setString(1, code);
            stmt.setDouble(2, money);
            stmt.setObject(3, currentTime);
            stmt.executeUpdate();
            con.commit();
            System.out.println("Add successfully!");
            closeDB();
            return code;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The passenger already exists!");
            System.out.println("----------------------------------");
        }
        closeDB();
        return null;
    }

    public int recharge(String code, double money){
        Properties prop = loadMySQLDBUser();
        openMySQLDB(prop);
        if(!cardExist(code)){
            closeDB();
            return 1;
        }
        try {
            stmt = con.prepareStatement("select money from cards where code = ?;");
            stmt.setString(1, code);
            con.commit();
            ResultSet rs = stmt.executeQuery();
            double moneyO = 0;
            if (rs.next()) {
                moneyO = rs.getLong("money");
            }
            moneyO = moneyO + money;

            stmt = con.prepareStatement("update cards set money = ? where code = ?;");
            stmt.setDouble(1, moneyO);
            stmt.setString(2, code);
            stmt.executeUpdate();
            con.commit();
            System.out.println("Recharge successfully!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("The card doesn't exist!");
            System.out.println("----------------------------------");
        }
        closeDB();
        return 0;
    }

    public boolean cardExist(String code) {
        try {
            stmt = con.prepareStatement("SELECT * FROM cards WHERE code = ?;");
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            con.commit();
            boolean exists = rs.next(); // 如果查询结果集有下一行，则表示车站存在
            return exists;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println("Failed to check card existence!");
            return false;
        }
    }


}
