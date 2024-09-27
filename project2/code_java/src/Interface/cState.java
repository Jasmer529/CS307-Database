package Interface;

public interface cState {
    int restart(String name);
    int stop(String name);
    String checkState(String name);
    boolean stationExist(String name);
}
