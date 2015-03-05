package q_learning;

import starcraft.Action;

public class QLearner {

	private static double ALPHA = 0.2;
	private static double GAMMA = 0.99;
	private static double RANDOM_ACTION_PROB = 0.1;
	
	private Environment environment;
	private QTable qTable;
	
	public QLearner(Environment environment, QTable qTable)
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
		double reward = environment.execute(action);
		State newState = environment.state();
		
		// Update Q-Table
		//Q(s,a) = Q(s,a) + alpha( r + gamma * max a'(Q(s', a')) - Q(s,a) )
		double newValue = qTable.get(state, action.getValue()) + ALPHA * (reward + GAMMA * qTable.bestQuantity(newState) - qTable.get(state, action.getValue()));
		newValue = Math.max(0, newValue); //TODO, ver si tiene sentido este max
		qTable.set(state, action, newValue);	
		
		return action;
	}
	
	// Choose a random action considering the probabilities of each
	private Action getAction(State state) {
		double total = 0;
		for (int a = 0; a<environment.numActions(); a++) {
			total += qTable.get(state, a);
		}

		double random = Math.random() * total;

		total = 0;
		for (int a = 0; a<environment.numActions(); a++) {
			total += qTable.get(state, a);
			if (total >= random)
				return Action.get(a);
		}

		return null;
	}

}
