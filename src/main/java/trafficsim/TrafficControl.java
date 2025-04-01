package trafficsim;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import static trafficsim.Helper.*;

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
            Vehicle vehicle = new Vehicle(i, this, mainGrid.getOriginPoint(), mainGrid.getRandomLocation());
            vehicleFleet.add(vehicle);
            Thread t = new Thread(vehicle);
            vehicleTreads.add(t);
            t.start();
            if (!addVehicleToOrigin(vehicle)) {
                throw new RuntimeException();
            }
        }
    }

    public void generateVehicles(int vehicleCount, HashMap<Integer,GridLocation> vehicleTargets) {
        vehicleFleet = new ArrayList<>(vehicleCount);
        vehicleTreads = new ArrayList<>(vehicleCount);
        for (int i = 0; i < vehicleCount; i++) {
            Vehicle vehicle = new Vehicle(i, this, mainGrid.getOriginPoint(), new GridLocation(vehicleTargets.get(i).getRow(), vehicleTargets.get(i).getColumn()));
            vehicleFleet.add(vehicle);
            Thread t = new Thread(vehicle);
            vehicleTreads.add(t);
            t.start();
            if (!addVehicleToOrigin(vehicle)) {
                throw new RuntimeException();
            }
        }
    }

    public void addTrafficLightListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCEvents.TRAFFIC_GO_STOP, l);
    }


    public void addFullTickListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCEvents.ALL_VEHICLES_MOVED, l);
    }

    public void addRetirementListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCEvents.ALL_VEHICLES_RETIRED, l);
    }

    public void addUnRetirementListener(PropertyChangeListener l) {
        propertyChangedBroadcaster.addPropertyChangeListener(TCEvents.NO_REST_FOR_THE_WICKED, l);
    }

    public void Go() {
        propertyChangedBroadcaster.firePropertyChange(TCEvents.TRAFFIC_GO_STOP, false, true);
    }

    public void Stop() {
        vehiclesMovedThisTick = 0;
        propertyChangedBroadcaster.firePropertyChange(TCEvents.TRAFFIC_GO_STOP, true, false);
    }

    public void ShutDown() throws InterruptedException {
        for (Vehicle vehicle : vehicleFleet) {
            vehicle.stop();


        }
        for (Thread thread : vehicleTreads) {
            thread.join(500);
        }


    }

    public int getVehicleMovement() {
        return 1500;
    }

    private Boolean addVehicleToOrigin(Vehicle vehicle) {
        return mainGrid.addVehicleToOrigin(vehicle);
    }

    public void vehicleRetired() {
        retiredVehicles++;
        if (retiredVehicles == vehicleFleet.size()) {
            propertyChangedBroadcaster.firePropertyChange(TCEvents.ALL_VEHICLES_RETIRED, false, true);
        }
    }

    public void vehicleUnRetired(Vehicle vehicle) {
        retiredVehicles--;
        propertyChangedBroadcaster.firePropertyChange(TCEvents.NO_REST_FOR_THE_WICKED, -1, vehicle.getId());
    }

    public boolean requestMove(Vehicle vehicle, int currentRow, int currentColumn, int nextRow, int nextColumn) {
        if (mainGrid.moveVehicle(vehicle, currentRow, currentColumn, nextRow, nextColumn)) {
            vehiclesMovedThisTick++;
            if (vehiclesMovedThisTick == vehicleFleet.size()) {
                vehiclesMovedThisTick = 0;
                propertyChangedBroadcaster.firePropertyChange(TCEvents.ALL_VEHICLES_MOVED, false, true);
            }

            return true;
        }
        return false;
    }

    public void newTargetForVehicle(VehicleInput input) {
        for (Vehicle vehicle : vehicleFleet) {
            if (vehicle.getId() == input.getVehicleId()) {
                if (input.isDirection()) {
                    if (input.getRow() != -1) {
                        vehicle.setCurrentTarget(new GridLocation(input.getRow(), vehicle.getCurrentTarget().getColumn()));
                    } else if (input.getColumn() != -1) {
                        vehicle.setCurrentTarget(new GridLocation(vehicle.getCurrentTarget().getRow(), input.getColumn()));
                    }
                } else {
                    vehicle.setCurrentTarget(new GridLocation(input.getRow(), input.getColumn()));
                }
                OutputHelpers.newTargetForVehicle(vehicle, vehicle.getCurrentTarget());
            }
        }
    }


    public Grid getGridSnapshot() {
        return mainGrid;
    }

    public void vehicleTargetSet(GridLocation newTarget) {
        mainGrid.UpdateTargetOnLocation(newTarget, true);
    }

    public void vehicleTargetRemoved(GridLocation currentTarget) {
        mainGrid.UpdateTargetOnLocation(currentTarget, false);
    }
}

