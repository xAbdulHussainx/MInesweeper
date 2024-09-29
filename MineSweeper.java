// TO DO: add your implementation and JavaDocs.

import java.util.Random;

/**
 * The minesweeper class.
 */
public class MineSweeper{

 
 	  

    /**
     * The eumerations method.
     * 
     */
    public enum Level {
        /**
         * TINY enum.
         */
        TINY, 
        /**
         * EASY enum.
         */
        EASY,
        /**
         * MEDIUM enum.
         */
        MEDIUM,
        /**
         * HARD enum.
         */
        HARD,
        /**
         * CUSTOM enum.
         */
        CUSTOM 
    }
    
    
    /**
     * intialize field.
     */
    private static int ROWS_EASY = 9;
    /**
     * intialize field.
     */
    private static int COLS_EASY = 9;
    /**
     * intialize field.
     */
    private static int MINES_EASY = 10;
    /**
     * intialize field.
     */
    private static int ROWS_TINY = 5;
    /**
     * intialize field.
     */
    private static int COLS_TINY = 5;
    /**
     * intialize field.
     */
    private static int MINES_TINY = 3;
    /**
     * intialize field.
     */
    private static int ROWS_MEDIUM = 16;
    /**
     * intialize field.
     */
    private static int COLS_MEDIUM = 16;
    /**
     * intialize field.
     */
    private static int MINES_MEDIUM = 40;
    /**
     * intialize field.
     */
    private static int ROWS_HARD = 16;
    /**
     * intialize field.
     */
    private static int COLS_HARD = 30;
    /**
     * intialize field.
     */
    private static int MINES_HARD = 99;

	   
    /**
     * intialize field.
     */
    private DynGrid310<Cell> board;

	  
    /**
     * intialize field.
     */
    private int rowCount;
    
    
    /**
     * intialize field.
     */
    private int colCount;

	
 /**
     * intialize field.
     */
	private int mineTotalCount;

	
 /**
     * intialize field.
     */
	private int clickedCount; 

	
 /**
     * intialize field.
     */
	private int flaggedCount; 

    

    /**
     * The status method.
     */
    public enum Status {
        /**
         * INIT enum.
         */
        INIT,
        /**
         * INGAME enum.
         */
        INGAME,
        /**
         * EXPLODED emum.
         */
        EXPLODED,
        /**
         * SOLVED enum.
         */
        SOLVED
    }

    /**
     * intialize field.
     */
    private Status status; 


    /**
     * The string status method.
     */
    public final static String[] Status_STRINGS = {
        "INIT", "IN_GAME", "EXPLODED", "SOLVED"
    };
    
    

    /**
     * The constructor.
     * @param seed The seed.
     * @param level The level.
     */
    public MineSweeper(int seed, Level level){
    
        if (level==Level.CUSTOM)
            throw new IllegalArgumentException("Customized games need more parameters!");
            
        switch(level){
            case TINY:
                rowCount = ROWS_TINY;
                colCount = COLS_TINY;
                mineTotalCount = MINES_TINY;
                break;
            case EASY:
                rowCount = ROWS_EASY;
                colCount = COLS_EASY;
                mineTotalCount = MINES_EASY;
                break;
            case MEDIUM:
                rowCount = ROWS_MEDIUM;
                colCount = COLS_MEDIUM;
                mineTotalCount = MINES_MEDIUM;
                break;
            case HARD:
                rowCount = ROWS_HARD;
                colCount = COLS_HARD;
                mineTotalCount = MINES_HARD;
                break;
            default:
                rowCount = ROWS_TINY;
                colCount = COLS_TINY;
                mineTotalCount = MINES_TINY;
		}
        

        board = genEmptyBoard(rowCount, colCount);
        

        initBoard(seed);
    }
    
    /**
     * The constructor.
     * @param seed The seed.
     * @param level The level.
     * @param rowCount The rowcount.
     * @param colCount The colcount.
     * @param mineCount The minecount.
     */
    public MineSweeper(int seed, Level level, int rowCount, int colCount, int mineCount){
        
        if (level != Level.CUSTOM)
        	throw new IllegalArgumentException("Only customized games need more parameters!");
        
        
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.mineTotalCount = mineCount;
        
        
        board = genEmptyBoard(rowCount, colCount);
        
    
       	initBoard(seed);
        
    }
    
    
        
   
    /**
     * The initilize board method.
     * @param seed The seed.
     */
    public void initBoard(int seed){
        
        Random random = new Random(seed);
        
        int mineNum = 0;
        for ( ;mineNum<mineTotalCount;){
        
            int row = random.nextInt(rowCount);
            int col = random.nextInt(colCount);
            
             
            if (hasMine(row, col)){
                continue;
            }
            
            board.get(row,col).setMine();
            mineNum++;
        }
        
        for (int row=0; row<rowCount; row++){
            for (int col=0; col<colCount; col++){
            
                int count = countNbrMines(row, col);
                board.get(row,col).setCount(count);
            }
        }
        
        status = Status.INIT;
           
        flaggedCount = 0;
        clickedCount = 0;

    }
    	
    /**
     * report row.
     * @return integer.
     */
    public int rowCount() { return rowCount; }
    
    /**
     * Report column.
     * @return integer.
     */
    public int colCount() { return colCount; }

    /**
     * is solved method.
     * @return boolean.
     */
    public boolean isSolved(){ return status == Status.SOLVED;    }
    
    /**
     * The isexploded.
     * @return boolean.
     */
    public boolean isExploded(){ return status == Status.EXPLODED; }

	 
    /**
     * board to string method.
     * @return String.
     */
    public String boardToString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("- |");
        for (int j=0; j<board.getNumCol(); j++){
			sb.append(j +"|");
		}
        sb.append("\n");
        
    	for(int i=0; i<board.getNumRow(); i++){
            sb.append(i+" |");
    		for (int j=0;j<board.getNumCol(); j++){
      			sb.append(board.get(i,j).toString());
      		    sb.append("|");
      		}
      		sb.append("\n");
    	}
    	return sb.toString().trim();

    }
    
   
    /**
     * To string method.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Board Size: " + rowCount() + " x " + colCount() + "\n");
        sb.append("Total mines: " + mineTotalCount + "\n");
        sb.append("Remaining mines: " + mineLeft() + "\n");
        sb.append("Game status: " + getStatus() + "\n");
        
        sb.append(boardToString());
        return sb.toString().trim();
    }

   
    /**
     * Is flagged method.
     * @param row the row.
     * @param col the column.
     * @return boolean.
     */
    public boolean isFlagged(int row, int col){
    	   
    	
        if (!board.isValidCell(row,col)){
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.isFlagged());
    }
    /**
     * The visible method.
     * @param row the row.
     * @param col the column.
     * @return boolean
     */
    public boolean isVisible(int row, int col){
    	 
    	
        if (!board.isValidCell(row,col)){
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.visible());               
    }
    /**
     * The has mine method.
     * @param row the row.
     * @param col the column.
     * @return booleam.
     */
    public boolean hasMine(int row, int col){
    	  
    	
        if (!board.isValidCell(row,col)){
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.hasMine());               
    }
    
    /**
     * The getcount method.
     * @param row the row.
     * @param col the column.
     * @return integer.
     */
    public int getCount(int row, int col){
    	
    	
        if (!board.isValidCell(row,col)){
            return -2;
        }
 
        Cell cell = board.get(row, col);
        return (cell.getCount());                    
    }
    

    /**
     * Mine left method.
     * @return integer.
     */
    public int mineLeft() { 
    	return mineTotalCount-flaggedCount; 
    	
    }
    /**
     * Status method.
     * @return string.
     */
    public String getStatus() { 
    	// report current game status
    	return Status_STRINGS[status.ordinal()]; 
    	
    }



    //return the game board
    /**
     * The getboard method.
     * @return board
     */
    public DynGrid310<Cell> getBoard(){ return board;}

 /**
     * The setboard method.
     * @param newBoard The newboard.
     * @param mineCount The minecount.
     */
	public void setBoard(DynGrid310<Cell> newBoard, int mineCount) {
		this.board = newBoard;
		
		rowCount = board.getNumRow();
		colCount = board.getNumCol();
		
		
	 	status = Status.INIT;
           
        flaggedCount = 0;
        clickedCount = 0;
        mineTotalCount = mineCount;
	}

   
    /**
     * The private helper method.
     * @param row The row.
     * @param col The column.
     * @return integer.
     */
    private int monkey(int row, int col){
        int numrow= board.getNumRow()-1;
        int numcol = board.getNumCol()-1;
        if(row>=0&&col>=0&&row<=numrow&&col<=numcol&&countNbrMines(row, col) == 0&&board.get(row, col).visible()==false){
            Cell cell = board.get(row, col);
            cell.setVisible();
            
            int placeholderrow;
            int placeholdercol;
            //top left
            placeholderrow=row-1;
            placeholdercol=col-1;
            monkey(placeholderrow,placeholdercol);
            //top middle
            placeholderrow=row-1;
            placeholdercol=col;
            monkey(placeholderrow,placeholdercol);
            //top right
            placeholderrow=row-1;
            placeholdercol=col+1;
            monkey(placeholderrow,placeholdercol);
            //middle left
            placeholderrow=row;
            placeholdercol=col-1;
            monkey(placeholderrow,placeholdercol);
            //middle right
            placeholderrow=row;
            placeholdercol=col+1;
            monkey(placeholderrow,placeholdercol);
            //bottom left
            placeholderrow=row+1;
            placeholdercol=col-1;
            monkey(placeholderrow,placeholdercol);
            //bottom middle
            placeholderrow=row+1;
            placeholdercol=col;
            monkey(placeholderrow,placeholdercol);
            //bottom right
            placeholderrow=row+1;
            placeholdercol=col+1;
            monkey(placeholderrow,placeholdercol);
            
            return 0;
        }
        else if(countNbrMines(row,col)>0){
            Cell cell = board.get(row, col);
            cell.setVisible();
            return 0;
        }
        else{
            return 0;
        }
        
    }
    
   
    /**
     * Creating the board method.
     * @param rowNum The row.
     * @param colNum The col.
     * @return board.
     */
    public static DynGrid310<Cell> genEmptyBoard(int rowNum, int colNum){
        if(rowNum>0&&colNum>0){
            DynGrid310<Cell> placeholder = new DynGrid310<Cell>();
            for (int i = 0; i < rowNum; i++) {
                DynArr310<Cell> placeholder1 = new DynArr310<>(colNum);
                for (int j = 0; j < colNum; j++) {
                    Cell cell = new Cell();
                    placeholder1.add(cell);
                }
                placeholder.addRow(i, placeholder1);
            }
            return placeholder;
        }
		
    	else{
            return null;
        } 

    }
    
    /**
     * The count number of mines.
     * @param row The row.
     * @param col The column.
     * @return integer.
     */
    public int countNbrMines(int row, int col){
   
        
    	if(row>=board.getNumRow()||col>=board.getNumCol()||row<0||col<0){
            return -2;
        }

        else{
            Cell cell = board.get(row, col);
            int numrow= board.getNumRow()-1;
            int numcol = board.getNumCol()-1;
            int placeholderrow=0;
            int placeholdercol=0;
            int count=0;
            if(cell.hasMine()){
                return -1;
            }
            else{
                //top left
                placeholderrow=row-1;
                placeholdercol=col-1;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //top middle
                placeholderrow=row-1;
                placeholdercol=col;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //top right
                placeholderrow=row-1;
                placeholdercol=col+1;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //middle left
                placeholderrow=row;
                placeholdercol=col-1;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //middle right
                placeholderrow=row;
                placeholdercol=col+1;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //bottom left
                placeholderrow=row+1;
                placeholdercol=col-1;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //bottom middle
                placeholderrow=row+1;
                placeholdercol=col;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                //bottom right
                placeholderrow=row+1;
                placeholdercol=col+1;
                if(placeholderrow>=0&&placeholdercol>=0&&placeholderrow<=numrow&&placeholdercol<=numcol){
                    cell = board.get(placeholderrow, placeholdercol);
                    if(cell.hasMine()){
                        count+=1;
                    }
                }
                return count;
            }
        }

    	
    	
    }
    



    /**
     * The click at method.
     * @param row The row.
     * @param col The column.
     * @return integer.
     */
    public int clickAt(int row, int col){

        if(row>=board.getNumRow()||col>=board.getNumCol()||row<0||col<0){
            return -2;
        }
        else{
            Cell cell = board.get(row, col);
            if(cell.isFlagged()||cell.visible()){
                return -2;
            }
            else if(cell.hasMine()){
                cell.setVisible();
                status= Status.EXPLODED;
                return -1;
            }
            else if (countNbrMines(row, col)==0){
                int placeholder = monkey(row, col);
                return placeholder;
            }
            else{
                cell.setVisible();
                status = Status.INGAME;
                return countNbrMines(row, col);
            }
        }
    	

   	}
    
    /**
     * The flag method.
     * @param row The row.
     * @param col The column
     * @return boolean
     */
    public boolean flagAt(int row, int col){
    	
        if(row>=board.getNumRow()||col>=board.getNumCol()||row<0||col<0){
            return false;
        }
        else{
            Cell cell = board.get(row, col);
            if(cell.visible()){
                return false;
            }
            else{
                cell.setFlagged();
                flaggedCount+=1;
                return true;
            }
        }

    	
         
    }

    /**
     * The unflag method.
     * @param row The row.
     * @param col The column.
     * @return boolean.
     */
    public boolean unFlagAt(int row, int col){
    
        if(row>=board.getNumRow()||col>=board.getNumCol()||row<0||col<0){
            return false;
        }
        else{
            Cell cell = board.get(row, col);
            if(cell.isFlagged()){
                cell.unFlagged();
                flaggedCount-=1;
                return true;
            }
            else{
                return false;
            }
            
        }
    	
      
    }

    
       


    
    /**
     * The main method.
     * @param args The arguments.
     */
    public static void main(String args[]){
    	
    	DynGrid310<Cell> myBoard = MineSweeper.genEmptyBoard(3,4);
    	
    	
    	if (myBoard.getNumRow() == 3 && myBoard.getNumCol()==4 &&
    		!myBoard.get(0,0).hasMine() && !myBoard.get(1,3).visible() &&
    		!myBoard.get(2,2).isFlagged() && myBoard.get(2,1).getCount()==-1){
    		System.out.println("Yay 0");
    	}
        

        
		Random random = new Random(10);
        MineSweeper game = new MineSweeper(random.nextInt(),Level.TINY);
        
        
        
        if (game.countNbrMines(0,0) == 0 && game.countNbrMines(4,2) == 1 &&
        	game.countNbrMines(3,3) == 3 &&	game.countNbrMines(2,3) == -1 &&
        	game.countNbrMines(5,5) == -2){
        	System.out.println("Yay 1");
        }
        
        
        if (game.clickAt(-1,0) == -2 && game.clickAt(3,3) == 3 &&
        	game.isVisible(3,3) && !game.isVisible(0,0) && 
        	game.getStatus().equals("IN_GAME") && game.mineLeft() == 3){
        	System.out.println("Yay 2");
        }
        
        if (game.clickAt(2,3) == -1 && game.isVisible(2,3) &&
        	game.getStatus().equals("EXPLODED") ){
        	System.out.println("Yay 3");
        }
        
		random = new Random(10);
        game = new MineSweeper(random.nextInt(),Level.TINY);
        game.clickAt(3,3);
        
        
        if (game.flagAt(2,3) && !game.isVisible(2,3)  &&
        	game.isFlagged(2,3) && game.flagAt(2,4) && 
        	game.mineLeft() == 1 && game.unFlagAt(2,3) &&
        	!game.isFlagged(2,3) && game.mineLeft() == 2){
        	System.out.println("Yay 4");
        }
        
        
		if (game.clickAt(2,4) == -2 && game.flagAt(2,4) &&
			!game.flagAt(3,3) && !game.unFlagAt(3,3) &&
			!game.unFlagAt(2,3)){
        	System.out.println("Yay 5");
        }
        
		
		if (game.clickAt(0,0) == 0 && game.isVisible(0,0) && game.isVisible(4,0) &&
			game.isVisible(0,4) && game.isVisible(3,2) && !game.isVisible(3,4) &&
			!game.isVisible(4,3)){
        	System.out.println("Yay 6");
        }
        
       
		
		//open all none-mine cells without any explosion solve the game!
		if (game.clickAt(4,4) == 1 && game.clickAt(3,4) == 3 && 
			game.getStatus().equals("SOLVED")){
        	System.out.println("Yay 7");
        }
		
    } 

}