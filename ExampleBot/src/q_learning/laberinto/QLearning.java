package q_learning.laberinto;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import q_learning.Casilla;

public class QLearning {

	private Casilla [][] tablero;
	private double [][][] Q; //para cada estado (casilla), almacena el aprendizaje por cada acci�n posible
	private double [][] r; //para cada estado almacena una recompensa (s�lo tendr� recompensa la meta)
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
					Q[i][j][k] = 1;
					//Tambi�n se podr�a seguir la pol�tica de inicializar en aleatorios
					//Q[i][j][k] = Math.random();
	}
	
	/* TODO cambiar el c�culo para utilizar una recompensa R, que se almacenar� por cada estado */
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
		double alpha = 0.2;
		double landa = 0.99;
		
		int acciones_posibles = 4;
        inicializaQ(limX, limY, acciones_posibles);
        r = new double [limX][limY]; //inicializa r
        r[meta.getPosX()][meta.getPosY()] = 100; //inicializamos la recompensa de llegar a la meta a 100
        
        //repetimos el experimento num_repeticiones veces
        for(int i=0; i<num_repeticiones; i++)
        {
        	long start_rep = System.currentTimeMillis();
        	
        	//inicializamos en la salida
        	this.casilla_actual = this.salida;
        	int num_movimientos = 0;
        	
        	//mientras no lleguemos al final o no agotemos los movimientos
        	while(!this.casilla_actual.equals(this.meta) ) //&& num_movimientos < (limX*limY))
        	{
        		ArrayList<Sucesor> sucesores = generaSucesores(this.casilla_actual); //con la accion que los ha "generado"
        		Sucesor sucesor_elegido = null;
        		
        		double Q_sa = Double.MIN_VALUE; 
        		double Q_sa_prima = Double.MIN_VALUE;
        		//elegimos la acci�n a realizar (sucesor) de todas las posibles, ya sea por el coste o aleatoriamente
        		//le asignamos un 10% de probabilidad de elegir una accion totalmente aleatoria
        		if(Math.random() <= 0.1) //TODO probar con varias probabilidades
        		{
        			int rand = (int) Math.floor(Math.random()*sucesores.size());
        			sucesor_elegido = sucesores.get(rand);
        		} 
        		else //SI NO, cogemos un sucesor teniendo en cuenta los costes
        			//dando m�s probabilidad a los que tengan m�s coste, pero generando un n�mero al azar de todas formas
        		{
       //System.out.println("CASILLA (" + this.casilla_actual.getPosX() + "," + this.casilla_actual.getPosY() + ") con " + sucesores.size() + " sucesores");
        			double probabilidad_total = 0;
        			for (int suc = 0; suc<sucesores.size(); suc++)
        			{
        				probabilidad_total = probabilidad_total + Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion()];
        //System.out.print("Q acc:" + sucesores.get(suc).getAccion() + " " + Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion()] + " - ");
        			}
        //System.out.println();
        			
        			double rand = Math.random() * probabilidad_total;
        			
        			double probabilidad_suma = 0;
        			for (int suc = 0; suc<sucesores.size(); suc++)
        			{
        				probabilidad_suma = probabilidad_suma + Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion()];
        				if (sucesor_elegido == null && probabilidad_suma >= rand)
    					{
        					sucesor_elegido = sucesores.get(suc);
    					}
        			}
        			
        //System.out.println("prob_total: " + probabilidad_total + ", rand: " + rand + ", opc_elegida: " + sucesor_elegido.getAccion());
            		
        		}
        		
        /*		
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */		
        		//calculamos el coste actual Q(s,a) para la f�rmula
        		Q_sa = Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesor_elegido.getAccion()];
        		//Calculamos el coste max a'(Q(s', a')) para la formula
        		for(int a_prima=0; a_prima<acciones_posibles; a_prima++)
    			{
    				//elige la accion con mayor recompensa Q para todas las a_prima
    				if(Q[sucesor_elegido.getCasilla_final().getPosX()][sucesor_elegido.getCasilla_final().getPosY()][a_prima] > Q_sa_prima)
    				{
    					Q_sa_prima = Q[sucesor_elegido.getCasilla_final().getPosX()][sucesor_elegido.getCasilla_final().getPosY()][a_prima];
    				}
    			}
        		
        		//ejecutamos la accion, y actualizamos Q
        		//Q(s,a) = Q(s,a) + alpha( r + landa * max a'(Q(s', a')) - Q(s,a) )
        		Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesor_elegido.getAccion()] = Q_sa + alpha * (r[sucesor_elegido.getCasilla_final().getPosX()][sucesor_elegido.getCasilla_final().getPosY()] + landa*Q_sa_prima - Q_sa);
        		this.casilla_actual = sucesor_elegido.getCasilla_final();
        		
        		num_movimientos++;
        	}
        	
        	//if(i%100 == 0 || i<100)
        		//System.out.println("REPETICION " + i + " TERMINADA en " + num_movimientos + " movs");
        }
        
        System.out.println("-------- ���APRENDIDO!!! --------");
        
        for(int i = 0; i < limX ; i++)
        {
            for(int j = 0; j < limX;j++)
            {
            	tablero[i][j].setText("UP: " + Double.toString(Q[i][j][0]) + ", RIGHT: " + Double.toString(Q[i][j][1]) + ", DOWN: " + Double.toString(Q[i][j][2]) + ", LEFT: " + Double.toString(Q[i][j][3]));
            }
        }
	}
	
	public void buscaMejorCamino()
	{
		this.casilla_actual = this.salida;
		while(!this.casilla_actual.equals(this.meta))
		{
			//Busca cual es la acci�n con mayor Q
			int acc_elegida = -1;
			Casilla cas_final = null;
			double Q_max = Double.MIN_VALUE;
			ArrayList<Sucesor> sucesores = generaSucesores(this.casilla_actual);
			
			for(int suc=0; suc<sucesores.size(); suc++)
			{
				double Q_act = Q[this.casilla_actual.getPosX()][this.casilla_actual.getPosY()][sucesores.get(suc).getAccion()];
				if(Q_act > Q_max)
				{
					Q_max = Q_act;
					acc_elegida = sucesores.get(suc).getAccion();
					cas_final = sucesores.get(suc).getCasilla_final();
				}
			}
			
			//ejecuta esa acci�n (cambia a esa casilla), y pinta la casilla de amarillo.
			this.casilla_actual = cas_final;
			this.casilla_actual.setBackground(Color.yellow);
		}
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


