import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class TrafficControl {

    public static final class TCPropertyEvents {
        public static final String TRAFFIC_GO_STOP = "trafficLight";
        public static final String ALL_VEHICLES_MOVED = "allVehiclesMoved";
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
    private Boolean canGo = false;
    private PropertyChangeSupport propertyChangedBroadcaster = new PropertyChangeSupport(this);
    private final int vehicleMovementTick = 1000;
    private int vehiclesMovedThisTick = 0;


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
            addVehicleToOrigin(v);
        }
    }

    public void listVehicleProgress() {
        for (Vehicle v : vehicleFleet) {
            System.out.println("Vehicle " + v.getName() + " is currently at " + v.getCurrentLocation().toString() + ". Heading to " + v.getCurrentTarget().toString() + ".");
        }
    }

    public void addTrafficLightListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCPropertyEvents.TRAFFIC_GO_STOP, l);
    }

    public void removeTrafficLightListener(PropertyChangeListener listener) {
        propertyChangedBroadcaster.removePropertyChangeListener(TCPropertyEvents.TRAFFIC_GO_STOP, listener);
    }


    public void addFullTickListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCPropertyEvents.ALL_VEHICLES_MOVED, l);
    }

    public void removeFullTickListener(PropertyChangeListener listener) {
        propertyChangedBroadcaster.removePropertyChangeListener(TCPropertyEvents.ALL_VEHICLES_MOVED, listener);
    }

    public void Go() {
        //if (!canGo) {
            //canGo = true;
            propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.TRAFFIC_GO_STOP, false, true);
        //}
    }

    public void Stop() {
        //if (canGo) {
            vehiclesMovedThisTick = 0;
            //canGo = false;
            propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.TRAFFIC_GO_STOP, true, false);
        //}
    }

    public void ShutDown() throws InterruptedException {
        for (Vehicle v : vehicleFleet) {
            v.stop();
        }
        for (Thread t : vehicleTreads) {
            t.join();
        }


    }

    public int getVehicleMovementTick() {
        return vehicleMovementTick;
    }

    private boolean addVehicleToOrigin(Vehicle vehicle) {
        return mainGrid.addVehicleToOrigin(vehicle);
    }

    public void vehicleRetired(Vehicle vehicle)
    {
        vehicleFleet.remove(vehicle);
    }

    public boolean requestMove(Vehicle vehicle, int currentRow, int currentColumn, int nextRow, int nextColumn) {
        if (mainGrid.moveVehicle(vehicle, currentRow, currentColumn, nextRow, nextColumn)) {
            vehiclesMovedThisTick++;
            if (vehiclesMovedThisTick == vehicleFleet.size()){
                vehiclesMovedThisTick = 0;
                propertyChangedBroadcaster.firePropertyChange(TCPropertyEvents.ALL_VEHICLES_MOVED, false, true);
            }

            return true;
        }
        return false;
    }
}

