package q_learning.laberinto;

import java.util.ArrayList;

public class QLearner {

	private static double ALPHA = 0.2;
	private static double LANDA = 0.99;
	private static double PROB_ACCION_ALEATORIA = 0.1;
	
	private Environment entorno;
	private QTable qTable;
	
	public QLearner(Environment entorno, QTable qTable)
	{
		this.entorno = entorno;
		this.qTable = qTable;
	}
	
	// Executes the learning process until the number of iterations or end state
	public int run(int num_iter_max)
	{     
		r = new double [limX][limY]; //inicializa r
        r[meta.getPosX()][meta.getPosY()] = 100; //inicializamos la recompensa de llegar a la meta a 100
        
        //inicializamos en el estado inicial
    	State estado = entorno.getEstadoInicial();
    	int num_iter = 0;
        
    	//repetimos el experimento num_iter_max veces, o hasta que llegue al estado final
    	while(!estado.equals(entorno.getEstadoFinal()) && num_iter < num_iter_max)
    	{
    		// Elige una Accion
    		Action accion = getAction(estado);
	
    		// Ejecutamos la accion
    		entorno.run(accion);
    		this.casilla_actual = sucesor_elegido.getCasilla_final();
    		
    		// Actualizamos Q
    		//Q(s,a) = Q(s,a) + alpha( r + landa * max a'(Q(s', a')) - Q(s,a) )
    		updateQ();
    		  		
    		num_iter++;
    	}

    	System.out.println("-------- ¡¡¡APRENDIDO!!! --------");
        
        // Almacena la tabla Q en persistencia (fichero)
//    	for(int i = 0; i < limX ; i++)
//        {
//            for(int j = 0; j < limX;j++)
//            {
//            	tablero[i][j].setText("UP: " + Double.toString(Q[i][j][0]) + ", RIGHT: " + Double.toString(Q[i][j][1]) + ", DOWN: " + Double.toString(Q[i][j][2]) + ", LEFT: " + Double.toString(Q[i][j][3]));
//            }
//        }
		
		return num_iteraciones;
	}
	
	private void updateQ() {
		//calculamos el coste actual Q(s,a) para la fórmula
		Q_sa = qTable[estado][accion.getValue()];
		//Calculamos el coste max a'(Q(s', a')) para la formula
		for(int a_prima=0; a_prima<acciones_posibles; a_prima++)
		{
			//elige la accion con mayor recompensa Q para todas las a_prima
			if(qTable[estado][a_prima] > Q_sa_prima)
			{
				Q_sa_prima = qTable[sucesor_elegido.getCasilla_final().getPosX()][sucesor_elegido.getCasilla_final().getPosY()][a_prima];
			}
		}
		
		
		qTable[estado][accion.getValue()] = Q_sa + ALPHA * (r[sucesor_elegido.getCasilla_final().getPosX()][sucesor_elegido.getCasilla_final().getPosY()] + LANDA*Q_sa_prima - Q_sa);
	}

	private Action getAction(State state) {
		double Q_sa = Double.MIN_VALUE; 
		double Q_sa_prima = Double.MIN_VALUE;

		//le asignamos una probabilidad de elegir una accion totalmente aleatoria
		if(Math.random() <= PROB_ACCION_ALEATORIA)
		{
			int rand = (int) Math.floor(Math.random()*sucesores.size());
			sucesor_elegido = sucesores.get(rand);
		} 
		else //SI NO, cogemos un sucesor teniendo en cuenta los costes
			//dando más probabilidad a los que tengan más coste, pero generando un número al azar de todas formas
		{
//System.out.println("CASILLA (" + this.casilla_actual.getPosX() + "," + this.casilla_actual.getPosY() + ") con " + sucesores.size() + " sucesores");
			double probabilidad_total = 0;
			for (int suc = 0; suc<sucesores.size(); suc++)
			{
				probabilidad_total = probabilidad_total + qTable[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion().getValue()];
//System.out.print("Q acc:" + sucesores.get(suc).getAccion() + " " + Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion()] + " - ");
			}
//System.out.println();
			
			double rand = Math.random() * probabilidad_total;
			
			double probabilidad_suma = 0;
			for (int suc = 0; suc<sucesores.size(); suc++)
			{
				probabilidad_suma = probabilidad_suma + qTable[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion().getValue()];
				if (sucesor_elegido == null && probabilidad_suma >= rand)
				{
					sucesor_elegido = sucesores.get(suc);
				}
			}
			
//System.out.println("prob_total: " + probabilidad_total + ", rand: " + rand + ", opc_elegida: " + sucesor_elegido.getAccion());
    		
		}
		
		return null;
	}

}
