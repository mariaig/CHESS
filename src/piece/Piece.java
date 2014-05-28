package piece;
import java.util.List;

import enums.Color;
import game.Move;


/**
 * Abstract class Piece that defines the behavior of every type of piece used in chess
 * @author Team Rocket
 *
 */
public abstract class Piece {
	public Color color;
	public boolean notMoved;

	Piece (Color color){
		this.color = color;
		this.notMoved = true;
	}
		
	/**
	 * Method that returns the first valid move of the piece that has the coordinates (i,j)
	 *  on the internal representation of the board
	 * @param i
	 * @param j
	 * @param board
	 * @return  -1 if there is no valid move available/ i * 10 + j if the piece can move to board[i][j]
	 */
	public abstract List<Move> validMove(int i, int j, Piece[][] board);
	
	/**
	 * Method that returns the piece's score depending on the position on the board
	 * @param i
	 * @param j
	 * @return
	 */
	public abstract int getScore(int i,int j);
	
	/**
	 * Method that returns the actual value of a piece depending on its type
	 * @return
	 */
	public abstract int getValue();
	
	/**
	 * Method that creates a clone of an object of type Piece depending on its sub-type
	 * @return
	 */
	public Piece clonePiece(){
		
		if(this instanceof Pawn){
			return new Pawn(color);
		}else if(this instanceof Bishop ){
			return new Bishop(color);
		}else if(this instanceof King ){
			return new King(color);
		}else if(this instanceof Knight ){
			return new Knight(color);
		}else if(this instanceof Queen ){
			return new Queen(color);
		}else{
			return new Rook(color);
		}
	}
	
}
