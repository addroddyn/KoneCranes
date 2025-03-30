import java.util.Scanner;

public class Helper {

    public static final class TCEvents {
        public static final String TRAFFIC_GO_STOP = "trafficLight";
        public static final String ALL_VEHICLES_MOVED = "allVehiclesMoved";
        public static final String ALL_VEHICLES_RETIRED = "allVehiclesRetired";
        public static final String NO_REST_FOR_THE_WICKED = "vehicleTakenOutOfRetirement";
    }

    public static final class InputHelpers {
        public static int getIntegerInput(String message, int defaultValue) {
            return getIntegerInput(message, defaultValue, Integer.MIN_VALUE + 1);
        }

        public static int getIntegerInput(String message, int defaultValue, int lowerLimit) {
            return getIntegerInput(message, defaultValue, lowerLimit, Integer.MAX_VALUE - 1);
        }

        public static int getIntegerInput(String message, int defaultValue, int lowerLimit, int upperLimit) {
            int returnValue = Integer.MIN_VALUE;
            Scanner scanner = new Scanner(System.in);
            while (returnValue < lowerLimit || returnValue > upperLimit) {
                System.out.println(message);
                try {
                    String response = scanner.nextLine();
                    if (response == null) {
                        throw new NumberFormatException();
                    }
                    if (response.isBlank()) returnValue = defaultValue;
                    else {
                        returnValue = Integer.parseInt(response);
                        if (returnValue < lowerLimit || returnValue > upperLimit) throw new NumberFormatException();
                    }
                } catch (Exception e) {
                    System.out.println("Incorrect value or format.");
                }
            }
            return returnValue;
        }
    }

    public static final class OutputHelpers {
        public static void printInputInstructions(Boolean isError) {
            if (isError) {
                System.out.println("Invalid command. Possible commands:");
            } else {
                System.out.println("Traffic stopped. Possible commands:");
            }
            System.out.println("resume -- continue traffic normally");
            System.out.println("stop -- close the program");
            System.out.println("manual -- manually give a new target to a vehicle. ");
        }

        public static void printNewTargetInstructions(boolean isError, int vehicleCount, int gridSize) {
            if (isError) {
                System.out.println("Invalid vehicle input. Please give a target to a vehicle:");
            } else {
                System.out.println("Manually give a target to a vehicle:");
            }
            System.out.println("V:x,y -- set location x,y as the new target for vehicle V");
            System.out.println("You have vehicles between 0 and " + (vehicleCount - 1) + " (inclusive).");
            System.out.println("The grid coordinates go from 0 to " + (gridSize - 1) + " (inclusive).");
            System.out.println("Type 'exit' to return to previous menu.");
        }

        public static void printResume() {
            System.out.println("You've chosen to resume the program. Carrying on.");
        }

        public static void printShutdown() {
            System.out.println("You've chosen to stop the program. Shutting down..");
        }

        public static void newTargetForVehicle(Vehicle v, GridLocation currentTarget) {
            System.out.println(v.getId() + " is now moving towards " + currentTarget.toString() );
        }
    }


}
