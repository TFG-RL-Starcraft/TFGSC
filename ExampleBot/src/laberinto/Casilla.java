package laberinto;

import javax.swing.JButton;


/**
 *
 * @author usuario_local
 */
public class Casilla extends JButton
{
    boolean abierto;
    Casilla padre;
    double g;
    double h;
    double f;
    int posX ;
    int posY ;
    boolean esPared;
    boolean esMeta;
    boolean esInicio;
    
    public Casilla(int posx,int posy)
    {
        posX = posx;
        posY = posy;
        abierto = true;
        esPared = false;
        esMeta = false;
        esInicio = false;
        padre = null;
        f = Double.MAX_VALUE;
    }
    
    public Casilla(Casilla c)
    { 
        abierto = c.abierto;
        padre = c.padre;
        g = c.g;
        h = c.h;
        f = c.f;
        posX = c.getPosX();
        posY = c.getPosY();
        esPared = c.esPared;
        esMeta = c.esMeta;
        esInicio = c.esInicio;
    }
    public boolean esMeta()
    {
        return esMeta;
    }
    public boolean esPared()
    {
        return esPared;
    }
    public boolean esInicio()
    {
        return esInicio;
    }
    public void setPared()
    {
        esPared = true;
    }
    public void setInicio()
    {
        esInicio = true;
    }
    public void setMeta()
    {
        esMeta = true;
    }
    public void setAbierto(boolean b)
    {
        abierto =b;
    }
    public int getPosX()
    {
        return posX;
    }
    public int getPosY()
    {
        return posY;
    }
    public void setInicio(boolean b)
    {
        esInicio = b;
    }
    public void setMeta(boolean b)
    {
        esMeta = b;
    }
    public void setPared(boolean b)
    {
        esPared = b;
    }
  
    public void setF(double f)
    {
        this.f = f;
    }
    public void setG(double g)
    {
        this.g = g;
    }
    public void setH(double h)
    {
        this.h = h;
    }
    public double getF()
    {
        return f;
    }
    public double getG()
    {
        return g;
    }
    public double getH()
    {
        return h;
    }
    /*
    Esto es por el método Jincho, luego hay que cambiarlo
    */
    public void calculaG(Casilla inicio)
    {
        this.g = Math.sqrt(Math.pow((inicio.getPosX()-this.posX), 2) + Math.pow(inicio.getPosY()-this.posY, 2));
    }
    public void calculaH(Casilla meta)
    {
        this.h = Math.sqrt(Math.pow((meta.getPosX()-this.posX), 2) + Math.pow(meta.getPosY()-this.posY, 2));
    }
    public void calculaF()
    {
        this.f =  this.g + this.h;
    }
    
    public Casilla getPadre()
    {
        return padre;
    }
    
    public void setPadre(Casilla padre)
    {
        this.padre = padre;
    }
}
