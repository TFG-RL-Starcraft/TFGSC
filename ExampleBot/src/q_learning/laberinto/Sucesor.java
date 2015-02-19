package q_learning.laberinto;

import q_learning.Casilla;

public class Sucesor {
	
	private Casilla casilla_final;
	private Accion accion;
	
	public Sucesor(Casilla cas, Accion acc)
	{
		casilla_final = cas;
		accion = acc;
	}
	
	
	//GETTERS & SETTERS
	public Casilla getCasilla_final() {
		return casilla_final;
	}

	public void setCasilla_final(Casilla casilla_final) {
		this.casilla_final = casilla_final;
	}

	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}
	

}
