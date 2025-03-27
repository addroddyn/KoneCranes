import java.util.ArrayList;

public class TrafficControl {

    private static TrafficControl instance = null;

    public static TrafficControl getInstance() {
        if (instance == null)
            instance = new TrafficControl();
        return instance;

    }

    private ArrayList<Vehicle> vehicleFleet;
    private Grid mainGrid;

    public void generateGrid(int gridSize) {
        mainGrid = new Grid(gridSize);
    }

    public void generateVehicles(int vehicleCount) {
        vehicleFleet = new ArrayList<>(vehicleCount);
        for (int i = 0; i < vehicleCount; i++) {
            vehicleFleet.add(new Vehicle());
        }
    }

}
