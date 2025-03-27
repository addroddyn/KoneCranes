import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Console;

public class Vehicle implements PropertyChangeListener, Runnable {
    private String name;
    private TrafficControl trafficControl;
    private GridLocation home;
    private GridLocation currentLocation;
    private GridLocation currentTarget;
    private Boolean canMove = false;
    private Boolean shouldMove = true;

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
        this.home = home;
        this.currentLocation = home;
        this.currentTarget = target;
        trafficControl.addPropertyChangeListener(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        canMove = (Boolean) e.getNewValue();
        /*System.out.println("Could I go before? " + e.getOldValue());
        System.out.println("Can I go now? " + e.getNewValue());*/
    }

    @Override
    public void run() {
        TryMove();
    }

    public void stop(){
        shouldMove = false;
    }

    private void TryMove() {
        try {
            while (shouldMove) {
                if (canMove) {
                    //System.out.println(this.name + " is trying to move.");
                } else {
                    //System.out.println(this.name + " is not moving.");
                }
                Thread.sleep(1000);
            }
            System.out.println(name + " stopped.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
