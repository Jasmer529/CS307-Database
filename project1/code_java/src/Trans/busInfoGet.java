package Trans;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import preData.changeString;

public class busInfoGet {

    public static void main(String[] args) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\stations.json");
             FileWriter writer = new FileWriter("busInfo.csv")) {

            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            writer.append("busStationId, busStationName, chukou, station_id\n");

            // 遍历每个站点
            int busStationId = 1;
            int station_id = 1;
            for (String stationName : jsonObject.keySet()) {
                JsonObject stationObject = jsonObject.getAsJsonObject(stationName);
                JsonArray bus_info = stationObject.getAsJsonArray("bus_info");
                stationName = changeString.changestn(stationName);
                // 遍历每个站点的公交信息
                for (int i = 0; i < bus_info.size(); i++) {
                    JsonObject busObject = bus_info.get(i).getAsJsonObject();
                    JsonArray bus_out_info = busObject.getAsJsonArray("busOutInfo");
                   // JsonObject chukou = busObject.getAsJsonObject("chukou");
                    String chukou = busObject.getAsJsonPrimitive("chukou").getAsString();
                    chukou = changeString.changeChuKou(chukou);
                    if(chukou.equals("此站暂无数据") || chukou.equals("") || chukou.contains("删除")){
                        continue;
                    }

                    // 遍历每个公交信息的出口信息
                    for (int j = 0; j < bus_out_info.size(); j++) {
                        JsonObject busOut = bus_out_info.get(j).getAsJsonObject();

                        String busName = busOut.get("busName").getAsString();
                        busName = changeString.changeBusName(busName);
                            writer.append(busStationId + ","+ busName  + "," + chukou +","+station_id+"\n");

                        busStationId++;

                    }
                }
                station_id++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
