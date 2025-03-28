import java.util.ArrayList;

public class GridLocation {
    private int row;
    private int column;
    private ArrayList<Vehicle> currentVehicles;

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
        return Integer.toString(row) + "," + Integer.toString(column);
    }

    public Boolean addVehicleToLocation(Vehicle v) {
        return currentVehicles.add(v);
    }

    public Boolean removeVehicleFromLocation(Vehicle v) {
            return currentVehicles.remove(v);
    }
}
