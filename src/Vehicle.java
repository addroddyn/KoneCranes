public class Vehicle {
    private String name;
    private GridLocation home;
    private GridLocation currentLocation;
    private GridLocation currentTarget;

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

    public Vehicle(String name,GridLocation home, GridLocation target) {
        this.name = name;
        this.home = home;
        this.currentLocation = home;
        this.currentTarget = target;
    }

    public String getName() {
        return name;
    }
}
