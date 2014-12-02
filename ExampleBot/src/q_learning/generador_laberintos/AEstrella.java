package q_learning.generador_laberintos;

import java.util.ArrayList;

import q_learning.Casilla;



public class AEstrella 
{
    ArrayList<Casilla> abierto;
    ArrayList<Casilla> cerrado;
    Casilla [][] tablero;
    Casilla inicial;
    Casilla meta ;
    int limX;
    int limY;
    ArrayList<Casilla> camino;
    
    public AEstrella(Casilla [][] tablero, Casilla inicial, Casilla meta, int limX, int limY)
    {
        this.tablero = tablero;
        this.limX = limX;
        this.limY = limY;
        this.inicial = inicial;
        this.meta = meta;
        
        //Initialize OPEN list
        abierto = new ArrayList<Casilla>();
      //Initialize CLOSED list
        cerrado = new ArrayList<Casilla>();
    }
    
    public void setPared (Casilla p, boolean b)
    {
        if(b)
        {
            cerrado.add(p);
        }
        else
        {
            cerrado.remove(p);
        }
    }
    
    public ArrayList<Casilla> buscaMejorCamino()
    {
        boolean camino_encontrado = false;
        
      //Add node_start to the OPEN list
        abierto.add(inicial);
        inicial.calculaH(meta);
        inicial.calculaG(inicial);
        inicial.calculaF();
       
      //While the OPEN list is not empty
      //{
        while(!abierto.isEmpty() && !camino_encontrado)
        {
            Casilla n = null;  
            double f = Double.POSITIVE_INFINITY;
            int indiceElegido = -1;
          //Get node n of de OPEN list with the lowest f(n)
            int i = 0;
            while(i < abierto.size())
            {
                //System.out.println(abierto.get(i).getPosX() + ", " + abierto.get(i).getPosY() + " tiene F: " + abierto.get(i).getF());
                if(abierto.get(i).getF() < f)
                {
                    f = abierto.get(i).getF();
                    n = abierto.get(i);
                    indiceElegido = i;       
                }
                i++;
            }
            
          //Add n to the CLOSED list
            //System.out.println("Cerrado: " + cerrado.size());
            //System.out.println("Abierto: " + abierto.size());
            //System.out.println("He elegido la: " + abierto.get(indiceElegido).getPosX() + ", " + abierto.get(indiceElegido).getPosY());
            cerrado.add(n);
            abierto.remove(indiceElegido);
            
            //System.out.println("Cerrado: " + cerrado.size());
            //System.out.println("Abierto: " + abierto.size());
        
            
          //If n is the same as node_goal we have found the solution
            if(n == meta)
            {
                camino_encontrado = true; //BREAK
            }
            else //not found the solution
            {
                //Generate each successor node nâ€™ of n
                ArrayList<Casilla> sucesores = generaSucesores(n);
              //For each successor node nâ€™ of n
              //{
                
                for(Casilla nPrima:sucesores)
                {

                    Casilla casilla_aux = new Casilla(nPrima);
 
                    //Set the parent of nâ€™ to n
                    casilla_aux.setPadre(n);
                    //Set h(nâ€™) to be the heuristically estimate distance to node_goal
                    casilla_aux.calculaH(meta);
                    //Set g(nâ€™) to be g(n) plus the cost to get to nâ€™ from n
                   // casilla_aux.calculaG(inicial);
                    //casilla_aux.calculaG(inicial);
                    casilla_aux.setG(n.getG()+ Math.sqrt(Math.pow((n.getPosX()-casilla_aux.getPosX()), 2) + Math.pow(n.getPosY()-casilla_aux.getPosY(), 2)));
                    //Set f(nâ€™) to be g(nâ€™) plus h(nâ€™)
                    casilla_aux.calculaF();
                    //If nâ€™ is on the OPEN list and existing one is as good or better, then
                    //discard nâ€™ and continue
                    if(abierto.contains(nPrima))
                    {
                        abierto.remove(nPrima);
                        
                        if(casilla_aux.getF() < nPrima.getF())
                        {
                        	nPrima.setPadre(casilla_aux.getPadre());
                        	nPrima.setH(casilla_aux.getH());
                        	nPrima.setG(casilla_aux.getG());
                            nPrima.setF(casilla_aux.getF());
                        }

                    }
                    else
                    {
                        nPrima.setPadre(casilla_aux.getPadre());
                        nPrima.setH(casilla_aux.getH());
                        nPrima.setG(casilla_aux.getG());
                        nPrima.setF(casilla_aux.getF());
                    }
                       
                    abierto.add(nPrima);
 
                }
                //System.out.println("DESPUES DE EVALUAR SUCESORES");
                //System.out.println("Cerrado: " + cerrado.size());
                //System.out.println("Abierto: " + abierto.size());
            }
        }

        if (abierto.isEmpty()) //no hay soluciÃ³n 
        {
            return null;
        } 
        else
        {
            return calculaCaminoInverso(meta);
        }
    }
    
    /**
     * Devuelve todos los posibles sucesores de una casilla
     * Evitando devolver las casillas que estÃ¡n en la lista de "cerrado"
     * @param c
     * @return 
     */
    public ArrayList<Casilla> generaSucesores(Casilla c)
    {
        ArrayList<Casilla> sucesores = new ArrayList<Casilla>();
        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++ )
            {
                if((c.getPosX()+x >= 0 && c.getPosX()+x < limX) && 
                        (c.getPosY()+y >=0 && c.getPosY()+y < limY) && 
                        (x != 0 || y != 0) &&
                        !cerrado.contains(tablero[c.getPosX()+x][c.getPosY()+y]))
                {
                    sucesores.add(tablero[c.getPosX()+x][c.getPosY()+y]);
                }
            }
        }

        /*PRUEBA DE LOS SUCESORES*/
        /*System.out.println("casilla: " + c.posX + ", " + c.posY );
        for(Casilla s : sucesores)
        {
            System.out.println("sucesores: " + s.posX + ", " + s.posY );    
        }*/

        return sucesores;
    }
    
    /**
     * 
     * @param meta
     * @return 
     */
    private ArrayList<Casilla> calculaCaminoInverso(Casilla meta) {
        
        ArrayList<Casilla> camino = new ArrayList<Casilla>();
        
        Casilla casilla_actual = meta.getPadre();
        
        while(!casilla_actual.esInicio())
        {
            camino.add(casilla_actual);
            casilla_actual = casilla_actual.getPadre();
        }
        
        return camino;
    }
}

