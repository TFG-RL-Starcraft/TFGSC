package q_learning.laberinto;

/**
 * Interface to the structure that contains the "table" Q(s,a) -> Quantity(state, action)
 *
 */
public interface QTable {

	// Clear the Quantities table
	public void clear();
	
	// Returns the Quantity of a pair (state, action)
	public double get(State state, Action action);
	
	// Set the Quatity of a pair (state, action)
	public void set(State state, Action action, double quantity);

	// the best Quantity of all the possible actions in a given state
	public double bestQuantity(State state);
}
