import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Scanner;

public class FlowControl implements PropertyChangeListener {

    TrafficControl trafficControl;
    private Boolean isThereActiveVehicle = true;


    public void start()  {
        trafficControl = TrafficControl.getInstance();
        /*int gridSize = getIntegerInput("Please enter grid size (empty for default:10, minimum: 5).", 10, 5);
        int vehicleCount = getIntegerInput("Please enter the number of vehicles (empty for default: 1, minimum: 1).", 1, 1);*/
        int gridSize = 10;
        int vehicleCount = 3;
        trafficControl.addFullTickListener(this);
        trafficControl.addRetirementListener(this);

        //get a separate thread to listen to user input
        Thread exitWatcher = getThread();
        exitWatcher.start();


        if (gridSize != Integer.MIN_VALUE && vehicleCount != Integer.MIN_VALUE) {
            trafficControl.generateGrid(gridSize);
            trafficControl.generateVehicles(vehicleCount);
            trafficControl.Go();
        }
    }

    private Thread getThread() {
        Thread exitWatcher = new Thread(() -> {
            try {
                while (isThereActiveVehicle) {
                    System.in.read(); // Wait for any key
                    System.out.println("🛑 Stopping...");
                    trafficControl.Stop();
                    System.in.read();
                    trafficControl.Go();
                }
            } catch (IOException e) {
                System.out.println(e + ": " + e.getMessage());
            }
        });
        // so we don't get hung up on the input thread
        exitWatcher.setDaemon(true);
        return exitWatcher;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case TrafficControl.TCPropertyEvents.ALL_VEHICLES_MOVED:
                trafficControl.Stop();
                trafficControl.Go();
                break;
            case TrafficControl.TCPropertyEvents.ALL_VEHICLES_RETIRED:
                isThereActiveVehicle = false;
                try {
                    trafficControl.ShutDown();
                    System.exit(0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
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
