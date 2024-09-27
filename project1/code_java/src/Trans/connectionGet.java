package Trans;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import preData.changeString;

public class connectionGet{

    public static void main(String[] args) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\lines.json");
             FileWriter writer = new FileWriter("connection.csv")) {
 // 解析 JSON 文件
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // 写入 CSV 文件头部
            writer.append("line_No, line_n, stationNo, station_n\n");

            // 遍历每条地铁线路
            int lineNo = 1;
            for (String lineName : jsonObject.keySet()) {
                JsonObject lineObject = jsonObject.getAsJsonObject(lineName);
                JsonArray stations = lineObject.getAsJsonArray("stations");

                // 写入每个站点的数据
                int stationNo = 1;
                for (int i = 0; i < stations.size(); i++) {
                    String stationName = stations.get(i).getAsString();
                    stationName = changeString.changestn(stationName);
                    writer.append(lineNo + "," + lineName + "," + stationNo + "," + stationName + "\n");
                    stationNo++;
                }

                lineNo++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
