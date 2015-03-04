package q_learning.laberinto;

/**
 * Enum containing the possible actions in each state
 * Every action will have a value, which will be used to index the Qtable
 *
 */
public enum Action {

	MOVE_UP(0),
	MOVE_RIGHT(1),
	MOVE_DOWN(2),
	MOVE_LEFT(3);
	
    private final int value;
	
    Action(int value) { 
        this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
}
