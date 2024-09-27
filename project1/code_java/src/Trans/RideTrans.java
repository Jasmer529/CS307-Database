package Trans;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import preData.changeString;

public class RideTrans {

    public static void main(String[] args) throws IOException {
        // 读取 JSON 文件
        JsonArray jsonArray = JsonParser.parseReader(new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\ride.json")).getAsJsonArray();

        // 创建 CSV 文件
        FileWriter fileWriter = new FileWriter("ride.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        // 写入 CSV 文件标题行
        csvPrinter.printRecord("user", "start_station", "end_station", "price", "start_time", "end_time");

        // 遍历 JSON 数组并将数据写入 CSV 文件
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String user = jsonObject.get("user").getAsString();
            String start_station = jsonObject.get("start_station").getAsString();
            String end_station = jsonObject.get("end_station").getAsString();
            start_station = changeString.changestn(start_station);
            end_station = changeString.changestn(end_station);
            String price = jsonObject.get("price").getAsString();
            String start_time = jsonObject.get("start_time").getAsString();
            String end_time = jsonObject.get("end_time").getAsString();
            csvPrinter.printRecord(user, start_station, end_station, price, start_time, end_time);
        }

        // 关闭 CSV 文件
        csvPrinter.flush();
        csvPrinter.close();
        fileWriter.close();
    }
}
