import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TrafficControl trafficControl = TrafficControl.getInstance();
        System.out.println("boo");
        int gridSize = getIntegerInput("Please enter grid size (empty for default:10, minimum: 5).", 10, 5);
        int vehicleCount = getIntegerInput("Please enter the number of vehicles (empty for default: 1, minimum: 1).", 1, 1);

        if (gridSize != Integer.MIN_VALUE && vehicleCount != Integer.MIN_VALUE) {
            trafficControl.generateGrid(gridSize);
            trafficControl.generateVehicles(vehicleCount);
            trafficControl.listVehicleProgress();
        }
    }

    //region getIntegerInput
    public static int getIntegerInput(String message, int defaultValue) {
        return getIntegerInput(message, defaultValue, Integer.MIN_VALUE + 1);
    }

    public static int getIntegerInput(String message, int defaultValue, int lowerLimit) {
        return getIntegerInput(message, defaultValue, lowerLimit, Integer.MAX_VALUE - 1);
    }

    public static int getIntegerInput(String message, int defaultValue, int lowerLimit, int upperLimit) {
        int returnValue = Integer.MIN_VALUE;
        Scanner myObj = new Scanner(System.in);
        while (returnValue < lowerLimit || returnValue > upperLimit) {
            System.out.println(message);
            try {
                String response = myObj.nextLine();
                if (response != null && response.isBlank())
                    returnValue = defaultValue;
                else {
                    returnValue = Integer.parseInt(response);  // Read user input
                    if (returnValue < lowerLimit || returnValue > upperLimit)
                        throw new NumberFormatException();
                }
            } catch (Exception e) {
                System.out.println("Incorrect value or format.");
            }
        }
        return returnValue;
    }
    //endregion
}