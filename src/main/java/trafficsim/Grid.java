package trafficsim;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

    private static Grid instance = null;

    public static Grid getInstance(int gridSize) {
        if (instance == null) {
            instance = new Grid(gridSize);
        }
        return instance;

    }

    private final int rowCount;
    private final int columnCount;
    private final GridLocation[][] currentGrid;

    public void UpdateTargetOnLocation(GridLocation currentTarget, boolean b) {
        currentGrid[currentTarget.getRow()][currentTarget.getColumn()].setAsTarget(b);
    }

    private Grid(int gridSize) {
        rowCount = gridSize;
        columnCount = gridSize;
        currentGrid = new GridLocation[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                currentGrid[i][j] = new GridLocation(i, j);
            }
        }
    }

    public GridLocation getOriginPoint() {
        return currentGrid[rowCount / 2][columnCount / 2];
    }

    public GridLocation getRandomLocation() {
        Random rnd = new Random();
        int targetRow = rnd.nextInt(rowCount);
        int targetColumn = rnd.nextInt(columnCount);
        // make sure every vehicle has to move
        if (targetRow == getOriginPoint().getRow() && targetColumn == getOriginPoint().getColumn()) {
            targetRow += 2;
            targetColumn += 2;
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


    public GridDTO getGridDTO() {
        GridDTO dto = new GridDTO();
        dto.rowCount = rowCount;
        dto.originRow = getOriginPoint().getRow();
        dto.originColumn = getOriginPoint().getColumn();

        ArrayList<ArrayList<GridLocationDTO>> dtoLocations = new ArrayList<>();
        for (int row = 0;row < rowCount; row++)
        {
            ArrayList<GridLocationDTO> locationRow = new ArrayList<>();
            for (int column = 0; column < columnCount; column++){
                GridLocation actualLocation = currentGrid[row][column];
                boolean isCenter = actualLocation.getRow() == getOriginPoint().getRow() && actualLocation.getColumn() == getOriginPoint().getColumn();
                locationRow.add(new GridLocationDTO(row, column, isCenter, actualLocation.isTarget(), actualLocation.hasVehicles()));

            }
            dtoLocations.add(locationRow);
        }
        dto.locations = dtoLocations;
        return dto;
    }


}

