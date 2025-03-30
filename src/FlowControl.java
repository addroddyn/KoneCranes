import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Scanner;

public class FlowControl implements PropertyChangeListener {

    private static enum INPUT_STATE {
        VALID,
        ERROR_MAIN,
        ERROR_MANUAL,
        EXITED_MANUAL
    }

    TrafficControl trafficControl;
    private Boolean isThereActiveVehicle = true;
    int gridSize = 10;
    int vehicleCount = 3;


    public void start() {
        trafficControl = TrafficControl.getInstance();
        /*int gridSize = Helper.InputHelpers.getIntegerInput("Please enter grid size (empty for default:10, minimum: 5).", 10, 5);
        int vehicleCount = Helper.InputHelpers.getIntegerInput("Please enter the number of vehicles (empty for default: 1, minimum: 1).", 1, 1);*/
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
                Scanner scanner = new Scanner(System.in);
                String input = "";
                INPUT_STATE state = INPUT_STATE.VALID;
                while (isThereActiveVehicle) {
                    if (state == INPUT_STATE.ERROR_MAIN) {
                        Helper.OutputHelpers.printInputInstructions(true);
                    } else if (state == INPUT_STATE.VALID || state == INPUT_STATE.EXITED_MANUAL) {
                        if (state == INPUT_STATE.VALID) {
                            System.in.read();
                        }
                        Helper.OutputHelpers.printInputInstructions(false);
                    }
                    trafficControl.Stop();
                    if (state == INPUT_STATE.ERROR_MANUAL) {
                        input = "manual";
                    } else {
                        input = scanner.nextLine();
                    }
                    switch (input) {
                        case "resume":
                            Helper.OutputHelpers.printResume();
                            state = INPUT_STATE.VALID;
                            trafficControl.Go();
                            break;
                        case "stop":
                            Helper.OutputHelpers.printShutdown();
                            isThereActiveVehicle = false;
                            trafficControl.ShutDown();
                            System.exit(0);
                            break;
                        case "manual":
                            if (state == INPUT_STATE.ERROR_MANUAL) {
                                Helper.OutputHelpers.printNewTargetInstructions(true, vehicleCount, gridSize);
                            } else {
                                Helper.OutputHelpers.printNewTargetInstructions(false, vehicleCount, gridSize);
                            }
                            input = scanner.nextLine();
                            if (input.equals("exit")) {
                                state = INPUT_STATE.EXITED_MANUAL;
                                break;
                            }
                            VehicleInput vehicleInput = new VehicleInput(input, vehicleCount, gridSize);
                            if (vehicleInput.isValid()) {
                                state = INPUT_STATE.VALID;
                                trafficControl.newTargetForVehicle(vehicleInput);
                                trafficControl.Go();
                            } else {
                                state = INPUT_STATE.ERROR_MANUAL;
                            }
                            break;
                        default:
                            state = INPUT_STATE.ERROR_MAIN;
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e + ": " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // so we don't get hung up on the input thread
        exitWatcher.setDaemon(true);
        return exitWatcher;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case Helper.TCEvents.ALL_VEHICLES_MOVED:
                trafficControl.Stop();
                trafficControl.Go();
                break;
            case Helper.TCEvents.ALL_VEHICLES_RETIRED:
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


}
