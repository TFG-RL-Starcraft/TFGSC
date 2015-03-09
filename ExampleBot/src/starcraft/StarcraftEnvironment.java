package starcraft;

import q_learning.Environment;
import q_learning.State;
import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class StarcraftEnvironment implements Environment{

	private static int BOX_LENGTH = 32;
	
	private Game game;
	private Unit unit;
	private State state;
	private State lastState;
	
	public StarcraftEnvironment(Game game, Unit unit, State lastState) {
		this.game = game;
		this.unit = unit;
		this.state = new StarcraftState((int)unit.getPosition().getX()/BOX_LENGTH, 
				(int)unit.getPosition().getY()/BOX_LENGTH, game.mapWidth(), game.mapHeight());
		this.lastState = lastState;
	}
	
	@Override
	public int numStates() {
		return game.mapHeight() * game.mapWidth();
	}

	@Override
	public int numActions() {
		return Action.values().length;
	}

	@Override
	public double execute(Action action) {
		
		double reward = 0;
		 
		// Current position
		int posX = (int)unit.getPosition().getX()/BOX_LENGTH;
		int posY = (int)unit.getPosition().getY()/BOX_LENGTH;
		
		String action_str = "";
		
		switch(action) {
		 case MOVE_UP: 
			 posY--;
			 action_str = "ARRIBA";
		     break;
		 case MOVE_RIGHT: 
			 posX++;
			 action_str = "DERECHA";
		     break;
		 case MOVE_DOWN:
			 posY++;
			 action_str = "ABAJO";
		     break;
		 case MOVE_LEFT:
			 posX--;
			 action_str = "IZQUIERDA";
		     break;
		 default: 
			 
			 break;
		}
/*
        if(new_p.isValid()){
        	unit.move(new_p);
			System.out.println(action_str + " - " + unit.getPosition().getX() / 32 + " "+ unit.getPosition().getY() / 32);
		}
        
        if (unit.isStuck())
			System.out.println("STUCK");*/
				
		//System.out.println("X: " + unit.getPosition().getX() / 32 + "(" + unit.getPosition().getX() + ") Y: "+ unit.getPosition().getY() / 32 + "(" + unit.getPosition().getY() + ")");
		//System.out.println("Accion a tomar: " + action_str);
		unit.move(new Position(posX*BOX_LENGTH, posY*BOX_LENGTH));
		
		
			 
		// This is a switch evaluating if the new State/Action would have a good/bad Reward
		
		if (isValid(posX, posY)) {
			state = new StarcraftState(posX, posY, game.mapWidth(), game.mapHeight());
			if(finalState()) {
				reward = 1000;
			} /*else {
				reward = -0.1;
			}*/
			
		} /*else {
			reward = -10;
		}*/

		return reward;
	}

	@Override
	public State state() {
		return state;
	}
	
	@Override
	public State lastState() {
		return lastState;
	}

	@Override
	public boolean finalState() {
		return state.getValue() == lastState.getValue();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	// The position x,y is valid
	private boolean isValid(int x, int y) {
		Position P = new Position(x,y);
		
		if(P.isValid()) {
			return (0 <= x) && (x < game.mapWidth()) &&
					(0 <= y) && (y < game.mapHeight());
		} else {
			return false;
		}
	}

}
