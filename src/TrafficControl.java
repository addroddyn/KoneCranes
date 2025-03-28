import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TrafficControl {

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
    private PropertyChangeSupport trafficLight = new PropertyChangeSupport(this);
    private final int vehicleMovementTick = 2000;

    public void generateGrid(int gridSize) {
        mainGrid = new Grid(gridSize);
    }

    public void generateVehicles(int vehicleCount) {
        vehicleFleet = new ArrayList<>(vehicleCount);
        vehicleTreads = new ArrayList<>(vehicleCount);
        for (int i = 0; i < vehicleCount; i++) {
            Vehicle v =  new Vehicle(Integer.toString(i), this, mainGrid.getOriginPoint(), mainGrid.getRandomLocation());
            vehicleFleet.add(v);
            Thread t = new Thread(v);
            vehicleTreads.add(t);
            t.start();
        }
    }

    public void listVehicleProgress() {
        for (Vehicle v : vehicleFleet) {
            System.out.println("Vehicle " + v.getName() + " is currently at " + v.getCurrentLocation().toString() + ". Heading to " + v.getCurrentTarget().toString() + ".");
        }
    }

    public void addPropertyChangeListener(
            PropertyChangeListener l) {
        trafficLight.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener l) {
        trafficLight.removePropertyChangeListener(l);
    }

    public void Go() {
        if (!canGo) {
            canGo = true;
            trafficLight.firePropertyChange("trafficLight", false, true);
        }
    }

    public void Stop() {
        if (canGo) {
            canGo = false;
            trafficLight.firePropertyChange("trafficLight", true, false);
        }
    }

    public void ShutDown() throws InterruptedException {
        for(Vehicle v: vehicleFleet) {
            v.stop();
        }
        for(Thread t : vehicleTreads) {
            t.join();
        }


    }

    public int getVehicleMovementTick() {
        return vehicleMovementTick;
    }
}

