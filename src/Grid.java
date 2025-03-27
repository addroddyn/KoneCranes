import java.util.Random;

public class Grid {

    private int rowLength;
    private int columnLength;
    private GridLocation[][] currentGrid;

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

    public Grid(int gridSize) {
        rowLength = gridSize;
        columnLength = gridSize;
        currentGrid = new GridLocation[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                currentGrid[i][j] = new GridLocation(i,j);
            }
        }
    }

    public GridLocation getOriginPoint() {
        return currentGrid[rowLength/2][columnLength/2];
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
}

