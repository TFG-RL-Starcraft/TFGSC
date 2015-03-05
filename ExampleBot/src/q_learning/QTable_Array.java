package q_learning;

import starcraft.Action;

public class QTable_Array implements QTable {

	private double[][] table;
	
	public QTable_Array(int numStates, int numActions) {
		table = new double[numStates][numActions];
		clear();
	}
	
	@Override
	public void clear() {
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[i].length; j++) {
				table[i][j] = 1;
			}
		}
		// Could also follow the random initialization policy  
		// Q[i][j] = Math.random();
		// This causes unpredictable actions in the exploring paths selection
	}

	@Override
	public double get(State state, int a) {
		return table[state.getValue()][a];
	}

	@Override
	public void set(State state, Action action, double quantity) {
		table[state.getValue()][action.getValue()] = quantity;
	}

	@Override
	public double bestQuantity(State state) {
		double best = table[state.getValue()][0];
		for (int i=1; i<table[state.getValue()].length; i++) {
			best = Math.max(best, table[state.getValue()][i]);
		}
		return best;
	}

	@Override
	public Action bestAction(State state) {
		int best_index = 0;
		double best = table[state.getValue()][best_index];
		
		for (int i=1; i<table[state.getValue()].length; i++) {
			if(best<table[state.getValue()][i]) {
				best = table[state.getValue()][i];
				best_index = i;
			}
		}
		
		return Action.get(best_index);
	}

}
