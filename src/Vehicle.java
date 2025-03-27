public class Vehicle {
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
}
