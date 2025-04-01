package trafficsim;

import java.util.Scanner;

public class Helper {

    public static final class TCEvents {
        public static final String TRAFFIC_GO_STOP = "trafficLight";
        public static final String ALL_VEHICLES_MOVED = "allVehiclesMoved";
        public static final String ALL_VEHICLES_RETIRED = "allVehiclesRetired";
        public static final String NO_REST_FOR_THE_WICKED = "vehicleTakenOutOfRetirement";
    }

    public static final class InputHelpers {

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
                    if (response.trim().isEmpty() && defaultValue != -1) returnValue = defaultValue;
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
            System.out.println("exit -- close the program");
            System.out.println("manual -- manually give a new target to a vehicle. ");
        }

        public static void printNewTargetInstructions(boolean isError, int vehicleCount, int gridSize) {
            if (isError) {
                System.out.println("Invalid vehicle input. Please give a target to a vehicle:");
            } else {
                System.out.println("Manually give a target to a vehicle:");
            }
            System.out.println("V:x,y -- set location x,y as the new target for vehicle V.");
            System.out.println("V:<direction> -- set a directional target for vehicle V. Possible values are 'north', 'east', 'west','south'.");
            System.out.println("V:new -- set a new random location for vehicle V.");
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

        public static void newTargetForVehicle(Vehicle vehicle, GridLocation currentTarget) {
            System.out.println(vehicle.getId() + " is now moving towards " + currentTarget.toString() );
        }

        public static void printStartup(boolean isError) {
            if (isError){
                System.out.println("You have entered an invalid value. Please try again.");
            }
            else {
                System.out.println("Welcome to Traffic sim.");
            }
                System.out.println("This software simulates a number of vehicles going to their target and back on a grid. Please select a generation method:");
                System.out.println("auto -- generate 5 vehicles on a 15x15 grid with random targets (targets can be modified during vehicle transit).");
                System.out.println("basic -- provide grid size and vehicle count. Vehicles will have random targets (targets can be modified during vehicle transit).");
                System.out.println("manual -- provide grid size and vehicle count. Manually select target coordinate for each vehicle (targets can be modified during vehicle transit).");
                System.out.println("exit -- Exit program.");

        }
    }


}
