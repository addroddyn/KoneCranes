package trafficsim;

public class GridLocationDTO {
    private final int row;
    private final int column;
    private final boolean isTarget;
    private final boolean hasVehicle;

    public GridLocationDTO(int row, int column, boolean isTarget, boolean hasVehicle, boolean toDelete) {
        this.row = row;
        this.column = column;
        this.isTarget = isTarget;
        this.hasVehicle = hasVehicle;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }


    public boolean isTarget() {
        return isTarget;
    }

    public boolean isHasVehicle() {
        return hasVehicle;
    }

}
