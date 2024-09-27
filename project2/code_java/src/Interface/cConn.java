package Interface;

public interface cConn {
    /*
     地铁站与地铁线的关系：
     将⼀个或多个地铁站在放⼊⼀个地铁线中指定的位置上。
     将⼀个地铁站从⼀个地铁线中移除
     */
    int putStationOnLine(String name, String l_name, int order);

    //判断stations里有没有存在这个地铁站
    boolean stationExist(String name);

    //判断某条lines上有没有这个地铁站
    boolean stationExistInLine(String name,String lineName);
    int deleteStationFromLine(String l_name, String name);
    boolean LineExist(String name);
    int getLineNoByName(String lineName);
}
