package starcraft;
import entrada_salida.IO_QTable;
import entrada_salida.Log;
import q_learning.Environment;
import q_learning.QLearner;
import q_learning.QTable;
import q_learning.QTable_Array;
import q_learning.State;
import bwapi.*;
import bwta.*;

public class Main_Starcraft{

    private Mirror mirror = new Mirror();

    private Game game;
    private Unit marine;
    private Player self;
    private QLearner q;
    
    private int numIter;

    public void run() {
        mirror.getModule().setEventListener(new DefaultBWListener() {
//            @Override
//            public void onUnitCreate(Unit unit) {
//                System.out.println("New unit " + unit.getType());
//            }

            @Override
            public void onStart() {
                game = mirror.getGame();
                self = game.self();

                //Use BWTA to analyze map
                //This may take a few minutes if the map is processed first time!
                System.out.println("Analyzing map...");
                BWTA.readMap();
                BWTA.analyze();
                
                getMarine();
                System.out.println("Map data ready");
                System.out.println("HEIGHT: " + game.mapHeight() + " WIDTH: " + game.mapWidth());
				System.out.println("MarineX: " + marine.getPosition().getX() / 32 + " MarineY: "
						+ marine.getPosition().getY() / 32);
				
				
				State ls = new StarcraftState(1, 1, game.mapWidth(), game.mapHeight());
				Environment e = new StarcraftEnvironment(game, marine, ls);
				
				QTable qT = IO_QTable.leerTabla("qtabla.txt");
				if(qT == null) {
					qT = new QTable_Array(e.numStates(), e.numActions());
				}
				q = new QLearner(e, qT);
				numIter = 0;
				
				game.setLocalSpeed(0);
				//game.setGUI(false);				
				
				
                game.enableFlag(1); // This command allows you to manually control the units during the game.
                
            }
 
            @Override
            public void onFrame() {
                game.setTextSize(10);
                game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());

//                StringBuilder units = new StringBuilder("My units:\n");
//
//                //iterate through my units
//                for (Unit myUnit : self.getUnits()) {
//                    units.append(myUnit.getType()).append(" ").append(myUnit.getTilePosition()).append("\n");
//
//                    //if there's enough minerals, train an SCV
//                    if (myUnit.getType() == UnitType.Terran_Command_Center && self.minerals() >= 50) {
//                        myUnit.train(UnitType.Terran_SCV);
//                    }
//
//                    //if it's a drone and it's idle, send it to the closest mineral patch
//                    if (myUnit.getType().isWorker() && myUnit.isIdle()) {
//                        Unit closestMineral = null;
//
//                        //find the closest mineral
//                        for (Unit neutralUnit : game.neutral().getUnits()) {
//                            if (neutralUnit.getType().isMineralField()) {
//                                if (closestMineral == null || myUnit.getDistance(neutralUnit) < myUnit.getDistance(closestMineral)) {
//                                    closestMineral = neutralUnit;
//                                }
//                            }
//                        }
//
//                        //if a mineral patch was found, send the drone to gather it
//                        if (closestMineral != null) {
//                            myUnit.gather(closestMineral, false);
//                        }
//                    }
//                }
//
//                //draw my units on screen
//                game.drawTextScreen(10, 25, units.toString());
            
            /*
             * Parte de nuestra aplicaci�n
             */
             
                // Si estamos en un nuevo estado (está quieto), ejecutamos un paso en el aprendizaje
                if(!marine.isMoving()) {
	                Action a = q.step();
	                numIter++;
                }
                
            }
            
	        @Override
	        public void onEnd(boolean isWinner) {
	    		System.out.println("END");
	    		Log.printLog("log.txt", Integer.toString(numIter));
	    		//q.endOfGame();
	    		IO_QTable.escribirTabla(q.qTable(), "qtabla.txt");
	    	}  
            
            
        });

        mirror.startGame();
    }
    
    private void getMarine() {
		for (Unit myUnit : self.getUnits()) {
			if (myUnit.getType() == UnitType.Terran_Marine) {
				marine = myUnit;
			}
		}
	}
    
    public static void main(String... args) {
        new Main_Starcraft().run();
    }
}