import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Vehicle implements PropertyChangeListener, Runnable {
    private String name;
    private TrafficControl trafficControl;
    private GridLocation home;
    private GridLocation currentLocation;
    private GridLocation currentTarget;
    private Boolean canMove = false;
    private Boolean shouldMove = true;
    private final int tick;

    public GridLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GridLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public GridLocation getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(GridLocation currentTarget) {
        this.currentTarget = currentTarget;
    }

    public Vehicle(String name, TrafficControl control, GridLocation home, GridLocation target) {
        this.name = name;
        this.trafficControl = control;
        //this.home = home;
        this.home = new GridLocation(home.getRow(), home.getColumn());
        this.currentLocation = new GridLocation(home.getRow(), home.getColumn());
        this.currentTarget = target;
        trafficControl.addTrafficLightListener(this);
        tick = trafficControl.getVehicleMovementTick();
        System.out.println(name + " created with target of " + currentTarget.toString());
    }

    public String getName() {
        return name;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        canMove = (Boolean) e.getNewValue();
    }

    @Override
    public void run() {
        shouldMove = true;
        TryMove();
    }

    public void stop() {
        // used for graceful shutdown of the thread
        shouldMove = false;
    }

    private void TryMove() {
        try {
            while (shouldMove) {
                while (!canMove && shouldMove) {
                    Thread.sleep(10); // wait until traffic light turns green
                }

                if (!shouldMove) break;
                if (currentLocation.getRow() != currentTarget.getRow() || currentLocation.getColumn() != currentTarget.getColumn()) {
                    moveTowardsCurrentTarget();
                }
                Thread.sleep(tick);
            }
            System.out.println(name + " stopped.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void moveTowardsCurrentTarget() throws InterruptedException {
        int nextRow = currentLocation.getRow();
        int nextColumn = currentLocation.getColumn();
        if (currentLocation.getRow() > currentTarget.getRow()) {
            nextRow--;
        }
        if (currentLocation.getRow() < currentTarget.getRow()) {
            nextRow++;
        }
        if (currentLocation.getColumn() > currentTarget.getColumn()) {
            nextColumn--;
        }
        if (currentLocation.getColumn() < currentTarget.getColumn()) {
            nextColumn++;
        }
        if (trafficControl.requestMove(this, currentLocation.getRow(), currentLocation.getColumn(), nextRow, nextColumn)) {
            currentLocation.setRow(nextRow);
            currentLocation.setColumn(nextColumn);
            System.out.println(name + " moved to " + currentLocation.toString());
        }
        if (currentLocation.getRow() == currentTarget.getRow() && currentLocation.getColumn() == currentTarget.getColumn()) {
            if (currentLocation.getRow() != home.getRow() || currentLocation.getColumn() != home.getColumn()) {
                System.out.println(name + " has reached its target and is now going home.");
                currentTarget.setRow(home.getRow());
                currentTarget.setColumn(home.getColumn());
            } else {
                System.out.println(name + " arrived home and is now retiring.");
                shouldMove = false;
            }
        }
    }
}
