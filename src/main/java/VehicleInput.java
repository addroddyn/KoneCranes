import java.util.Random;

public class VehicleInput {

    private int vehicleId = -1;
    private int row = -1;
    private int column = -1;
    private boolean isValid = false;
    private boolean isDirection = false;

    public int getVehicleId() {
        return vehicleId;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public boolean isValid() {
        return isValid;
    }

    public boolean isDirection() {
        return isDirection;
    }

    public VehicleInput(String inputString, int vehicleCount, int gridSize) {
        String[] splitInput = inputString.split(":");
        try {
            if (splitInput.length == 2) {
                vehicleId = Integer.parseInt(splitInput[0]);
                String[] suffix = splitInput[1].split(",");
                if (suffix.length == 2) {
                    row = Integer.parseInt(suffix[0]);
                    column = Integer.parseInt(suffix[1]);
                }
                if (suffix[0].equals("new"))
                {
                    Random rnd = new Random();
                    row = rnd.nextInt(gridSize);
                    column = rnd.nextInt(gridSize);
                    if (row == (gridSize /2)  && column == (gridSize /2)) {
                        row++;
                        column++;
                    }
                }
                if (vehicleId >= 0 && vehicleId < vehicleCount && suffix[0].equals("north"))
                {
                    isValid = true;
                    isDirection = true;
                    row = 0;
                }
                if (vehicleId >= 0 && vehicleId < vehicleCount && suffix[0].equals("south"))
                {
                    isValid = true;
                    isDirection = true;
                    row = gridSize - 1;
                }
                if (vehicleId >= 0 && vehicleId < vehicleCount && suffix[0].equals("west"))
                {
                    isValid = true;
                    isDirection = true;
                    column = 0;
                }
                if (vehicleId >= 0 && vehicleId < vehicleCount && suffix[0].equals("east"))
                {
                    isValid = true;
                    isDirection = true;
                    column = gridSize -1 ;
                }
                if (vehicleId >= 0 && row >= 0 && column >= 0 &&
                        vehicleId < vehicleCount && row < gridSize && column < gridSize) {
                    isValid = true;
                }
            }
        }
        catch (Exception e)
        {
            isValid = false;
        }
    }

}
