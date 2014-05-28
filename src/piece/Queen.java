package piece;

import enums.Color;
import game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queen extends Piece {
	
	// actual value of the piece
	public static final int score = 5000;
	
	// value of the piece depending on the position on the table
	final int positionScore[][] = {
			{-20,-10,-10, -5, -5,-10,-10,-20},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{-5,  0,  5,  5,  5,  5,  0, -5},
			{-5,  0,  5,  5,  5,  5,  0, -5},
			{-10,  0,  5,  5,  5,  5,  0,-10},
			{-10,  0,  0,  0,  0,  0,  0,-10},
			{-20,-10,-10, -5, -5,-10,-10,-20}
		};
	
	public int getScore(int i,int j){
		return positionScore[i][j];
	}
	public int getValue(){
		return score;
	}
	
	public Queen(Color color) {
		super(color);
	}

	@Override
	public List<Move> validMove(int i, int j, Piece[][] board) {
		
		List<Integer> relX = new ArrayList<Integer>();
		List<Integer> relY = new ArrayList<Integer>();
		
		relX.addAll(Arrays.asList(-1, -1, 1, 1));
		relY.addAll(Arrays.asList(-1, 1, -1, 1));
		
		// The Queen acts also as the Rook and the Bishop...so there should be made the same conditions 
		// 		as for the other pieces

		List<Move> moves = new ArrayList<Move>();

		int k = 1;
		while (!relX.isEmpty()) {

			for (int index = 0; index < relX.size(); index++) {
				int ki = i + k * relX.get(index), kj = j + k * relY.get(index);

				if ((ki >= 0 && ki < 8) && (kj >= 0 && kj < 8)) {
					if (board[ki][kj] == null) {
						moves.add(new Move(i, j, ki, kj));
					} else if (board[ki][kj] != null
							&& board[ki][kj].color != board[i][j].color) {
						// we have encountered the opponent's piece...so we have
						// to stop
						
						moves.add(new Move(i, j, ki, kj));
						relX.remove(index);
						relY.remove(index);
						index--;
					} else if (board[ki][kj] != null
							&& board[ki][kj].color == board[i][j].color) {
						// we have encountered one of our pieces.... stop
						
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

		relX.clear();
		relY.clear();

		relX.addAll(Arrays.asList(-1, 1, 0, 0));
		relY.addAll(Arrays.asList(0, 0, -1, 1));

		k = 1;

		while (!relX.isEmpty()) {

			for (int index = 0; index < relX.size(); index++) {
				int ki = i + k * relX.get(index), kj = j + k * relY.get(index);
				if ((ki >= 0 && ki < 8) && (kj >= 0 && kj < 8)) {
					if (board[ki][kj] == null) {
						moves.add(new Move(i, j, ki, kj));
					} else if (board[ki][kj] != null
							&& board[ki][kj].color != board[i][j].color) {
						// we have encountered the opponent's piece...so we have
						// to stop
						
						moves.add(new Move(i, j, ki, kj));
						relX.remove(index);
						relY.remove(index);
						index--;
					} else if (board[ki][kj] != null
							&& board[ki][kj].color == board[i][j].color) {
						// we have encountered one of our pieces.... stop
						
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
