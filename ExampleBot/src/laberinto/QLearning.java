package laberinto;

import java.util.ArrayList;

public class QLearning {

	private Casilla [][] tablero;
	private double [][][] Q; //para cada casilla, almacena la recompensa de cada posible estado
	private Casilla salida;
	private Casilla meta ;
	private Casilla casilla_actual;
	private int limX;
	private int limY;
	
	//enumerado de las posibles acciones
	private int MOVERSE_ARRIBA = 0;
	private int MOVERSE_DERECHA = 1;
	private int MOVERSE_ABAJO = 2;
	private int MOVERSE_IZQUIERDA = 3;
	
	
	public QLearning(Casilla [][] tablero, Casilla salida, Casilla meta, int limX, int limY)
    {
        this.tablero = tablero;
        this.limX = limX;
        this.limY = limY;
        this.salida = salida;
        this.meta = meta;
    }
	
	
	private void inicializaQ(int x, int y, int a)
	{
		Q = new double [x][y][a];
		
		for(int i=0; i<x; i++)
			for(int j=0; j<y; j++)
				for(int k=0; k<a; k++)
					Q[i][j][k] = 0;
					//Tambi�n se podr�a seguir la pol�tica de inicializar en aleatorios
					//Q[i][j][k] = Math.random();
	}
	
	
	/* PSEUDOCODIGO
	 * Inicializar la tabla Q(s,a) (con 0's o valores aleatorios)
	 * Repetir (por cada episodio)
	 * 		Observar o inicializar s
	 * 		Repetir (por cada paso del episodio, n veces)
	 * 			Seleccionar a para s actual mediante pol�tica derivada de Q (p ej. e-greedy)
	 * 			Ejecutar la acci�n a
	 * 			Observar es estado resultante s' y recompensa resultante r'
	 * 			Q(s,a) = Q(s,a) + alpha( r + landa * max a'(Q(s', a')) - Q(s,a) )
	 * 			s = s'
	 * 		hasta que s sea terminal
	 * 
	 * 
	 * A medida que el agente se mueve hacia delante desde un viejo estado a uno nuevo, Q-Learning propaga las estimaciones de Q hacia atr�s desde el nuevo estado al viejo.
	 * Aunque en teor�a el ciclo de Q-Learning se realiza infinitamente, en la pr�ctica el aprendizaje se realiza por episodios, que conluyen en aguna condici�n espec�fica o llegado un n�mero m�ximo de itearaciones.
	 * El factor alpha se denomina par�metro del tama�o del paso [0-1], que significa el valor de refuerzo que le damos al aprendizaje.
	 * 
	 * 
	 * Vamos a comenzar con una pol�tica simple, que consiste en que si llegamos a la meta, damos una gran recompensa
	 */
	public void aprendeLaberinto(int num_repeticiones)
	{ 
		double alpha = 0.1;
		double landa = 0.5;
		
		int acciones_posibles = 4;
        inicializaQ(limX, limY, acciones_posibles);
        for(int j=0; j<acciones_posibles; j++) //INICIALIZAMOS LOS COSTES DE LA META A 100
        	Q[meta.posX][meta.posY][j] = 100;
        
        //repetimos el experimento num_repeticiones veces
        for(int i=0; i<num_repeticiones; i++)
        {
        	//inicializamos en la salida
        	this.casilla_actual = this.salida;
        	int num_movimientos = 0;
        	
        	//mientras no lleguemos al final o no agotemos los movimientos
        	while(!this.casilla_actual.equals(this.meta) && num_movimientos < 100)
        	{
        		ArrayList<Sucesor> sucesores = generaSucesores(this.casilla_actual); //con la accion que los ha "generado"
        		Sucesor sucesor_elegido = sucesores.get(0); //inicializamos en el primer sucesor, pero cambiar�
        		
        		double Q_sa = Double.MIN_VALUE; 
        		double Q_sa_prima = Double.MIN_VALUE;
        		//elegimos la acci�n a realizar (sucesor) de todas las posibles, ya sea por el coste o aleatoriamente
        		//le asignamos un 80% de probabilidad de elegir una accion aleatoria
        		if(Math.random() <= 0.8) //TODO probar con varias probabilidades
        		{
        			int rand = (int) Math.floor(Math.random()*sucesores.size());
        			sucesor_elegido = sucesores.get(rand);
        		} 
        		else
        		{
        			for(int suc=0; suc<sucesores.size(); suc++)
        			{
        				//elige la accion con mayor recompensa Q en la tabla
        				if(Q[this.casilla_actual.posX][this.casilla_actual.posY][sucesores.get(suc).getAccion()] > Q_sa)
        				{
        					sucesor_elegido = sucesores.get(suc);
        				}
        			}
        		}
        		
        		//calculamos el coste actual Q(s,a) para la f�rmula
        		Q_sa = Q[this.casilla_actual.posX][this.casilla_actual.posY][sucesor_elegido.getAccion()];
        		//Calculamos el coste max a'(Q(s', a')) para la formula
        		for(int a_prima=0; a_prima<acciones_posibles; a_prima++)
    			{
    				//elige la accion con mayor recompensa Q para todas las a_prima
    				if(Q[sucesor_elegido.getCasilla_final().posX][sucesor_elegido.getCasilla_final().posY][a_prima] > Q_sa_prima)
    				{
    					Q_sa_prima = Q[sucesor_elegido.getCasilla_final().posX][sucesor_elegido.getCasilla_final().posY][a_prima];
    				}
    			}
        		
        		//ejecutamos la accion, y actualizamos Q
        		//Q(s,a) = Q(s,a) + alpha( r + landa * max a'(Q(s', a')) - Q(s,a) )
        		Q[this.casilla_actual.posX][this.casilla_actual.posY][sucesor_elegido.getAccion()] = Q_sa + alpha * (landa*Q_sa_prima - Q_sa);
        		this.casilla_actual = sucesor_elegido.getCasilla_final();
        		System.out.println(this.casilla_actual.getPosX() + ", " + this.casilla_actual.getPosY());
        	}
        	System.out.println("-----------------------------------VUELTA TERMINADA----------------------");
        }
        
        System.out.println("-------- APRENDIDO --------");
        
        for(int i = 0; i < limX ; i++)
        {
            for(int j = 0; j < limX;j++)
            {
            	tablero[i][j].setText(Double.toString(Q[i][j][0]) + " " + Double.toString(Q[i][j][1]) + " " + Double.toString(Q[i][j][2]) + " " + Double.toString(Q[i][j][3]));
            }
        }
	}
	
	public void buscaMejorCamino()
	{
		
	}
	
	/**
     * Devuelve todos los posibles sucesores de una casilla
     * Evitando devolver las casillas fuera del tablero
     * @param c
     * @return 
     */
    public ArrayList<Sucesor> generaSucesores(Casilla c) //solo cogemos 4 (up, down, left, right)
    {
        ArrayList<Sucesor> sucesores = new ArrayList<Sucesor>();
        
        if((c.getPosY()-1 >=0) &&
                !tablero[c.getPosX()][c.getPosY()-1].esPared()) //00 - MOVERSE_ARRIBA
        {
        	sucesores.add(new Sucesor(tablero[c.getPosX()][c.getPosY()-1], MOVERSE_ARRIBA));
        }
        
        if((c.getPosX()+1 < limX) &&
                !tablero[c.getPosX()+1][c.getPosY()].esPared()) //01 - MOVERSE_DERECHA
        {
        	sucesores.add(new Sucesor(tablero[c.getPosX()+1][c.getPosY()], MOVERSE_DERECHA));
        }
        
        if((c.getPosY()+1 < limY) &&
                !tablero[c.getPosX()][c.getPosY()+1].esPared()) //02 - MOVERSE_ABAJO
        {
        	sucesores.add(new Sucesor(tablero[c.getPosX()][c.getPosY()+1], MOVERSE_ABAJO));
        }
        
        if((c.getPosX()-1 >=0) &&
                !tablero[c.getPosX()-1][c.getPosY()].esPared()) //03 - MOVERSE_IZQUIERDA
        {
        	sucesores.add(new Sucesor(tablero[c.getPosX()-1][c.getPosY()], MOVERSE_IZQUIERDA));
        }


        return sucesores;
    }

}

