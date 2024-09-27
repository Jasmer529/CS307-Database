package Trans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class passTrans {

    public static void main(String[] args) throws IOException {
        // 读取 JSON 文件
        JsonArray jsonArray = JsonParser.parseReader(new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\passenger.json")).getAsJsonArray();

        // 创建 CSV 文件
        FileWriter fileWriter = new FileWriter("passenger.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        // 写入 CSV 文件标题行
        csvPrinter.printRecord("name", "id_number", "phone_number", "gender", "district");

        // 遍历 JSON 数组并将数据写入 CSV 文件
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String idNumber = jsonObject.get("id_number").getAsString();
            String phoneNumber = jsonObject.get("phone_number").getAsString();
            String gender = jsonObject.get("gender").getAsString();
            String district = jsonObject.get("district").getAsString();
            csvPrinter.printRecord(name, idNumber, phoneNumber, gender, district);
        }

        // 关闭 CSV 文件
        csvPrinter.flush();
        csvPrinter.close();
        fileWriter.close();
    }
}
