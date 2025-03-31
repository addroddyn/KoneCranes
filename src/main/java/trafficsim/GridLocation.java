package trafficsim;

import java.util.ArrayList;

public class GridLocation {
    private int row;
    private int column;
    private final ArrayList<Vehicle> currentVehicles;
    private boolean isTarget = false;

    public GridLocation(int row, int column) {
        this.row = row;
        this.column = column;
        currentVehicles = new ArrayList<>();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String toString() {
        return row + "," + column;
    }

    public Boolean addVehicleToLocation(Vehicle v) {
        synchronized (this) {
            return currentVehicles.add(v);
        }
    }

    public Boolean removeVehicleFromLocation(Vehicle v) {
        synchronized (this) {
            return currentVehicles.remove(v);
        }
    }

    public boolean hasVehicles(){
        synchronized (this){
            return !currentVehicles.isEmpty();
        }
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setAsTarget(boolean target) {
        isTarget = target;
    }
}
