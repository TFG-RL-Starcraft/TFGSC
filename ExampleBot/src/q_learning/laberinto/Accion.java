package q_learning.laberinto;

/**
 * Enumerado que contiene las acciones posibles en cada estado
 * A cada acción le daremos un valor, que será el que luego utilizaremos para indexar en la tabla Q(s,a)
 * @author Lin
 *
 */
public enum Accion {

	MOVERSE_ARRIBA(0),
	MOVERSE_DERECHA(1),
	MOVERSE_ABAJO(2),
	MOVERSE_IZQUIERDA(3);
	
    private final int valor;
	
    Accion(int valor) { 
        this.valor = valor;
	}
	
	public int getValor()
	{
		return this.valor;
	}
	
}
