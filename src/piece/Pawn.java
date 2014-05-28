package piece;
import java.util.ArrayList;
import java.util.List;

import enums.Color;
import game.*;

public class Pawn extends Piece {
	
	// actual value of the piece
	public static final int score = 100;
	
	// value of the piece depending on the position on the table
	final int positionScore[][] = {
			{0,  0,  0,  0,  0,  0,  0,  0},
			{50, 50, 50, 50, 50, 50, 50, 50},
			{10, 10, 20, 30, 30, 20, 10, 10},
			{5,  5, 10, 25, 25, 10,  5,  5},
			{0,  0,  0, 20, 20,  0,  0,  0},
			{5, -5,-10,  0,  0,-10, -5,  5},
			{5, 10, 10,-20,-20, 10, 10,  5},
			{0,  0,  0,  0,  0,  0,  0,  0}
		};
	
	public Pawn(Color color) {
		super(color);
	}
	
	public int getScore(int i,int j){
		return positionScore[i][j];
	}
	public int getValue(){
		return score;
	}
	
	@Override
	public List<Move> validMove(int i, int j, Piece[][] board) {
		
		List<Move> moves = new ArrayList<Move>();
		
		//Requirements for left enemy piece capture.
		if((j + 1) < 8 && board[i+1][j+1] != null ){
			if(board[i+1][j+1].color != board[i][j].color)
				moves.add(new Move(i,j,i+1,j+1));
		}
		//Requirements for right enemy piece capture.
		if((j - 1) >= 0 &&  board[i+1][j-1] != null){
			if(board[i+1][j-1].color != board[i][j].color)
				moves.add(new Move(i,j,i+1,j-1));
		}
		if (i == 1) {
			//When of the second line, the pawn moves two squares (if he has place) or one square.
			if (board[i + 2][j] == null && board[i+1][j]==null) {
				moves.add(new Move(i,j,i+2,j));
			}
		} 
		if ((i+1)<8 && board[i + 1][j] == null){
			//Pawn moves one square (if the square is empty).
			moves.add(new Move(i,j,i+1,j));
		}
		
		return moves;
	}
	
	
}