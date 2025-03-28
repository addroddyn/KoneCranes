import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class Main {

    static BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        TrafficControl trafficControl = TrafficControl.getInstance();
        /*int gridSize = getIntegerInput("Please enter grid size (empty for default:10, minimum: 5).", 10, 5);
        int vehicleCount = getIntegerInput("Please enter the number of vehicles (empty for default: 1, minimum: 1).", 1, 1);*/
        int gridSize = 10;
        int vehicleCount = 1;
        int inputWaitInterval = 2000;
        int controlTick = (int) Math.round((trafficControl.getVehicleMovementTick() * 1.5));

        // input is on different thread so we don't get lost
        Thread inputThread = getInputThread();
        inputThread.start();

        if (gridSize != Integer.MIN_VALUE && vehicleCount != Integer.MIN_VALUE) {
            trafficControl.generateGrid(gridSize);
            trafficControl.generateVehicles(vehicleCount);
            String input = "";
            while (input == null || !input.equalsIgnoreCase("stop")) {
                System.out.println("------------");
                // we make sure enough time has passed so that every vehicle stepped once
                Thread.sleep(controlTick);
                System.out.println("Vehicles will resume moving in 2 seconds. Type 'stop' to stop.");
                trafficControl.Stop();

                input = null;
                long deadline = System.currentTimeMillis() + 2000;

                // Wait up to 2 seconds for input
                while (System.currentTimeMillis() < deadline && input == null) {
                    input = inputQueue.poll(inputWaitInterval, TimeUnit.MILLISECONDS);
                }

                if (input == null) {
                    System.out.println("No input. Letting vehicles go...");
                    trafficControl.Go();
                } else {
                    System.out.println("User typed: " + input);
                    // Handle other commands like "MAN" here if needed
                }
            }
            // shut down and join up all the threads
            trafficControl.ShutDown();
        }
    }

    private static Thread getInputThread() {
        Thread inputThread = new Thread(() -> {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    String input = in.readLine();
                    if (input != null) {
                        inputQueue.put(input.trim());
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });
        // daemon so the program doesn't wait for it to finish
        inputThread.setDaemon(true);
        return inputThread;
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
                if (response == null) {
                    throw new NumberFormatException();
                }
                if (response.isBlank()) returnValue = defaultValue;
                else {
                    returnValue = Integer.parseInt(response);  // Read user input
                    if (returnValue < lowerLimit || returnValue > upperLimit) throw new NumberFormatException();
                }
            } catch (Exception e) {
                System.out.println("Incorrect value or format.");
            }
        }
        return returnValue;
    }
    //endregion
}