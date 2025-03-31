import java.util.ArrayList;

public class GridLocation {
    private int row;
    private int column;
    private final ArrayList<Vehicle> currentVehicles;

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
}
