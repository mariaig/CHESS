package piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import enums.Color;
import game.Move;

public class Bishop extends Piece {
	
	// actual value of the piece
	public static final int score = 500;
	
	// value of the piece depending on the position on the table
	final int positionScore[][] = {
		{-20,-10,-10,-10,-10,-10,-10,-20},
		 {-10,  0,  0,  0,  0,  0,  0,-10},
		 {-10,  0,  5, 10, 10,  5,  0,-10},
		 {-10,  5,  5, 10, 10,  5,  5,-10},
		 {-10,  0, 10, 10, 10, 10,  0,-10},
		 {-10, 10, 10, 10, 10, 10, 10,-10},
		 {-10,  5,  0,  0,  0,  0,  5,-10},
		 {-20,-10,-10,-10,-10,-10,-10,-20}
		 };

	public Bishop(Color color) {
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


		List<Integer> relX = new ArrayList<Integer>();
		List<Integer> relY = new ArrayList<Integer>();
		
		// vector of relative positions that a Bishop could have after a move is made
		relX.addAll(Arrays.asList(-1,-1,1,1));
		relY.addAll(Arrays.asList(-1,1,-1,1));
		
		List<Move> moves = new ArrayList<Move>();

		int k = 1;
		while(!relX.isEmpty()){
			
			for(int index = 0; index < relX.size(); index++){
				int ki = i + k * relX.get(index), kj = j + k * relY.get(index);

				if( (ki >= 0 && ki < 8) && (kj >= 0 && kj < 8) ){
					
					if(board[ki][kj] == null){
						moves.add(new Move(i,j,ki,kj));
					}else if( board[ki][kj] != null && board[ki][kj].color != board[i][j].color){
						// we have encountered the opponent's piece...so we have to stop
						
						moves.add(new Move(i, j, ki,kj));
						relX.remove(index);
						relY.remove(index);
						index--;
					}else if( board[ki][kj] != null && board[ki][kj].color == board[i][j].color) {
						// we have encountered one of our pieces.... stop
						
						relX.remove(index);
						relY.remove(index);
						index--;
					}
				}else{
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
