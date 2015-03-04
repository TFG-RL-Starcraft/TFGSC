package q_learning.laberinto;

public class StarcraftState implements State {

	private int posX, posY;
	private int SC_WIDTH, SC_HEIGHT;
	
	public StarcraftState(int posX, int posY, int width, int height) {
		this.posX = posX;
		this.posY = posY;
		this.SC_WIDTH = width;
		this.SC_HEIGHT = height;
	}
	
	@Override
	public int getValue() {
		return posY * SC_WIDTH + posX;
	}

}
