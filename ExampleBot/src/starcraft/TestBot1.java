package starcraft;
import q_learning.laberinto.Action;
import q_learning.laberinto.Environment;
import q_learning.laberinto.QLearner;
import q_learning.laberinto.QTable;
import q_learning.laberinto.QTable_Array;
import q_learning.laberinto.StarcraftEnvironment;
import q_learning.laberinto.StarcraftState;
import q_learning.laberinto.State;
import bwapi.*;
import bwta.*;

public class TestBot1{

    private Mirror mirror = new Mirror();

    private Game game;
    private Unit marine;
    private Player self;
    private QLearner q;

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
                System.out.println(game.mapHeight() + " " + game.mapWidth());
				System.out.println(marine.getPosition().getX() / 32 + " "
						+ marine.getPosition().getY() / 32);
				
				
				State s = new StarcraftState(1, 1, game.mapHeight(), game.mapWidth());
				Environment e = new StarcraftEnvironment(game, marine, s);

				QTable qT = new QTable_Array(e.numStates(), e.numActions());
				// q = new QLearner(w,qT);

				q = new QLearner(e, qT);

				//game.setLocalSpeed(0);
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
             * Parte de nuestra aplicación
             */
                Action a = q.step();
                Position p = new Position(marine.getPosition().getX(),marine.getPosition().getY());
                String direction = "";
                
                switch(a) {
                	case MOVE_UP: 
					p = new Position(marine.getPosition().getX(),marine.getPosition().getY() - 32);
					direction = "ARRIBA";
					break;
	       		 case MOVE_RIGHT: 
	       			p = new Position(marine.getPosition().getX() + 32, marine.getPosition().getY());
	       			direction = "DERECHA";
	       		    break;
	       		 case MOVE_DOWN:
	       			p = new Position(marine.getPosition().getX(),marine.getPosition().getY() + 32);
	       			direction = "ABAJO";
	       		    break;
	       		 case MOVE_LEFT:
					p = new Position(marine.getPosition().getX() - 32, marine.getPosition().getY());
					direction = "IZQUIERDA";
					break;
	       		 default: 
	   			 
	   			 break;
                }
                
                if(p.isValid()){
					marine.move(p);
					System.out.println(direction + " - " + marine.getPosition().getX() / 32 + " "+ marine.getPosition().getY() / 32);
				}
                
                if (marine.isStuck())
					System.out.println("STUCK");
                
            }
            
	        @Override
	        public void onEnd(boolean isWinner) {
	    		System.out.println("END");
	    		//q.endOfGame();
	    		//Fichero.escribirTabla(q.qTable());
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
        new TestBot1().run();
    }
}
