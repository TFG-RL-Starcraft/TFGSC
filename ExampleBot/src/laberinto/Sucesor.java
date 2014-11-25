package laberinto;

public class Sucesor {
	
	private Casilla casilla_final;
	private int accion;
	
	public Sucesor(Casilla cas, int acc)
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

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}
	

}
