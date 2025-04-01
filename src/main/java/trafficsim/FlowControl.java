package trafficsim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static trafficsim.Helper.*;

@Component
public class FlowControl implements PropertyChangeListener {
    @Autowired
    private final GridStreamController gridStreamController = new GridStreamController();

    private enum INPUT_STATE {
        VALID,
        ERROR_MAIN,
        ERROR_MANUAL,
        EXITED_MANUAL
    }

    TrafficControl trafficControl;
    private Boolean isThereActiveVehicle = true;
    int gridSize = 15;
    int vehicleCount = 3;


    public void start() {
        trafficControl = TrafficControl.getInstance();

        Scanner scanner = new Scanner(System.in);
        boolean inputChosen = false;
        boolean inputError = false;
        while (!inputChosen) {
            OutputHelpers.printStartup(inputError);
            String generationMethod = scanner.nextLine();
            inputError = false;
            switch (generationMethod) {
                case "auto":
                    trafficControl.generateGrid(gridSize);
                    trafficControl.generateVehicles(vehicleCount);
                    inputChosen = true;
                    break;
                case "basic":
                    gridSize = InputHelpers.getIntegerInput("Please enter grid size (empty for default:15, minimum: 5).", 10, 5);
                    vehicleCount = InputHelpers.getIntegerInput("Please enter the number of vehicles (empty for default: 5, minimum: 1).", 1, 1);
                    if (gridSize != Integer.MIN_VALUE && vehicleCount != Integer.MIN_VALUE) {
                        inputChosen = true;
                    } else {
                        inputError = true;
                    }
                    break;
                case "manual":
                    int gridSize = InputHelpers.getIntegerInput("Please enter grid size (empty for default:15, minimum: 5).", 10, 5);
                    int vehicleCount = InputHelpers.getIntegerInput("Please enter the number of vehicles (empty for default: 5, minimum: 1).", 1, 1);
                    if (gridSize != Integer.MIN_VALUE && vehicleCount != Integer.MIN_VALUE) {
                        HashMap<Integer, GridLocation> vehicleTargets = new HashMap<>();
                        for (int i = 0; i < vehicleCount; i++) {
                            int row = InputHelpers.getIntegerInput("Please enter the target row for vehicle " + i + " between 0 and " + (gridSize - 1) + " (inclusive)", -1, 0, gridSize - 1);
                            int column = InputHelpers.getIntegerInput("Please enter the target column for vehicle " + i + "  between 0 and " + (gridSize - 1) + " (inclusive)", -1, 0, gridSize - 1);
                            if (row != Integer.MIN_VALUE && column != Integer.MIN_VALUE) {
                                vehicleTargets.put(i, new GridLocation(row, column));
                            } else {
                                inputError = true;
                            }
                        }
                        if (!inputError) {
                            trafficControl.generateGrid(gridSize);
                            trafficControl.generateVehicles(vehicleCount, vehicleTargets);
                            inputChosen = true;
                        }

                    }
                    break;
                case "exit":
                    System.exit(0);
                    break;
                default:
                    inputError = true;
                    break;
            }

        }
        trafficControl.addFullTickListener(this);
        trafficControl.addRetirementListener(this);
        trafficControl.Go();

        //get a separate thread to listen to user input
        Thread inputThread = getThread();
        inputThread.start();
    }

    private Thread getThread() {
        Thread inputThread = new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                String input;
                INPUT_STATE state = INPUT_STATE.VALID;
                while (isThereActiveVehicle) {
                    if (state == INPUT_STATE.ERROR_MAIN) {
                        OutputHelpers.printInputInstructions(true);
                    } else if (state == INPUT_STATE.VALID || state == INPUT_STATE.EXITED_MANUAL) {
                        if (state == INPUT_STATE.VALID) {
                            @SuppressWarnings("unused")
                            int b = System.in.read();
                        }
                        OutputHelpers.printInputInstructions(false);
                    }
                    trafficControl.Stop();
                    if (state == INPUT_STATE.ERROR_MANUAL) {
                        input = "manual";
                    } else {
                        input = scanner.nextLine();
                    }
                    switch (input) {
                        case "resume":
                            OutputHelpers.printResume();
                            state = INPUT_STATE.VALID;
                            trafficControl.Go();
                            break;
                        case "exit":
                            OutputHelpers.printShutdown();
                            isThereActiveVehicle = false;
                            trafficControl.ShutDown();
                            System.exit(0);
                            break;
                        case "manual":
                            OutputHelpers.printNewTargetInstructions(state == INPUT_STATE.ERROR_MANUAL, vehicleCount, gridSize);
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
        // daemon so we don't get hung up on the input thread
        inputThread.setDaemon(true);
        return inputThread;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case TCEvents.ALL_VEHICLES_MOVED:
                trafficControl.Stop();
                gridStreamController.pushGrid(trafficControl.getGridSnapshot());
                try {
                    // let the frontend do its thing
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                trafficControl.Go();
                break;
            case TCEvents.ALL_VEHICLES_RETIRED:
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
