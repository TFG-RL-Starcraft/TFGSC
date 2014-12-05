
public interface World {
	
	// Possible states
	public int numStates();
	
	// Possible actions
	public int numActions();
	
	// Executes an action in the current state
	// Returns the reward
	public double execute(int action);
	
	// Returns current state
	public int state();
	
	// current state is final?
	public boolean finalState();
	
	// reset to initial world state
	public void reset();
}
