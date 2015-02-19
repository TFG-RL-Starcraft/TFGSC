package q_learning.generador_laberintos;

public enum TipoCasilla
{
	VACIO(0),
	PARED(1),
	INICIO(2),
	META(3);
	
    private final int valor;
	
    TipoCasilla(int valor) { 
        this.valor = valor;
	}
	
	public int getValor()
	{
		return this.valor;
	}
}
