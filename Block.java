public class Block { 
  
        private Point UpperLeft; 
        private Point LowerRight; 
        private String size; 
          
        public Block(Point one, Point two) { 
              
            if (one.x < 0 || one.x < 0 || one.x < 0 || one.y < 0) { 
                throw new IllegalArgumentException("Points can not be negative."); 
            } 
              
            if (one.x > two.x || one.y > two.y) { 
                throw new IllegalArgumentException("Points are not UpperLeft and LowerRight"); 
            } 
              
            UpperLeft = one; 
            LowerRight = two; 
              
            size = Integer.toString(getHeight()) + "x" + Integer.toString(getWidth()) ; 
        } 
          
        public Point UpperLeft(){ 
            return UpperLeft; 
        } 
          
        public Point LowerRight(){ 
            return LowerRight; 
        } 
  
        public int getHeight() { 
            return ((LowerRight.y - UpperLeft.y) + 1); 
        } 
  
        public int getWidth() { 
            return ((LowerRight.x - UpperLeft.x) + 1); 
        } 
  
        public void changeOrientation(Point one, Point two) { 
              
            Block temp = new Block(one,two); 
              
            if (temp.getHeight() != this.getHeight() || temp.getWidth() != this.getWidth()) { 
                throw new IllegalArgumentException("Block size can not change"); 
            } 
              
            UpperLeft = one; 
            LowerRight = two; 
        } 
          
        public String getType() { 
            return size; 
        } 
          
        public Block newCopy() { 
            return new Block(UpperLeft(), LowerRight()); 
        } 
        public int hashCode(){ 
            return UpperLeft.x * 3 + UpperLeft.y * 11 + LowerRight.x * 19 + LowerRight.y * 17; 
        } 
        public boolean equals(Object o){
        	Block block = (Block) o;
        	return block.UpperLeft.equals(UpperLeft) && block.LowerRight.equals(LowerRight);
        }
        public String toString(){
        	return " (" + this.UpperLeft + this.LowerRight + ") ";
        }
}
