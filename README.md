# KoneCranes home assignment

## 1. Tech stack
 - Java 11
 - Spring/Maven
 - HTML/JavaScript/CSS

## 2. Software Description
This software simulates an arbitrary number of vehicles moving on a given grid. Vehicles will start from the start of
the grid, then move towards a target that is either randomly assigned, or given as input. Once they reach their 
target, they return home and retire. Once all vehicles have been retired, the program finishes. The user can, at any 
point, stop the simulation to a give a new target to a vehicle.

## 3A. Software architecture - main logic
### FlowControl
- main controller of the software flow
- handles initial setup
- handles ad-hoc stopping of the simulation
- initializes TrafficControl
### TrafficControl
- singleton main class responsible for coordinating the simulation
- initializes the grid and the vehicles, and assigns targets to them
- handles the stop/go function of the vehicle threads
- handles move requests from vehicles
- emits events when all vehicles have moved one tick, and when all vehicles have been retired
## 3B - Software architecture - data classes
### Grid
- singleton class to store the current grid
- provides random location as vehicle if needed
- handles move requests coming in from TrafficControl
### GridLocation
- a single location from the grid
- may know if it's a target, and knows the amount of vehicles it has on it
### Vehicle
- between initialization and retirement, continuously tries moving towards its target
- gets Go/Stop signal from TrafficController
- sends move desire up to TrafficController
- knows its current location, current target, and origin point
## 3C - Software architecture - Helpers
### Helper
- mostly a collection of long system.out lines
- handles and validates integer inputs
### VehicleInput
- handles/validates the different possible syntaxes for manual vehicle inputs when added from ad-hoc menu
### GridDTO, GridLocationDTO
- extracting various data from Grid and GridLocation into a JSON-friendly format, to be passed along to the front-end
## 3D - Software architecture - misc
### WebApp
- program entry point, starts FlowControl
- has a Console parser as a Bean
### GridStreamController
- creates emitter for Server events
- sends Grid DTO through it
### index.html, app.js
- handle front-end logic
- draw the grid as a table on first event
- update/redraw vehicle cells on every tick

## 4. Software Flow
1. WebApp starts FlowControl
2. FlowControl sets up everything
   1. asks for user input on generation method (auto/basic/manual), initializes TrafficControl with given parameters
   2. after TrafficControl is started, starts a separate input thread that waits for ad-hoc user input
3. TrafficControl generates everything and handles the first cycle
   1. generates grid at desired size
   2. generates desired vehicle count with random or manual targets
   3. starts every vehicle on a separate thread, and the first cycle begins
   4. vehicles start moving and update TrafficControl when they've done one step
   5. after every vehicle moved, TrafficControl fires of the ALL_VEHICLES_MOVED event
4. FlowControl tells GridStreamController to push a new grid to the front-end, tells TrafficControl to continue
5. TrafficControl completes another cycle and tells FlowControl
6. Repeat until all vehicles are done or software is stopped ad-hoc
   1. in ad-hoc menu, FlowControl stops TrafficControl, and lets the user resume/exit/give a new target to a vehicle
   2. after new target or resume, FlowControl will let TrafficControl go, and software will continue as normal 
   from step 5