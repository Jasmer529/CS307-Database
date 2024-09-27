package Trans;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.csv.CSVFormat;

import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import preData.changeString;

public class linesTrans {

    public static void main(String[] args) throws IOException {
        // 读取 JSON 文件
        JsonObject jsonObject = JsonParser.parseReader(new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\lines.json")).getAsJsonObject();

        // 创建 CSV 文件
        FileWriter fileWriter = new FileWriter("lines.csv");
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

        // 写入 CSV 文件标题行
        csvPrinter.printRecord("line_name", "stations", "start_time", "end_time", "intro", "mileage", "color", "first_opening", "url");

        // 遍历 JSON 对象并将数据写入 CSV 文件
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            JsonObject lineObject = entry.getValue().getAsJsonObject();
            String lineName = entry.getKey();
            JsonElement stationsElement = lineObject.get("stations");

            String stations = stationsElement.isJsonArray() ? jsonArrayToString(stationsElement.getAsJsonArray()) : "";
            stations = changeString.changestn(stations);
            String startTime = lineObject.get("start_time").getAsString();
            String endTime = lineObject.get("end_time").getAsString();
            String intro = lineObject.get("intro").getAsString();
            String mileage = lineObject.get("mileage").getAsString();
            String color = lineObject.get("color").getAsString();
            String firstOpening = lineObject.get("first_opening").getAsString();
            String url = lineObject.get("url").getAsString();

            // 写入 CSV 记录
            csvPrinter.printRecord(lineName, stations, startTime, endTime, intro, mileage, color, firstOpening, url);
        }

        // 关闭 CSV 文件
        csvPrinter.flush();
        csvPrinter.close();
        fileWriter.close();
    }

    // 将 JsonArray 转换为逗号分隔的字符串
    private static String jsonArrayToString(JsonArray jsonArray) {
        List<String> stationsList = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            stationsList.add(element.getAsString());
        }
        return String.join(",", stationsList);
    }
}
