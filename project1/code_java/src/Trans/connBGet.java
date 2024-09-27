package Trans;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preData.changeString;

public class connBGet {

    public static void main(String[] args) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\stations.json");
             FileWriter writer = new FileWriter("connB.csv")) {

            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            writer.append("bus_id, busStationId\n");

            // 遍历每个站点
            int busStationId = 1;
            int bus_id = 1;
            HashMap<String, Integer> bus = new HashMap<>();
            HashSet<String> dapeng = new HashSet<>();
            HashSet<String> gaokuai = new HashSet<>();
            HashSet<String> gaofeng = new HashSet<>();
            for (String stationName : jsonObject.keySet()) {
                JsonObject stationObject = jsonObject.getAsJsonObject(stationName);
                JsonArray bus_info = stationObject.getAsJsonArray("bus_info");
                stationName = changeString.changestn(stationName);
                // 遍历每个站点的公交信息
                for (int i = 0; i < bus_info.size(); i++) {
                    JsonObject busObject = bus_info.get(i).getAsJsonObject();
                    JsonArray bus_out_info = busObject.getAsJsonArray("busOutInfo");
                    String chukou = busObject.getAsJsonPrimitive("chukou").getAsString();
                    chukou = changeString.changeChuKou(chukou);
                    if(chukou.equals("此站暂无数据") || chukou.equals("") || chukou.contains("删除")){
                        continue;
                    }
                    for (int j = 0; j < bus_out_info.size(); j++) {
                        JsonObject busOut = bus_out_info.get(j).getAsJsonObject();

                        String busName = busOut.get("busName").getAsString();
                        String busInfo = busOut.get("busInfo").getAsString();

                        busInfo = changeString.changeBusInfo(busInfo);
                        busName = changeString.changeBusName(busName);

                        String[] busArr = busInfo.split("、");
                        HashSet<String> boom = new HashSet<>();
                        HashSet<String> bdapeng = new HashSet<>();
                        HashSet<String> bgaokuai = new HashSet<>();
                        HashSet<String> bgaofeng = new HashSet<>();
                        for (int i1 = 0; i1 < busArr.length; i1++) {
                            if(busArr[i1].equals("")){
                                continue;
                            }
                            String number = "";
                            boolean q = false;
                            boolean p = true;
                            boolean q1 = false;
                            boolean p1 = true;
                            String newName = "";
                            if(busArr[i1].contains("大鹏假日专线")){
                                p = false;
                                p1 = false;
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(busArr[i1]);
                                while (matcher.find()) {
                                    number = matcher.group();
                                }
                                if(!dapeng.contains(number)){
                                    dapeng.add(number);
                                    q = true;
                                }
                                if(!bdapeng.contains(number)){
                                    bdapeng.add(number);
                                    q1 = true;
                                }
                                newName = "大鹏假日专线" + number;
                            } else if(busArr[i1].contains("高峰专线") ){
                                p = false;
                                p1 = false;
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(busArr[i1]);
                                while (matcher.find()) {
                                    number = matcher.group();
                                }
                                if (!gaofeng.contains(number)) {
                                    gaofeng.add(number);
                                    q = true;
                                }
                                if (!bgaofeng.contains(number)) {
                                    bgaofeng.add(number);
                                    q1 = true;
                                }
                                newName = "高峰专线" + number;
                            }else if(busArr[i1].contains("高快巴士")){
                                p = false;
                                p1 = false;
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(busArr[i1]);
                                while (matcher.find()) {
                                    number = matcher.group();
                                }
                                if(!gaokuai.contains(number)){
                                    gaokuai.add(number);
                                    q = true;
                                }
                                if(!bgaokuai.contains(number)){
                                    bgaokuai.add(number);
                                    q1 = true;
                                }
                                newName = "高快巴士" + number;
                            }
                            if(busArr[i1].endsWith("号")){
                                busArr[i1] = busArr[i1].replace("号", "");
                            }
                            if(busArr[i1].endsWith("需删除") || busArr[i1].contains("无")){
                                continue;
                            }
                            if (busArr[i1].contains(" ")){
                                busArr[i1].replace(" ", "");
                            }

                            if((!bus.containsKey(busArr[i1]) && p ) || q){
                                if(q){
                                    bus.put(newName, bus_id);
                                    boom.add(newName);
                                }else {
                                    bus.put(busArr[i1], bus_id);
                                    boom.add(busArr[i1]);
                                }
                                writer.append(bus_id + ","+ busStationId +"\n");
                                bus_id++;
                            } else {
                                int bid = 0;
                                if(!p1){
                                    bid = bus.get(newName);
                                }else {
                                    bid = bus.get(busArr[i1]);
                                }
                                if((!boom.contains(busArr[i1]) && p1) || q1){
                                    writer.append(bid + "," + busStationId + "\n");
                                    if(q1){
                                        boom.add(newName);
                                    }else {
                                        boom.add(busArr[i1]);
                                    }
                                }
                            }
                        }
                        busStationId++;

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

