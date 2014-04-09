import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.HashSet; 
    
public class Board {  
        
    private HashMap<Point, Block> myTray;   
    private int boardWidth;  
    private int boardHeight;  
    private ArrayList<String> myMoves; 
    private HashSet<Block> Blocks; 
    private HashSet<Point> emptySpaces; 
      
    public Board(int height, int width, Board parent) {  
            
        if (height > 256 || width > 256)   
            throw new IllegalArgumentException("Exceeded maximum allowed configuration for tray size.");  
        if (height < 0 || width < 0)   
            throw new IllegalArgumentException("Size must be a nonnegative number.");  
        boardWidth = width;  
        boardHeight = height;  
         
        myTray = new HashMap<Point, Block>(); 
        myMoves = new ArrayList<String>(); 
        Blocks = new HashSet<Block>(); 
        emptySpaces = new HashSet<Point>(); 
          
        Blocks.addAll(parent.Blocks); 
        myMoves.addAll(parent.myMoves); 
        emptySpaces.addAll(parent.emptySpaces); 
          
        if (parent.myTray != null) { 
            myTray.putAll(parent.myTray);    
        } 
    }  
      
    public Board(int height, int width) { 
        boardWidth = width;  
        boardHeight = height;  
        myTray = new HashMap<Point, Block>(); 
        myMoves = new ArrayList<String>(); 
        Blocks = new HashSet<Block>(); 
        emptySpaces = new HashSet<Point>(); 
          
        for (int j = 0; j < boardWidth; j++) { 
            for (int k = 0; k < boardHeight; k++) { 
                emptySpaces.add(new Point(j,k)); 
            } 
        } 
    }  
        
    private void clearBlock(Block block) {  
            
        for (int j = block.UpperLeft().x; j <= block.LowerRight().x; j++) {  
            for (int k = block.UpperLeft().y; k <= block.LowerRight().y; k++) {  
                myTray.remove(new Point(j,k)); 
                Blocks.remove(block); 
                emptySpaces.add(new Point(j,k)); 
            }  
        }  
    }  
        
    private void fillBlock(Block block) {  
            
        for (int j = block.UpperLeft().x; j <= block.LowerRight().x; j++) {  
            for (int k = block.UpperLeft().y; k <= block.LowerRight().y; k++) {  
                myTray.put(new Point(j,k), block);  
                Blocks.add(block); 
                emptySpaces.remove(new Point(j,k)); 
            }  
        }  
    }  
        
    public boolean validMove(Block block, String direction) {  
           
        if (direction.equals("right")) { 
             
           for (int i = 0; i < block.getHeight(); i++) { 
               //checks if the block can move one space to the right 
               if (myTray.containsKey(new Point(block.LowerRight().x+1, block.LowerRight().y - i))) { 
                   return false; 
               } 
           } 
           return true; 
        } 
         
        if (direction.equals("left")) { 
              
             for (int i = 0; i < block.getHeight(); i++) { 
               //checks if the block can move one space to the left 
               if (myTray.containsKey(new Point(block.UpperLeft().x-1, block.UpperLeft().y + i))) { 
                   return false; 
               } 
           } 
            return true; 
        } 
          
        if (direction.equals("up")) { 
              
            for (int i = 0; i < block.getWidth(); i++) { 
               //checks if the block can move one space up 
               if (myTray.containsKey(new Point(block.UpperLeft().x + i, block.UpperLeft().y-1))) { 
                   return false; 
               } 
               return true; 
           } 
              
        } 
          
        if (direction.equals("down")) { 
              
            for (int i = 0; i < block.getWidth(); i++) { 
                   //checks if the block can move one space down 
                   if (myTray.containsKey(new Point( block.LowerRight().x - i, block.LowerRight().y+1))) { 
                       return false; 
                   } 
               } 
            return true; 
        } 
          
        return true;  
    }  
      
       
    public Board makeMove(Block block, Point point, String direction) throws IllegalStateException {  
            
        Block tempBlock = new Block(block.UpperLeft(), block.LowerRight()); //copy of the block being moved  
        
        if (!validMove(block, direction)) {  
            throw new IllegalStateException("not a valid move!");  
        }  
          
        else{   
            tempBlock.changeOrientation(point, new Point(point.x + block.getWidth()-1, point.y + block.getHeight()-1));  
        }  
            
        Board board = new Board(boardHeight, boardWidth, this);  
        board.clearBlock(block); //clear the original position of the block in the new board  
        board.fillBlock(tempBlock); //fill the new board with the updated block position  
          
        String moveMade = Integer.toString(block.UpperLeft().y) + " " +  Integer.toString(block.UpperLeft().x) + " " + Integer.toString(point.y) + " " + Integer.toString(point.x);  
        board.myMoves.add(moveMade); 
        return board;  
    }  
        
    public void addBlock(Block block) throws IllegalStateException {  
            
        if (myTray.containsValue(block))  
            throw new IllegalStateException("block already in board!");  
            
        fillBlock(block);  
    }  
     
    
    public boolean isOK() throws IllegalStateException {  
        ArrayList<Point> occupied = new ArrayList<Point>();
        if (Blocks != null) {
        	for (Block current : Blocks) { 
            	ArrayList<Point> tempPoints = new ArrayList<Point>();  
                
            	//This checks to make sure the blocks are within the actual board dimensions.
            	if (current.UpperLeft().x < 0 || current.UpperLeft().y < 0 || current.LowerRight().x > boardWidth || current.LowerRight().y > boardHeight)  
                    throw new IllegalStateException("isOK Error: Blocks go out of dimensions given.");  
            	
            	//This checks to make sure the upper left point values are smaller or equal to the lower right point values.
            	if (current.UpperLeft().x < current.LowerRight().x || current.UpperLeft().y < current.LowerRight().y) 
                    throw new IllegalStateException("isOK Error: The blocks' point values are not correctly inputted as upper left and lower right coordinates."); 
            	
            	//This adds blocks into an array list by converting them into points, 
            	//so the values of blocks that occupy more than two spaces can be checked for all of the blocks they occupy.
            	for (int j=current.UpperLeft().x; j <= current.LowerRight().x; j++) {
                	for (int k=current.UpperLeft().y; k <= current.LowerRight().y; k++) {
                		tempPoints.add(new Point(j, k));
                	}
                }
            	
            	//This checks for overlapping blocks.
            	for (Point checkPoint : tempPoints) {
            		if (occupied.contains(checkPoint)) {
            			throw new IllegalStateException("isOK Error: Overlapping block space at (" + checkPoint.x + "," + checkPoint.y + ")");
            		} else {
            			occupied.add(checkPoint);
            		}            	
            	} //end overlapping block check
        	} //end for loop for hash set of blocks  
        } //end null check
        return true;        
    }  //end isOK
    
    
    public int hashCode(){ //new hashCode 
        
        int rtn = 0; 
        for (Block block: myTray.values()) { 
            rtn += Math.pow(block.UpperLeft().x, 2) + Math.pow(block.LowerRight().y, 3); 
        } 
        return rtn; 
    }  
        
    public boolean equals(Object o) {  
        Board board = (Board) o;  
        if (board.boardHeight != boardHeight || board.boardWidth != boardWidth)  
            return false;  
        else if (!this.myTray.equals(board.myTray)) {  
            return false;  
        }  
        else {  
            return true;  
        }  
    }  
        
    public HashMap<Point, Block> getTray() { 
        return myTray; 
    } 
        
    public String toString() {  
        String board = "";  
         
        for (int j = 0; j < boardHeight; j++) {  
            for (int k = 0; k < boardWidth; k++) {  
                if (myTray.containsKey(new Point(k,j))) {  
                    Block block = myTray.get(new Point(k,j)); 
                    int size = block.getHeight() * block.getWidth(); 
                    board += Integer.toString(size);  
                }  
                else{  
                    board += "O";  
                }  
            }  
            board += "\n";  
        }  
        return board;  
    }  
      
    public String displayMoves() { 
          
        String allMoves = ""; 
        for (int i = 0; i < myMoves.size(); i++) { 
            allMoves += myMoves.get(i); 
            allMoves += "\n"; 
        } 
        return allMoves; 
    } 
      
    public int getHeight() { 
        return boardHeight; 
    } 
      
    public int getWidth() { 
        return boardWidth; 
    } 
      
    public HashSet<Block> getBlocks() { 
        return Blocks; 
    } 
      
    public HashSet<Point> getEmptySpaces() { 
        return emptySpaces; 
    } 
      
  
} 
