import java.util.Random;

public class Grid {

    private static Grid instance = null;

    public static Grid getInstance(int gridSize) {
        if (instance == null) {
            instance = new Grid(gridSize);
        }
        return instance;

    }

    private final int rowLength;
    private final int columnLength;
    private final GridLocation[][] currentGrid;

    public int GetRowStart() {
        return 0;
    }

    public int GetColumnStart() {
        return 0;
    }

    public int GetColumnEnd() {
        return columnLength - 1;
    }

    public int GetRowEnd() {
        return rowLength - 1;
    }

    private Grid(int gridSize) {
        rowLength = gridSize;
        columnLength = gridSize;
        currentGrid = new GridLocation[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                currentGrid[i][j] = new GridLocation(i, j);
            }
        }
    }

    public GridLocation getOriginPoint() {
        return currentGrid[rowLength / 2][columnLength / 2];
    }

    public GridLocation getRandomLocation() {
        Random rnd = new Random();
        int targetRow = rnd.nextInt(rowLength);
        int targetColumn = rnd.nextInt(columnLength);
        // make sure every vehicle has to move
        if (targetRow == getOriginPoint().getRow() && targetColumn == getOriginPoint().getColumn()) {
            targetRow++;
            targetColumn++;
        }
        return currentGrid[targetRow][targetColumn];
    }

    public boolean moveVehicle(Vehicle vehicle, int currentRow, int currentColumn, int nextRow, int nextColumn) {
        return currentGrid[currentRow][currentColumn].removeVehicleFromLocation(vehicle) &&
                currentGrid[nextRow][nextColumn].addVehicleToLocation(vehicle);
    }

    public boolean addVehicleToOrigin(Vehicle vehicle) {
        return getOriginPoint().addVehicleToLocation(vehicle);
    }
}

