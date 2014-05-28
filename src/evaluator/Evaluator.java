package evaluator;

import java.util.ArrayList;

import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Piece;
import piece.Queen;
import piece.Rook;
import enums.Color;
import game.Game;
import game.Move;
/**
 * Class that is used in the Evaluation process of the board
 * @author Team Rocket
 *
 */
public class Evaluator {

	
	/**
	 * Evaluation function that receives the state of the board at a specific moment and returns a score based
	 * 		on the positions occupied by all the pieces
	 * @param board
	 * @param engineColor
	 * @return
	 */
	public static int evaluateBoard(Piece[][] board,Color playerColor, Color engineColor, Game game){
		int eval = 0;
		int inf = 30000;
		
		// numbers of pieces of each type that are still on the board for each player
		int qp = 0, qo = 0,
			rp = 0, ro = 0,
			bp = 0, bo = 0,
			np = 0, no = 0,
			kp = 0, ko = 0,
			pp = 0, po = 0;
		
		// the number of possible moves that each player can make
		int mobilityP = 0, mobilityO = 0;
		
		if(game.engineInCheck(playerColor, board, playerColor)){
			return engineColor == playerColor? inf : -inf;
		}
		
		
		for(int i = 0;i < 8; i++){
			for(int j = 0;j < 8; j++){
				if(board[i][j] != null){						
					if(board[i][j].color == playerColor){
						eval += (board[i][j].getValue()+board[i][j].getScore(i, j));
						
					}else{
						eval -= (board[i][j].getValue()+board[i][j].getScore(7-i, 7-j));
					}
			
					// incrcement the number of pieces according to its type
					if(board[i][j] instanceof Queen){
						if(board[i][j].color == playerColor){
							qp++;
						}else{
							qo++;
						}
					}else if(board[i][j] instanceof Knight){
						if(board[i][j].color == playerColor){
							np++;
						}else{
							no++;
						}
					}else if(board[i][j] instanceof Rook){
						if(board[i][j].color == playerColor){
							rp++;
						}else{
							ro++;
						}
					}else if(board[i][j] instanceof Bishop){
						if(board[i][j].color == playerColor){
							bp++;
						}else{
							bo++;
						}
					}else if(board[i][j] instanceof Pawn){
						if(board[i][j].color == playerColor){
							pp++;
						}else{
							po++;
						}
					}else if(board[i][j] instanceof King){
						if(board[i][j].color == playerColor){
							kp++;
						}else{
							ko++;
						}
					}
					
				}
			}
		}
		// compute the mobility
		ArrayList<Move> movesP = (ArrayList<Move>)game.generateMoves(playerColor, board);
		ArrayList<Move> movesO;
		if( playerColor == Color.BLACK){
			movesO = (ArrayList<Move>)game.generateMoves(Color.WHITE, board);
		}else{
			movesO = (ArrayList<Move>)game.generateMoves(Color.BLACK, board);
		}
		
		mobilityP = movesP.size();
		mobilityO = movesO.size();
		
		// add the mobility and the scores to the total evaluation score
		eval += King.score * (kp - ko) + Queen.score * (qp - qo) + Rook.score * (rp - ro) + Pawn.score * (pp - po) +
				Bishop.score * (bp - bo) + Knight.score * (np - no);
		
		eval += 200 * (mobilityP - mobilityO);
		
		// return the value according to the color of the player
		return engineColor == playerColor? eval : -eval;
	}
}
