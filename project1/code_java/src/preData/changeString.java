package preData;

public class changeString {
    public static String changestn(String stationName){
        if(stationName.contains("\n")) {
            stationName = stationName.replace("\n", "");
        }
        if(stationName.contains(" ")){
            stationName = stationName.replace(" ", " ");
        }
        if(stationName.contains(" ")){
            stationName = stationName.replace(" ", " ");
        }
        while (stationName.startsWith(" ")){
            stationName = stationName.substring(1, stationName.length());
        }
        while (stationName.endsWith(" ")){
            stationName = stationName.substring(0, stationName.length() - 1);
        }
        if (stationName.contains("  ")){
            stationName = stationName.replace("  ", " ");
        }
        if(stationName.contains("station")){
            stationName = stationName.replace("station", "Station");
        }
        return stationName;
    }

    public static String changeText(String textt){
        if(textt.contains(" ")){
            textt = textt.replace(" ", " ");
        }
        if(textt.contains(" ")){
            textt = textt.replace(" ", " ");
        }
        if(textt.contains("(")){
            textt = textt.replace("(","（");
        }
        if(textt.contains(")")){
            textt = textt.replace(")","）");
        }
        if(textt.contains(",")){
            textt = textt.replace(",","、");
        }
        if(textt.contains(" ")){
            textt = textt.replace(" ", "");
        }
        return textt;
    }
    public static String changeOut(String outt){
        if(outt.contains(" ")){
            outt = outt.replace(" ", " ");
        }
        if(outt.contains(" ")){
            outt = outt.replace(" ", " ");
        }
        while (outt.startsWith(" ")){
            outt = outt.substring(1, outt.length());
        }
        return outt;
    }

    public static String changeBusInfo(String bus){
        if(bus.contains(" 路")){
            bus = bus.replace(" 路", "");
        }
        if(bus.contains("路")){
            bus = bus.replace("路", "");
        }
        if(bus.contains(" ")){
            bus = bus.replace(" ", "、");
        }
        if(bus.contains("；")){
            bus = bus.replace("；", "、");
        }
        if(bus.contains(";")){
            bus = bus.replace(";", "、");
        }
        if(bus.contains(".")){
            bus = bus.replace(".", "、");
        }
        if(bus.contains(",")){
            bus = bus.replace(",", "、");
        }
        if(bus.contains("，")){
            bus = bus.replace("，", "、");
        }
        if(bus.contains("/")){
            bus = bus.replace("/", "、");
        }
        if(bus.contains("、、")){
            bus = bus.replace("、、", "、");
        }
        if(bus.contains("、、、")){
            bus = bus.replace("、、、", "、");
        }
        if(bus.contains("\n")){
            bus = bus.replace("\n", "");
        }
        if(bus.contains("m")){
            bus = bus.replace("m", "M");
        }
        if(bus.contains("M ")){
            bus = bus.replace("M ", "M");
        }
        if(bus.contains("e")){
            bus = bus.replace("E", "M");
        }
        if(bus.contains("(")){
            bus = bus.replace("(","（");
        }
        if(bus.contains(")")){
            bus = bus.replace(")","）");
        }
        if(bus.contains("（（")){
            bus = bus.replace("（（","（");
        }
        if(bus.contains("））")){
            bus = bus.replace("））","）");
        }
        if(bus.contains(" ")){
            bus = bus.replace(" ", " ");
        }
        if(bus.contains(" ")){
            bus = bus.replace(" ", " ");
        }
        return bus;
    }
    public static String changeBusName(String bus){
        if(bus.contains(" ")){
            bus = bus.replace(" ", "、");
        }
        if(bus.contains(",")){
            bus = bus.replace(",", "、");
        }
        if(bus.contains("(")){
            bus = bus.replace("(","（");
        }
        if(bus.contains(")")){
            bus = bus.replace(")","）");
        }

        return bus;
    }
    public static String changeChuKou(String outt){
        if(outt.contains("人")){
            outt = outt.replace("人", "入");
        }
        if(outt.contains(" ")){
            outt = outt.replace(" ", " ");
        }
        if(outt.contains(" ")){
            outt = outt.replace(" ", " ");
        }
        while (outt.startsWith(" ")){
            outt = outt.substring(1, outt.length());
        }
        if(outt.contains(",")){
            outt = outt.replace(",","、");
        }
        while (outt.contains(" ")){
            outt = outt.replace(" ","");
        }
        return outt;
    }

    //别忘了那个需删除
    public static String changeBus(String bus){
        if(bus.endsWith("路")){
            bus = bus.replace("路", "");
        }
        if(bus.contains("（（")){
            bus.replace("（（" , "（");
        }
        if(bus.contains("））")){
            bus.replace("））" , "）");
        }
        return bus;
    }
}
