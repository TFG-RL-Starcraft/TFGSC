
public class Maze implements World {
	
	private static final String[] MAZE = {
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",		
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",		
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",		
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",		
		"                                        ",
		"                                        ",
		"                                        ",
		"                                        ",
	};
	
	private static final int MAZE_WIDE = MAZE[0].length();
	private static final int MAZE_HEIGHT = MAZE.length;
	
	// player position
	private int posX, posY;
	
	public Maze() {
		reset();
	}
	
	// valid position
	private boolean valid(int x, int y) {
		return (0 <= x) && (x < MAZE_WIDE) &&
				(0 <= y) && (y < MAZE_HEIGHT) && 
				MAZE[y].charAt(x) == ' ';
	}

	// maze exit
	private boolean exit(int x, int y) {
		return (y == MAZE_HEIGHT-1) && (x == MAZE_WIDE-1);
	}
	
	@Override
	public int numStates() {
		return MAZE_WIDE * MAZE_HEIGHT;
	}

	@Override
	public int numActions() {
		return 4;
	}

	@Override
	public double execute(int action) {
		int[] dx = { 1, 0, -1, 0 };
		int[] dy = { 0, 1, 0, -1 };
		double reward = 0;

		posX += dx[action];
		posY += dy[action];
		
		if (valid(posX, posY)) {
			if (exit(posX, posY))
				reward = 1;
		} else {
			posX -= dx[action];
			posY -= dy[action];
			reward = -1;
		}

		return reward;
	}

	@Override
	public void reset() {
		posX = 0;
		posY = 0;
	}

	@Override
	public int state() {
		return posY * MAZE_WIDE + posX;
	}
	
	@Override
	public boolean finalState() {
		return exit(posX, posY);
	}

}
