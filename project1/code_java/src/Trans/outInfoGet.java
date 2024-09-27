package Trans;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import preData.changeString;

public class outInfoGet {

    public static void main(String[] args) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\stations.json");
             FileWriter writer = new FileWriter("outInfo.csv")) {
            // 解析 JSON 文件
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            // 写入 CSV 文件头部
            writer.append("id, out_info, outNo ,ttext, station_id\n");
            // 遍历每条线路
            int stationNo = 1;
            int id = 1;
            for (String stationName : jsonObject.keySet()) {
                HashMap<String, String> hashMap = new HashMap<>();
                JsonObject stationObject = jsonObject.getAsJsonObject(stationName);
                JsonArray out_info = stationObject.getAsJsonArray("out_info");
                int outNo = 1;
                for (int i = 0; i < out_info.size(); i++) {
                    JsonObject outIn = out_info.get(i).getAsJsonObject();
                    String outt = outIn.get("outt").getAsString();
                    String textt = outIn.get("textt").getAsString();
                    outt = changeString.changeOut(outt);
                    textt = changeString.changeText(textt);
                    hashMap.put(outt, outt);
                    writer.append(id + "," + outt + "," + outNo + "," + textt + "," + stationNo + "\n");
                    id++;
                    outNo++;
                }
                JsonArray bus_info = stationObject.getAsJsonArray("bus_info");
                // 遍历每个站点的公交信息、
                String last = "";
                for (int i = 0; i < bus_info.size(); i++) {
                    JsonObject busObject = bus_info.get(i).getAsJsonObject();
                    String chukou = busObject.getAsJsonPrimitive("chukou").getAsString();
                    chukou = changeString.changeChuKou(chukou);
                    if(chukou.equals("此站暂无数据") || chukou.equals("") || chukou.contains("删除")){
                        continue;
                    }
                    if (!hashMap.containsKey(chukou) && !last.equals(chukou)) {
                        writer.append(id + "," + chukou + "," + outNo + "," + "公交站" + "," + stationNo + "\n");
                        id++;
                    }
                    last = chukou;
                }
                stationNo++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
