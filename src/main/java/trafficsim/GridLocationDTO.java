package trafficsim;

public class GridLocationDTO {
    private int row = 0;
    private int column = 0;
    private boolean isCenter = false;
    private boolean isTarget = false;
    private boolean hasVehicle = false;

    public GridLocationDTO(int row, int column, boolean isCenter, boolean isTarget, boolean hasVehicle) {
        this.row = row;
        this.column = column;
        this.isCenter = isCenter;
        this.isTarget = isTarget;
        this.hasVehicle = hasVehicle;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isCenter() {
        return isCenter;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public boolean isHasVehicle() {
        return hasVehicle;
    }
}
