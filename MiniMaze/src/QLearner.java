
public class QLearner {
	
	public static final double ALPHA = 0.1f;
	public static final double GAMMA = 0.1f;
	
	private World world;
	private QTable qTable;
	
	public QLearner(World world) {
		this.world = world;
		qTable = new QTable(world.numStates(), world.numActions());
	}
	
	public void reset() {
		qTable.clear();
	}
	
	public QTable qTable() {
		return qTable;
	}
	
	private int getAction(int state) {
		double total = 0;
		for (int a = 0; a<world.numActions(); a++) {
			total += qTable.get(state, a);
		}

		double random = Math.random() * total;

		total = 0;
		for (int a = 0; a<world.numActions(); a++) {
			total += qTable.get(state, a);
			if (total >= random)
				return a;
		}

		return -1;
	}
	
	public int run(int maxSteps) {
		int steps = 0;
		
		// Initial state
		world.reset();
		int state = world.state();
	
		// Loop
		while (!world.finalState() && (steps < maxSteps)) {
			steps++;
			
			// Choose action
			int action = getAction(state);

			// Execute action
			double reward = world.execute(action);
			int newState = world.state();

			// Update Q-Table
			double newValue = (1-ALPHA) * qTable.get(state, action) + ALPHA * (reward + GAMMA * qTable.bestReward(newState));
			newValue = Math.max(0, newValue);
			qTable.set(state, action, newValue);

			// Update state
			state = newState;
		}

		return steps;
	}
	
	public void run(int iterations, int maxSteps) {
		for (int i=0; i<iterations; i++)
			run(maxSteps);
	}
}
