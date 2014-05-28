package piece;

import java.util.ArrayList;
import java.util.List;

import enums.Color;
import game.Move;

public class King  extends Piece{
	// actual value of the piece
	public static final int score = 20000;
	
	// value of the piece depending on the position on the table
	final int positionScore[][]={
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-30,-40,-40,-50,-50,-40,-40,-30},
			{-20,-30,-30,-40,-40,-30,-30,-20},
			{-10,-20,-20,-20,-20,-20,-20,-10},
			{20, 20,  0,  0,  0,  0, 20, 20},
			{20, 30, 10,  0,  0, 10, 30, 20}
	};
	public int getScore(int i,int j){
		return positionScore[i][j];
	}
	public int getValue(){
		return score;
	}
	// vector of relative positions that a King could have after a move is made
	int relative[][]={
			{1,1,1,0,0,-1,-1,-1},
			{-1,0,1,-1,1,-1,0,1}
			};

	public King(Color color) {
		super(color);
	}

	@Override
	public List<Move> validMove(int i, int j, Piece[][] board) {
		List<Move> moves=new ArrayList<Move>();
		for(int inc=0;inc<relative[0].length;inc++){
			int auxi=i+relative[0][inc];
			int auxj=j+relative[1][inc];
			
			if( auxi>=0 && auxi<8 && auxj>=0 && auxj<8){
				if(board[auxi][auxj]==null || board[auxi][auxj].color!=board[i][j].color)
					moves.add(new Move(i, j, auxi, auxj));
			}
		}
		return moves;
	}

}
