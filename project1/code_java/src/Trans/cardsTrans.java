package Trans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class cardsTrans {

    public static void main(String[] args) throws IOException {
        // 读取 JSON 文件
        JsonArray jsonArray = JsonParser.parseReader(new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\cards.json")).getAsJsonArray();

        // 创建 CSV 文件
        FileWriter fileWriter = new FileWriter("cards.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        // 写入 CSV 文件标题行
        csvPrinter.printRecord("code", "money", "create_time");

        // 遍历 JSON 数组并将数据写入 CSV 文件
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String code = jsonObject.get("code").getAsString();
            String money = jsonObject.get("money").getAsString();
            String create_time = jsonObject.get("create_time").getAsString();
            csvPrinter.printRecord(code, money, create_time);
        }

        // 关闭 CSV 文件
        csvPrinter.flush();
        csvPrinter.close();
        fileWriter.close();
    }
}
