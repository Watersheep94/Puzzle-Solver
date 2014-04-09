import java.io.*;
import java.util.*;

public class Solver {

	/**
	 * @param args
	 */
	Board myRoot;
	Board myGoal;
	Board currentBoard;
	PriorityQueue<Node> fringe;
	HashSet<Board> boardSeen;
	public Solver(Board root, Board goal){
		myRoot = root;
		myGoal = goal;
		currentBoard = root;
		boardSeen = new HashSet<Board>();
		boardSeen.add(root);
		fringe = new PriorityQueue (1000, new NodeComparator());
		fringe.add(new Node(root, 0));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		long startTime = System.currentTimeMillis();
		if (args.length > 3 || args.length < 2) {
            System.err.println ("Wrong number arguments");
            System.exit (1);
        }
		String inputfile;
		String outputfile;
		if(args[0].startsWith("-o")){
			inputfile = args[1];
			outputfile = args[2];
		}else{
			inputfile = args[0];
			outputfile = args[1];
		}
		Readinput in = new Readinput(inputfile, 0, 0);
		Readinput goal = new Readinput(outputfile, in.board().getHeight(), in.board().getWidth());
		Solver s = new Solver(in.board(), goal.board());
		s.solve();

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
		}catch (OutOfMemoryError e){
			System.out.println("Ran out of memory");
		}
	}
	private class Node{
		private int manhatdis;
		private Board myBoard;
		public Node(Board b, int mdis){
			myBoard = b;
			manhatdis = mdis;
		}
	}
	private class NodeComparator implements Comparator<Node>{
		
		@Override
		public int compare(Node obj1, Node obj2){

			if (obj1.manhatdis > obj2.manhatdis){
				return 1;
			}else if (obj1.manhatdis < obj2.manhatdis){
				return -1;
			}else{
				return 0;
			}
		}

	}
	private int calculateManhattan(Board board){
		int sumdistance = 0;
		for (Block i: board.getBlocks()){
			for (Block b: myGoal.getBlocks()){
				if (b.getType().equals(i.getType())){
					sumdistance += Math.abs((i.UpperLeft().x - b.UpperLeft().x));
					sumdistance += Math.abs((i.UpperLeft().y - b.UpperLeft().y));
				}
			}
		}
	return sumdistance;
	}
	public void solve(){
		//System.out.println(myGoal);
		while (!this.isSolved()){
			this.findPossibleMoves();
			if(fringe.isEmpty()){
				System.out.println("No solution found");
				System.exit(1);
			}
			currentBoard = fringe.remove().myBoard;
			

			

			
		}
		System.out.println(currentBoard.displayMoves());
		
		
	}
	public boolean isSolved(){

		for (Block i: myGoal.getBlocks()){ 

            if (!currentBoard.getBlocks().contains(i)) 
                return false; 
        } 
        return true; 
	}
	private void findPossibleMoves(){
		HashMap<Point, Block> currTray = currentBoard.getTray();
		for (int i=0; i < currentBoard.getWidth(); i++){
			for(int j=0; j < currentBoard.getHeight(); j++){

				if (!currTray.containsKey(new Point(i,j))){
					if (i-1 >= 0){
					this.tryMove(new Point(i-1, j), "right");}
					if(i+1 < currentBoard.getWidth()){
					this.tryMove(new Point(i+1, j), "left");}
					if(j-1 >= 0){
					this.tryMove(new Point(i, j-1), "down");}
					if(j+1 < currentBoard.getHeight()){
					this.tryMove(new Point(i, j+1), "up");}
							
					
			}
			}
		}
		
	}
	private void tryMove(Point p, String direction){
		HashMap<Point, Block> currTray = currentBoard.getTray();
		if (currTray.containsKey(p)){
		Point upperl = currTray.get(p).UpperLeft();
		Point moveto = new Point(-1,-1);
		if (direction.equals("right")){
			moveto = new Point(upperl.x+1, upperl.y);
		}else if (direction.equals("left")){
			moveto = new Point(upperl.x-1, upperl.y);
		}else if (direction.equals("up")){
			moveto = new Point(upperl.x, upperl.y-1);
		}else if (direction.equals("down")){
			moveto = new Point(upperl.x, upperl.y+1);
		}
		
		try{
		Board possibleBoard = currentBoard.makeMove(currTray.get(p), moveto);
		if (!boardSeen.contains(possibleBoard)){
			Node n = new Node(possibleBoard, this.calculateManhattan(possibleBoard));
			fringe.add(n);
			boardSeen.add(possibleBoard);
		}
		}catch(IllegalStateException e){
			return;
		}
			
		}
		
		
	}


}
