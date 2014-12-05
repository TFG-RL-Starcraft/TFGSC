
public class QTable {
	
	private double[][] table;
	
	public QTable(int numStates, int numActions) {
		table = new double[numStates][numActions];
		clear();
	}
	
	public void clear() {
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[i].length; j++) {
				table[i][j] = 1;
			}
		}
	}
	
	public double get(int state, int action) {
		return table[state][action];
	}
	
	public void set(int state, int action, double reward) {
		table[state][action] = reward;
	}

	public double bestReward(int state) {
		double best = table[state][0];
		for (int i=1; i<table[state].length; i++) {
			best = Math.max(best, table[state][i]);
		}
		return best;
	}
}
