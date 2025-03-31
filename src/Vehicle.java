import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Vehicle implements PropertyChangeListener, Runnable {
    private final int id;
    private final TrafficControl trafficControl;
    private final GridLocation home;
    private final GridLocation currentLocation;
    private GridLocation currentTarget;
    private Boolean canMove = false;
    private Boolean shouldMove = true;
    private final int tick;

    public GridLocation getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(GridLocation currentTarget) {
        this.currentTarget = currentTarget;
        if (!shouldMove){
            trafficControl.vehicleUnRetired(this);
        }
    }

    public int getId(){
        return id;
    }

    public Vehicle(int id, TrafficControl control, GridLocation home, GridLocation target) {
        this.id = id;
        this.trafficControl = control;
        //this.home = home;
        this.home = new GridLocation(home.getRow(), home.getColumn());
        this.currentLocation = new GridLocation(home.getRow(), home.getColumn());
        this.currentTarget = new GridLocation(target.getRow(), target.getColumn());
        trafficControl.addTrafficLightListener(this);
        trafficControl.addUnRetirementListener(this);
        tick = trafficControl.getVehicleMovement();
        vehicleLogLine("Created with target of " + currentTarget.toString());
    }


    @Override
    public void propertyChange(PropertyChangeEvent e) {
        switch (e.getPropertyName()) {
            case Helper.TCEvents.TRAFFIC_GO_STOP:
            canMove = (Boolean) e.getNewValue();
            break;
            case Helper.TCEvents.NO_REST_FOR_THE_WICKED:
                if ((int)e.getNewValue() == id) {
                    Thread newThread = new Thread(this);
                    newThread.start();
                }
        }
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
                    // let's not spam the CPU
                    //noinspection BusyWait
                    Thread.sleep(10);
                }

                if (!shouldMove) break;
                if (currentLocation.getRow() != currentTarget.getRow() || currentLocation.getColumn() != currentTarget.getColumn()) {
                    moveTowardsCurrentTarget();
                }
                // add a bit of delay to vehicle movement, so that the whole thing is not over in .2 seconds
                //noinspection BusyWait
                Thread.sleep(tick);
            }
            System.out.println(id + " stopped.");
        } catch (Exception e) {
            vehicleLogLine(e + ", "+ e.getMessage());
        }
    }

    private void moveTowardsCurrentTarget() {
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
            vehicleLogLine("Moved to " + currentLocation);
        }
        if (currentLocation.getRow() == currentTarget.getRow() && currentLocation.getColumn() == currentTarget.getColumn()) {
            if (currentLocation.getRow() != home.getRow() || currentLocation.getColumn() != home.getColumn()) {
                vehicleLogLine("Reached its target and is now going home.");
                currentTarget.setRow(home.getRow());
                currentTarget.setColumn(home.getColumn());
            } else {
                vehicleLogLine("Arrived home and is now retiring.");
                trafficControl.vehicleRetired();
                shouldMove = false;
            }
        }
    }

    private void vehicleLogLine(String message){
        System.out.println(id + ": " + message);
    }
}
