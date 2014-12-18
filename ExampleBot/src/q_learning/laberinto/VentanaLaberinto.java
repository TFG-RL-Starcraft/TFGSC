package q_learning.laberinto;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

import q_learning.Casilla;

@SuppressWarnings("serial")
public class VentanaLaberinto extends javax.swing.JFrame {
	
	//Variables creadas por m�, para generar la ventana
    public static final int xboton = 30; //ancho y alto de las celdas
    public static final int yboton = 30;
    public static final int xInicio = 150; //posicio de comienzo del tablero
    public static final int yInicio = 50;
    
    public static final int VACIO = 0;
    public static final int PARED = 1;
    public static final int INICIO = 2;
    public static final int META = 3;
    
    private Casilla tablero[][]; //arraylist de JButtons para crear el tablero
    private Casilla salida;
    private Casilla meta;   
    
    private QLearning q; //guarda la referencia a toda la estructura del ejercicio
    
    int maxX = 15; //casillas m�ximas en horizontal y vertical
    int maxY = 15;
    
    // Variables declaration - do not modify
    private javax.swing.JButton btEmpezar;
    private javax.swing.JButton btCargarLaberinto;
   // End of variables declaration
	
    public VentanaLaberinto() {
        initComponents();

    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {

        btEmpezar = new javax.swing.JButton();
        btCargarLaberinto = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btEmpezar.setText("Empezar");
        btEmpezar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmpezarActionPerformed(evt);
            }
        });
        btEmpezar.setBounds(10, 10, 105, 30);
        btEmpezar.setVisible(true);
        this.add(btEmpezar);
        
        btCargarLaberinto.setText("Cargar Laberinto");
        btCargarLaberinto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	btCargarLaberintoActionPerformed(evt);
            }
        });
        btCargarLaberinto.setBounds(115, 10, 150, 30);
        btCargarLaberinto.setVisible(true);
        this.add(btCargarLaberinto);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        this.setSize(800, 600);
        
        generarLaberinto();
        
        q = new QLearning(tablero, salida, meta, maxX, maxY); //INICIALIZA LA ESTRUCTURA PARA EL ALGORITMO
        
    }

    
    private void btEmpezarActionPerformed(java.awt.event.ActionEvent evt) {
        long start = System.currentTimeMillis();
        
        q.aprendeLaberinto(10000);
        
        long end = System.currentTimeMillis();
        long res = end - start;
        System.out.println("TIEMPO DE EJECUCI�N: " + res/1000.0 + "segs.");
        
        q.buscaMejorCamino();
    }
    
    private void btCargarLaberintoActionPerformed(ActionEvent evt) {
    	//borramos el laberinto anterior
    	for(int j = 0; j < maxY;j++)
        {
            for(int i = 0; i < maxX ; i++)
            {
            	this.tablero[i][j].setEnabled(false);
            	this.tablero[i][j].setVisible(false);
            	this.tablero[i][j] = null;
            }            
        }
    	
    	//leemos el fichero y generamos el nuevo laberinto
    	FileReader fichero = null;
    	BufferedReader br = null;
        try
        {
            fichero = new FileReader("laberinto.txt");
            br = new BufferedReader(fichero);
 
            String linea;
            linea=br.readLine();

            //generamos un tablero de las dimensioes de la primera linea
            String dim[] = linea.split(",");
            maxX = Integer.parseInt(dim[0]);
            maxY = Integer.parseInt(dim[1]);
            
            generarLaberinto(); //aqu� genera el tablero y vuelve a dar valor a las variables locales
            
            //generamos el resto de casillas
            int cont_Y = 0;
            while((linea=br.readLine())!=null)
            {
            	String cas[] = linea.split(",");
            	for(int i = 0; i < maxX ; i++)
            	{
            		if(Integer.parseInt(cas[i]) == PARED)
            		{
                        setPared(this.tablero[i][cont_Y]);
            		}
                	else if(Integer.parseInt(cas[i]) == INICIO)
                	{
                		salida = this.tablero[i][cont_Y];
                        setSalida(salida);
                	}
                	else if(Integer.parseInt(cas[i]) == META)
                	{
                		meta = this.tablero[i][cont_Y];
                        setMeta(meta);
                	}

            	}
            	cont_Y++;
            }
            
            q = new QLearning(tablero, salida, meta, maxX, maxY); //INICIALIZA LA ESTRUCTURA PARA EL ALGORITMO
               
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
		
	}


    private void generarLaberinto() {

        tablero = new Casilla [maxX][maxY];
        
        /* Inicializa las casillas visualmente y las añade a la ventana*/
        for(int i = 0; i < maxX ; i++)
        {
            for(int j = 0; j < maxY;j++)
            {
                tablero[i][j] = new Casilla(i,j);
                tablero[i][j].setBounds(xboton*i+xInicio, yboton*j+yInicio, xboton, yboton);
                tablero[i][j].setVisible(true);
                tablero[i][j].setBackground(Color.WHITE);              
                this.add(tablero[i][j]);
                tablero[i][j].addActionListener(new ActionListener() 
                {
                    public void actionPerformed(ActionEvent e) {
                        Casilla c = (Casilla)e.getSource();
                        System.out.println(c.getText());
                    }
                });
            }
        }

        repaint();
    }
    
    
    public void setSalida(Casilla salida)
    {
    	salida.setInicio();
    	salida.setBackground(Color.blue);
        
        this.repaint();
    }
    
    
    public void setMeta(Casilla meta)
    {
    	meta.setMeta();
    	meta.setBackground(Color.green);
        
        this.repaint();
    }
    

    public void setPared(Casilla pared)
    {    
    	pared.setPared(true);
    	pared.setBackground(Color.black);
       
        this.repaint();
    }
	
	
    public Casilla getCasilla(int x, int y)
    {
        return this.tablero[x][y];
    }

}
