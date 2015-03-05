package aprendizaje;

import starcraft.Action;

/**
 * Interface to be implemented by classes that want to learn QLearning.
 * This interface provides a basis for methods to abstract the QLearning algorithm.
 *
 */
public interface Environment {

	// Possible states
	public int numStates();
	
	// Possible actions
	public int numActions();
	
	// Executes an action in the current state
	// Returns the reward
	public double execute(Action action);
	
	// Returns current state
	public State state();
	
	// Last state
	public State lastState();
	
	// Current state is final?
	public boolean finalState();
	
	// Reset to initial world state
	public void reset();
}
