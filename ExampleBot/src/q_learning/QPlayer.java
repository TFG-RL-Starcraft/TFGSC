package q_learning;

import starcraft.Action;

public class QPlayer {

	private Environment environment;
	private QTable qTable;
	
	public QPlayer(Environment environment, QTable qTable)
	{
		this.environment = environment;
		this.qTable = qTable;
	}
	
	// Executes one step in the learning process
	public Action step()
	{     
		State state = environment.state();

		// Choose action
		Action action = getAction(state);

		// Execute action
		environment.execute(action);

		return action;
	}
	
	// Choose the best valued action between possible
	private Action getAction(State state) {

		return qTable.bestAction(state);
	}
	
}