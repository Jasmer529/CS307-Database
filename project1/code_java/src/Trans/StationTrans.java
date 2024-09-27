package Trans;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import preData.changeString;

public class StationTrans {

    public static void main(String[] args) throws IOException {
        // 读取 JSON 文件
        JsonObject jsonObject = JsonParser.parseReader(new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\stations.json")).getAsJsonObject();

        // 创建 CSV 文件
        FileWriter fileWriter = new FileWriter("stations.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        // 写入 CSV 文件标题行
        csvPrinter.printRecord("station_name", "district","intro", "chinese_name");

        // 遍历 JSON 对象并将数据写入 CSV 文件
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            JsonObject stationObject = entry.getValue().getAsJsonObject();
            String stationName = entry.getKey();
            stationName = changeString.changestn(stationName);

            String district = stationObject.get("district").getAsString();
            String intro = stationObject.get("intro").getAsString();
            String chineseName = stationObject.get("chinese_name").getAsString();

            // 写入 CSV 记录
            csvPrinter.printRecord(stationName, district, intro, chineseName);
        }

        // 关闭 CSV 文件
        csvPrinter.flush();
        csvPrinter.close();
        fileWriter.close();
    }

}
