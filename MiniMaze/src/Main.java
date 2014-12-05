
public class Main {

	public static void main(String[] args) {
		Maze maze = new Maze();
		QLearner learner = new QLearner(maze);
		
		for (int i=0; i<10000; i++)
			System.out.println(learner.run(Integer.MAX_VALUE));
		
	}
}
