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

	/*
	 * Esta sería otra política para elegir la acción a realizar
	 */
//	private Action getAction(int state) {
//		double Q_sa = Double.MIN_VALUE; 
//		double Q_sa_prima = Double.MIN_VALUE;
//
//		//le asignamos una probabilidad de elegir una accion totalmente aleatoria
//		if(Math.random() <= RANDOM_ACTION_PROB)
//		{
//			int rand = (int) Math.floor(Math.random()*sucesores.size());
//			sucesor_elegido = sucesores.get(rand);
//		} 
//		else //SI NO, cogemos un sucesor teniendo en cuenta los costes
//			//dando más probabilidad a los que tengan más coste, pero generando un número al azar de todas formas
//		{
//
//			double probabilidad_total = 0;
//			for (int suc = 0; suc<sucesores.size(); suc++)
//			{
//				probabilidad_total = probabilidad_total + qTable[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion().getValue()];
//			}
//			
//			double rand = Math.random() * probabilidad_total;
//			
//			double probabilidad_suma = 0;
//			for (int suc = 0; suc<sucesores.size(); suc++)
//			{
//				probabilidad_suma = probabilidad_suma + qTable[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion().getValue()];
//				if (sucesor_elegido == null && probabilidad_suma >= rand)
//				{
//					sucesor_elegido = sucesores.get(suc);
//				}
//			}
//		}
//		
//		return null;
//	}
	
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
