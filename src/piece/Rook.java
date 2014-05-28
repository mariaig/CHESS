package piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.Color;
import game.Move;

public class Rook extends Piece{
	
	// actual value of the piece
	public static final int score = 500;
	
	// value of the piece depending on the position on the table
	final int positionScore[][] = {
			{0,  0,  0,  5,  5,  0,  0,  0},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{-5,  0,  0,  0,  0,  0,  0, -5},
			{5, 10, 10, 10, 10, 10, 10,  5},
			{0,  0,  0,  0,  0,  0,  0,  0}
			};

	public int getScore(int i,int j){
		return positionScore[i][j];
	}
	
	public int getValue(){
		return score;
	}
	public Rook(Color color) {
		super(color);
	}
	
	
	@Override
	public List<Move> validMove(int i, int j, Piece[][] board) {
		List<Move> moves = new ArrayList<Move>();

		ArrayList<Integer> relX = new ArrayList<Integer>();
		ArrayList<Integer> relY = new ArrayList<Integer>();

		// relative positions of a Rook after making a move
		relX.addAll(Arrays.asList(-1, 1, 0, 0));
		relY.addAll(Arrays.asList(0, 0, -1, 1));

		int k = 1;

		
		while (!relX.isEmpty()) {

			for (int index = 0; index < relX.size(); index++) {
				
				int ki = i + k * relX.get(index), kj = j + k * relY.get(index);
				
				if ((ki >= 0 && ki < 8) && (kj >= 0 && kj < 8)) {

					if (board[ki][kj] == null) {
						moves.add(new Move(i, j, ki, kj));
						
					} else if (board[ki][kj] != null
							&& board[ki][kj].color != board[i][j].color) {
						// we have encountered the opponent's piece...so the engine should stop from 
						//		searching a valid move in that direction
						
						moves.add(new Move(i, j, ki, kj));
						relX.remove(index);
						relY.remove(index);
						index--;
						
					} else if (board[ki][kj] != null
							&& board[ki][kj].color == board[i][j].color) {
						// we have encountered one of our pieces.... the engine in this case stops from searching
						
						relX.remove(index);
						relY.remove(index);
						index--;
					}
				} else {
					relX.remove(index);
					relY.remove(index);
					index--;
				}
			}
			k++;
		}

		return moves;
	}

}
