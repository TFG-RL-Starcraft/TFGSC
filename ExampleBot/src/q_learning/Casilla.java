package q_learning;

import java.awt.Color;

import javax.swing.JButton;

import q_learning.generador_laberintos.TipoCasilla;


/**
 *
 * @author usuario_local
 */
@SuppressWarnings("serial")
public class Casilla extends JButton
{
    int posX ;
    int posY ;
    TipoCasilla tipo;
    
    public Casilla(int posx,int posy)
    {
        posX = posx;
        posY = posy;
        tipo = TipoCasilla.VACIO;
    }
    
    public Casilla(Casilla c)
    { 
        posX = c.getPosX();
        posY = c.getPosY();
        tipo = c.getTipo();
    }
    
	public boolean esMeta()
    {
        return this.tipo.compareTo(TipoCasilla.META) == 0;
    }
    public boolean esPared()
    {
        return this.tipo.compareTo(TipoCasilla.PARED) == 0;
    }
    public boolean esInicio()
    {
        return this.tipo.compareTo(TipoCasilla.INICIO) == 0;
    }
    public void setInicio()
    {
    	this.tipo = TipoCasilla.INICIO;
		this.setBackground(Color.blue);
    }
    public void setMeta()
    {
        this.tipo = TipoCasilla.META;
		this.setBackground(Color.green);
    }
    public TipoCasilla getTipo()
    {
		return tipo;
	}
    public void setTipo(TipoCasilla tipo)
    {
        this.tipo = tipo;
    }
    public int getPosX()
    {
        return posX;
    }
    public int getPosY()
    {
        return posY;
    }
    public void setPared(boolean b)
    {
        if(b)
        {
        	this.tipo = TipoCasilla.PARED;
            this.setBackground(Color.black);
        }
        else
        {
        	this.tipo = TipoCasilla.VACIO;
            this.setBackground(Color.white);
        }
    }

	public void reset() {
		this.tipo = TipoCasilla.VACIO;
		this.setBackground(Color.WHITE);
	}
}
