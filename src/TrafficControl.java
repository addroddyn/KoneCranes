import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class TrafficControl {

    public static final class TCPropertyEvents {
        public static final String TRAFFIC_GO_STOP = "trafficLight";
        public static final String ALL_VEHICLES_MOVED = "allVehiclesMoved";
        public static final String ALL_VEHICLES_RETIRED = "allVehiclesRetired";
    }

    private static TrafficControl instance = null;

    public static TrafficControl getInstance() {
        if (instance == null)
            instance = new TrafficControl();
        return instance;
    }

    private ArrayList<Vehicle> vehicleFleet;
    private ArrayList<Thread> vehicleTreads;
    private Grid mainGrid;
    private final PropertyChangeSupport propertyChangedBroadcaster = new PropertyChangeSupport(this);
    private int vehiclesMovedThisTick = 0;
    private int retiredVehicles = 0;


    public void generateGrid(int gridSize) {
        mainGrid = Grid.getInstance(gridSize);
    }

    public void generateVehicles(int vehicleCount) {
        vehicleFleet = new ArrayList<>(vehicleCount);
        vehicleTreads = new ArrayList<>(vehicleCount);
        for (int i = 0; i < vehicleCount; i++) {
            Vehicle v = new Vehicle(Integer.toString(i), this, mainGrid.getOriginPoint(), mainGrid.getRandomLocation());
            vehicleFleet.add(v);
            Thread t = new Thread(v);
            vehicleTreads.add(t);
            t.start();
            if (!addVehicleToOrigin(v)) {
                throw new RuntimeException();
            }
        }
    }

    public void addTrafficLightListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCPropertyEvents.TRAFFIC_GO_STOP, l);
    }


    public void addFullTickListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCPropertyEvents.ALL_VEHICLES_MOVED, l);
    }

    public void addRetirementListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCPropertyEvents.ALL_VEHICLES_RETIRED, l);
    }

    public void Go() {
        propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.TRAFFIC_GO_STOP, false, true);
    }

    public void Stop() {
        vehiclesMovedThisTick = 0;
        propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.TRAFFIC_GO_STOP, true, false);
    }

    public void ShutDown() throws InterruptedException {
        for (Vehicle v : vehicleFleet) {
            v.stop();
        }
        for (Thread t : vehicleTreads) {
            t.join(100);
        }


    }

    public int getVehicleMovement() {
        return 1000;
    }

    private Boolean addVehicleToOrigin(Vehicle vehicle) {
        return mainGrid.addVehicleToOrigin(vehicle);
    }

    public void vehicleRetired() {
        retiredVehicles++;
        if (retiredVehicles == vehicleFleet.size()) {
            propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.ALL_VEHICLES_RETIRED, false, true);
        }
    }

    public boolean requestMove(Vehicle vehicle, int currentRow, int currentColumn, int nextRow, int nextColumn) {
        if (mainGrid.moveVehicle(vehicle, currentRow, currentColumn, nextRow, nextColumn)) {
            vehiclesMovedThisTick++;
            if (vehiclesMovedThisTick == vehicleFleet.size()) {
                vehiclesMovedThisTick = 0;
                propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.ALL_VEHICLES_MOVED, false, true);
            }

            return true;
        }
        return false;
    }
}

