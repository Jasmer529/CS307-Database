package Trans;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import preData.changeString;

public class busGet {

    public static void main(String[] args) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\resource\\stations.json");
             FileWriter writer = new FileWriter("bus.csv")) {
            // 解析 JSON 文件
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // 写入 CSV 文件头部
            writer.append("id, bus_n\n");

            int bus_id = 1;
            HashMap<String, String> bus = new HashMap<>();
            HashSet<String> dapeng = new HashSet<>();
            HashSet<String> gaokuai = new HashSet<>();
            HashSet<String> gaofeng = new HashSet<>();
            int count = 1;

            for (String stationName : jsonObject.keySet()) {
                JsonObject stationObject = jsonObject.getAsJsonObject(stationName);
                JsonArray bus_info = stationObject.getAsJsonArray("bus_info");
                stationName = changeString.changestn(stationName);
                String last = "";
                // 遍历每个站点的公交信息
                for (int i = 0; i < bus_info.size(); i++) {
                    JsonObject busObject = bus_info.get(i).getAsJsonObject();
                    JsonArray bus_out_info = busObject.getAsJsonArray("busOutInfo");
                    // 遍历每个公交信息的出口信息
                    for (int j = 0; j < bus_out_info.size(); j++) {
                        JsonObject busOut = bus_out_info.get(j).getAsJsonObject();

                        String busInfo = busOut.get("busInfo").getAsString();
                        String busName = busOut.get("busName").getAsString();
                        busInfo = changeString.changeBusInfo(busInfo);
                        String[] busArr = busInfo.split("、");
                        for (int i1 = 0; i1 < busArr.length; i1++) {
                            if(busArr[i1].equals("")){
                                continue;
                            }
                            String number = "";
                            String newName = "";
                            boolean q = false;
                            boolean p = true;
                            if(busArr[i1].contains("大鹏假日专线")){
                                p = false;
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(busArr[i1]);
                                while (matcher.find()) {
                                    number = matcher.group();
                                }
                                if(!dapeng.contains(number)){
                                    dapeng.add(number);
                                    q = true;
                                }
                                newName = "大鹏假日专线" + number;
                            } else if( busArr[i1].contains("高峰专线") ){
                                p = false;
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(busArr[i1]);
                                while (matcher.find()) {
                                    number = matcher.group();
                                }
                                if (!gaofeng.contains(number)) {
                                    gaofeng.add(number);
                                    q = true;
                                }
                                newName = "高峰专线" + number;
                            }else if(busArr[i1].contains("高快巴士")){
                                p = false;
                                Pattern pattern = Pattern.compile("\\d+");
                                Matcher matcher = pattern.matcher(busArr[i1]);
                                while (matcher.find()) {
                                    number = matcher.group();
                                }
                                if(!gaokuai.contains(number)){
                                    gaokuai.add(number);
                                    q = true;
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

                            if((!bus.containsKey(busArr[i1])&& p ) || q){
                                if(q){
                                    bus.put(newName, busName);
                                    writer.append(bus_id + ","+ newName  + "\n");
                                }else {
                                    bus.put(busArr[i1], busName);
                                    writer.append(bus_id + ","+ busArr[i1]  + "\n");
                                }

                                bus_id++;
                            }
                            else {
                                count++;
                            }
                        }

                    }
                }
            }
            System.out.println(count);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

