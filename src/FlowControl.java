import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FlowControl implements PropertyChangeListener {

    static BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    TrafficControl trafficControl;


    public void start() throws InterruptedException, IOException {
        trafficControl = TrafficControl.getInstance();
        /*int gridSize = getIntegerInput("Please enter grid size (empty for default:10, minimum: 5).", 10, 5);
        int vehicleCount = getIntegerInput("Please enter the number of vehicles (empty for default: 1, minimum: 1).", 1, 1);*/
        int gridSize = 10;
        int vehicleCount = 3;
        trafficControl.addFullTickListener(this);

        // input is on different thread so we don't get lost
        Thread inputThread = getInputThread();
        inputThread.start();

        if (gridSize != Integer.MIN_VALUE && vehicleCount != Integer.MIN_VALUE) {
            trafficControl.generateGrid(gridSize);
            trafficControl.generateVehicles(vehicleCount);
            trafficControl.Go();

            // shut down and join up all the threads
            //trafficControl.ShutDown();

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        trafficControl.Stop();

                trafficControl.Go();
    }

    //endregion
}
