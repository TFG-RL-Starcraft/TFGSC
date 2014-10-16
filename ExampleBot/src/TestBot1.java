import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bwapi.*;
import bwta.*;

public class TestBot1{

    private Mirror mirror = new Mirror();

    private Game game;

    private Player self;

    private int workers; 
    
    public void run() {
        mirror.getModule().setEventListener(new DefaultBWListener() {
            @Override
            public void onUnitCreate(Unit unit) {
                System.out.println("New unit " + unit.getType());
            }

            @Override
            public void onStart() {
                game = mirror.getGame();
                self = game.self();
                workers = getWorkers();
                //Use BWTA to analyze map
                //This may take a few minutes if the map is processed first time!
                System.out.println("Analyzing map...");
                BWTA.readMap();
                BWTA.analyze();
                System.out.println("Map data ready");
                
                game.enableFlag(1); // This command allows you to manually control the units during the game.
                
            }

            @Override
            public void onFrame() {
                game.setTextSize(10);
                game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
                StringBuilder units = new StringBuilder("My units:\n");
                
               
                
                //iterate through my units
                for (Unit myUnit : self.getUnits()) {
                    units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");
                    
                    if (workers<=8){
	                    //if there's enough minerals, train an SCV or Drone or Probe
	                    if (myUnit.getType() == UnitType.Terran_Command_Center && self.minerals() >= 50) {
	                        myUnit.train(UnitType.Terran_SCV);
	                        workers++;
	                    } else if (myUnit.getType() == UnitType.Zerg_Hatchery && self.minerals() >= 50) {
	                        myUnit.train(UnitType.Zerg_Drone);
	                        workers++;
	                    } else if (myUnit.getType() == UnitType.Protoss_Nexus && self.minerals() >= 50) {
	                        myUnit.train(UnitType.Protoss_Probe);
	                        workers++;
	                    }	
                    }
                    
                    if(workers==9 && myUnit.getType().isWorker()){
                    	TilePosition t = self.getStartLocation().makeValid();
                    	if(self.getRace()==Race.Terran)	
                    		myUnit.build(t, UnitType.Terran_Supply_Depot);
                    	if(self.getRace()==Race.Zerg)	
                    		myUnit.build(t, UnitType.Zerg_Overlord);
                    	if(self.getRace()==Race.Protoss)	
                    		myUnit.build(t, UnitType.Protoss_Pylon);
                    		
                    }
                    	
                    
                    //if it's a drone and it's idle, send it to the closest mineral patch
                    if (myUnit.getType().isWorker() && myUnit.isIdle()) {
                        Unit closestMineral = null;

                        //find the closest mineral
                        for (Unit neutralUnit : game.neutral().getUnits()) {
                            if (neutralUnit.getType().isMineralField()) {
                                if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
                                    closestMineral = neutralUnit;
                                }
                            }
                        }

                        //if a mineral patch was found, send the drone to gather it
                        if (closestMineral != null) {
                            myUnit.gather(closestMineral, false);
                        }
                    }
                }

                //draw my units on screen
                game.drawTextScreen(10, 25, units.toString());
            }
        });

        mirror.startGame();
    }

    private int getWorkers(){
    	int numberOfWorkers = 0;
    	
    	for (Unit myUnit : self.getUnits()) {
    		if(myUnit.getType().isWorker())
    			numberOfWorkers++;
    	}
    	
    	return numberOfWorkers;
    }
    
    public static void main(String... args) {
        new TestBot1().run();
    }
}
