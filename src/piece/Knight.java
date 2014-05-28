package piece;

import java.util.ArrayList;
import java.util.List;

import enums.Color;
import game.Move;

public class Knight extends Piece {
	
	// actual value of the piece
	public static final int score = 320;
	
	// value of the piece depending on the position on the table
	final int positionScore[][] = {
			{-50,-40,-30,-30,-30,-30,-40,-50},
			 {-40,-20,  0,  0,  0,  0,-20,-40},
			 {-30,  0, 10, 15, 15, 10,  0,-30},
			 {-30,  5, 15, 20, 20, 15,  5,-30},
			 {-30,  0, 15, 20, 20, 15,  0,-30},
			 {-30,  5, 10, 15, 15, 10,  5,-30},
			 {-40,-20,  0,  5,  5,  0,-20,-40},
			 {-50,-40,-30,-30,-30,-30,-40,-50}
		};
	public int getScore(int i,int j){
		return positionScore[i][j];
	}
	
	public int getValue(){
		return score;
	}		
	// vector of relative positions that a Knight could have after a move is made
	int relative[][] = {{ 2, 2, -2, -2,  1, 1, -1, -1 },
						{-1, 1, -1,  1, -2, 2, -2,  2 }};

	
	public Knight(Color color) {
		super(color);
	}

	@Override
	public List<Move> validMove(int i, int j, Piece[][] board) {
		List<Move> moves = new ArrayList<Move>();
		
		int auxi, auxj;
		for (int k = 0; k < 8; k++){
			
			auxi = i + relative[0][k];
			auxj = j + relative[1][k];
			if ((auxi < 8 && auxi >= 0) && (auxj < 8 && auxj >= 0)){
				
				if (board[auxi][auxj] != null){
					if (board[auxi][auxj].color != board[i][j].color)
						moves.add(new Move(i, j, auxi, auxj));
				}
				else{
					moves.add(new Move(i, j, auxi, auxj));
				}
			}
		}
		return moves;
	}

}
