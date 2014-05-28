package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import openings.Openings;

import enums.*;
import evaluator.Evaluator;
import evaluator.Pair;
import piece.*;

/**
 * Entity that encapsulates the actual game: board, active player, engine's color etc.
 * @author Team Rocket
 */
public class Game {
	private Piece[][] board;
	private Mode mode;
	private Color engineColor;
	private Color activePlayer;
	
	// positions of the kings of each player( used in the 'engineInCheck' function)
	private int kingXU;
	private int kingYU;
	
	private int kingXO;
	private int kingYO;
	
	private String history;
	
	private int INFINITY = 20000;
	
	public Game() {
		this(Color.BLACK);
	}
	
	/**
	 * Creates a new game where the engine plays a specific color
	 * @param engineColor
	 */
	public Game(Color engineColor) {
		setActivePlayer(Color.WHITE);
		setMode(Mode.xboard);
		setEngineColor(engineColor);
		setBoard();
		setActivePlayer(Color.WHITE);
		history = "";
	}
	
	/**
	 * Function that makes a given move on the board taken as parameter
	 * @param board
	 * @param move
	 */
	void doMove(Piece[][] board, Move move, Color playerColor){
		
		if(board[move.initialX][move.initialY] instanceof Pawn && move.finalX == 7){
			board[move.finalX][move.finalY] = new Queen(playerColor);
		}else{
			board[move.finalX][move.finalY] = board[move.initialX][move.initialY];
		}
		board[move.initialX][move.initialY] = null;
	}
	
	
	/**
	 * Function that given a move knows how to interpret it and transform the indexes into a String so
	 * 		that Winboard understands it 
	 * @param move
	 * @return
	 */
	String moveToWinboard(Move move){
		String winMove;
		if(engineColor==Color.BLACK){
			winMove = blackIndexToPosition(move.initialX, move.initialY)+blackIndexToPosition(move.finalX, move.finalY);
		}else 
			winMove = whiteIndexToPosition(move.initialX, move.initialY)+whiteIndexToPosition(move.finalX, move.finalY);
		
		return winMove;
	}
	
	/**
	 * Function that generates all the possible moves of a specific player given a state of the board
	 * @param playerColor
	 * @param board
	 * @return
	 */
	public List<Move> generateMoves(Color playerColor, Piece[][] board){
		List<Move> allMoves = new ArrayList<Move>();
		List<Move> pieceMoves = new ArrayList<Move>();
		
		// the engine generates all possible moves that verify the fact that after that it won't be in mate 
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j]!= null && board[i][j].color == playerColor){
					pieceMoves = board[i][j].validMove(i, j, board);
					
					for(Move move : pieceMoves){
						
						// a copy of the board is made so the hypothetical move to be made and verified
						Piece[][] boardCopy = cloneBoard(board);
						doMove(boardCopy, move, playerColor);
						
						// the position of the king is saved for further use
						int kingX, kingY;
						if(playerColor == engineColor){
							kingX = kingXU;
							kingY = kingYU;
						}else{
							kingX = kingXO;
							kingY = kingYO;
						}
						
						if(board[move.initialX][move.initialY] instanceof King  && playerColor == engineColor){
							kingXU = move.finalX;
							kingYU = move.finalY;	
						}else{
							kingXO = move.finalX;
							kingYO = move.finalY;	
						}
						
						// if the current move doesn't put the engine in mate then it is added in the list of possible moves
						if(!engineInCheck(engineColor, boardCopy, playerColor)){
								allMoves.add(move);
						}
						
						// the king position is reset to the old value
						if(playerColor == engineColor){
							kingXU = kingX;
							kingYU = kingY;
						}else{
							kingXO = kingX;
							kingYO = kingY;
						}
						
						
					}
					
					pieceMoves.clear();
				}
			}
		}
		
		return allMoves;
		
	}
	
	/**
	 * Function that implements the negamax algorithm (standard negamax)
	 * @param boardState
	 * @param depth
	 * @param playerColor
	 * @return
	 */
	public Pair<Integer, Move> negamax(Piece[][] boardState, int depth, Color playerColor){

			if(depth == 0)
				return new Pair<Integer, Move>(Evaluator.evaluateBoard(boardState, playerColor, engineColor, this), null);
			
			Integer max = Integer.MIN_VALUE/2;
			Pair<Integer, Move> maxPair = new Pair<Integer, Move>(max, null);
			
			// generate all possible moves of the current player
			ArrayList<Move> moves = (ArrayList<Move>)generateMoves(playerColor, boardState);
			
			if(moves.isEmpty()) return new Pair<Integer, Move>(max, null);
			
			// iterate through all moves and pick the best one
			for(Move move : moves){
				
				Color oppositeColor = playerColor == Color.BLACK? Color.WHITE : Color.BLACK;
				
				// a copy of the board is made so the hypothetical move to be made
				Piece[][] boardCopy = cloneBoard(boardState);
				doMove(boardCopy, move, playerColor);
				
				// call recursively the negamax function
				Pair<Integer, Move> value =  negamax(boardCopy, depth - 1, oppositeColor);			
				Integer score = (-1)* value.first;

				// return from the recursion and verify if the current move is the best one until now
				//		if so, the move is saved
				if(score > max){
					max = score;
					maxPair = value;
					maxPair.setSecond(move);
				}
				
			}
			return maxPair;
	}
	
	/**
	 * Function that implements the alpha beta pruning algorithm (standard)
	 * @param boardState
	 * @param depth
	 * @param playerColor
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public Pair<Integer, Move> alphabeta(Piece[][] boardState, int depth, Color playerColor, int alpha, int beta){
		if(depth == 0){
			return new Pair<Integer, Move>(Evaluator.evaluateBoard(boardState, playerColor, engineColor, this), null);
		}
		
		ArrayList<Move> moves = (ArrayList<Move>)generateMoves(playerColor, boardState);

		// if the list of possible moves that do not put the player in check them the current state of the board should 
		// 	not be reached
		if(moves.isEmpty()){
			if(playerColor == engineColor){
				return new Pair<Integer, Move>(-INFINITY, null);
			}else{
				return new Pair<Integer, Move>(INFINITY, null);
			}
		}
		
		Move bestWorstMove = new Move();
		
		for(Move move : moves){
			Color oppositeColor = playerColor == Color.BLACK? Color.WHITE : Color.BLACK;
			
			// a copy of the board is made so the hypothetical move to be made and verified
			Piece[][] boardCopy = cloneBoard(boardState);
			doMove(boardCopy, move, playerColor);
			
			// go in recursivity
			Pair<Integer, Move> value =  alphabeta(boardCopy, depth-1, oppositeColor , -beta, -alpha);
			int score = -(int) value.first;
			
			// cut-off
			if(score >= beta){
				return new Pair<Integer, Move>(beta, move);
			}
			
			if( score > alpha){
        		alpha = score;
        		bestWorstMove = move;
        	}
		}
		
		return new Pair<Integer, Move>(alpha, bestWorstMove);
	}

	public void writeToFile(String ss){
		File f = new File("history.txt");
		
		
		try{
			if(!f.exists()){
				f.createNewFile();
			}
			
			FileWriter fw = new FileWriter(f, true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(ss + "\n");
			bw.close();
			
		}catch(Exception e){
			
		}
	}

	/**
	 * Function that finds the next valid move of the engine, one that would not put him in check
	 * 		In the other case, when there are no more valid moves to be made the function returns
	 * 		stalemate or mate, depending on the result
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws FileNotFoundException 
	 */
	public String nextMove() throws FileNotFoundException, UnsupportedEncodingException{
		
		String nextMove;
		Openings open = new Openings();
		
		// if history is empty then engine is White and we have to choose a random move
		if(history.equals("")){
			nextMove = open.getRandomOpen();
		}else{
			nextMove = open.getOpenningMove(history);
		}
		// the move is valid because it is in the opening list
		if(nextMove.length() != 0){
			history += nextMove + " ";
			Move move;
			
			if(engineColor == Color.BLACK){
				int from = blackPositionToIndex(nextMove.substring(0,2));
				int to=blackPositionToIndex(nextMove.substring(2,4));
				move = new Move(from/10,from%10,to/10,to%10);
			}else{
				int from = whitePositionToIndex(nextMove.substring(0,2));
				int to=whitePositionToIndex(nextMove.substring(2,4));
				move = new Move(from/10,from%10,to/10,to%10);
			}
			
			// if the king is moved than its position is saved for further use in engineInCheck() method
			if(kingXU == move.initialX && kingYU == move.initialY){
				kingXU = move.finalX;
				kingYU = move.finalY;
			}
			
			doMove(board, move, engineColor);
			return nextMove;
			
		}
		
		// the engine first verifies if his current state is mate
		boolean inCheckAtFirst = engineInCheck(engineColor, board, engineColor);
		
		List<Move> allMoves = generateMoves(engineColor, board);
		
	
		// the engine verifies if there are any valid moves to be made and transmits the result to Winboard
		if(allMoves.isEmpty()){
			if(inCheckAtFirst){
				return engineColor == Color.BLACK? "RESULT{ 0-1 {Black mates} }":"RESULT{ 1-0 {White mates} }";
			}else{
				return "RESULT{ 1/2-1/2 {Stalemate} }";
			}
		}

		int inf = Integer.MAX_VALUE/2;
		Pair<Integer, Move> next = alphabeta(board, 4, engineColor, -inf, inf);

		Move move =(Move) next.second;
		
		// if the king is moved than its position is saved for further use in engineInCheck() method
		if(kingXU == move.initialX && kingYU == move.initialY){
			kingXU = move.finalX;
			kingYU = move.finalY;
		}
		
		doMove(board, move, engineColor);
		return moveToWinboard(move);
	
		
	}

	/**
	 * Function that returns the type of the move given as a string (ex normal move, pawn promotion)
	 * Useful in the next stages when will occur more types.
	 * @param move
	 * @return
	 */
	private MovesType moveType(String move){
		if(move.endsWith("q")){
			return MovesType.pawnPromotion;
		}else{
			return MovesType.normal;
		}
	}
	

	/**
	 * Function that checks if the engine's King is in check.
	 * In this stage, it verifies the squares sorrounding the King 
	 * (Basically, if the King is in check by the pawn)
	 * @param color
	 * @return false/true
	 */
	public boolean engineInCheck(Color color, Piece[][] boardCopy, Color playerColor){
		
		int kingX, kingY;
		if(playerColor == engineColor){
			kingX = kingXU;
			kingY = kingYU;
		}else{
			kingX = kingXO;
			kingY = kingYO;
		}
		
		
		
		// the kings must not be close
		int relKing[][]={
				{1,1,1,0,0,-1,-1,-1},
				{-1,0,1,-1,1,-1,0,1}
				};
		
		for(int i = 0 ; i < relKing[0].length; i++){
			int kRelX = kingX + relKing[0][i];
			int kRelY = kingY + relKing[1][i];
			
			if((kRelX>=0 && kRelX<8) 
					&&(kRelY>=0 && kRelY<8)
					&& boardCopy[kRelX][kRelY] != null
					&& boardCopy[kRelX][kRelY] instanceof King 
					&& boardCopy[kRelX][kRelY].color != engineColor){
				return true;
			}
		}
		
		// the engine verifies if the king is attacked by the pawns 
		if(kingX+1<8){
			if((kingY-1)>=0 
					&& boardCopy[kingX+1][kingY-1]!=null
					&& boardCopy[kingX+1][kingY-1] instanceof Pawn 
					&& boardCopy[kingX+1][kingY-1].color!=engineColor){
				return true;
			}
			if((kingY+1)<8
					&& boardCopy[kingX+1][kingY+1]!=null
					&& boardCopy[kingX+1][kingY+1] instanceof Pawn 
					&& boardCopy[kingX+1][kingY+1].color!=engineColor){
				return true;
			}
		}
		
		
		int relative[][] = {{ 2, 2, -2, -2,  1, 1, -1, -1 },
							{-1, 1, -1,  1, -2, 2, -2,  2 }};
		for(int i=0;i<relative[0].length;i++){
			int krelX = kingX + relative[0][i];
			int krelY = kingY + relative[1][i];
			if( (krelX>=0 && krelX<8) 
					&& (krelY>=0 && krelY<8)
					&& boardCopy[krelX][krelY]!=null
					&& boardCopy[krelX][krelY] instanceof Knight
					&& boardCopy[krelX][krelY].color!=engineColor){
				return true;
			}
		}
		
		// distance - the distance to the king
		// 		the verifications are made in concentric circles around the king
		int distance=1;
		
		// the engine verifies if the king is attacked by the rooks or the queen
		System.out.println("verifica r&q");
		ArrayList<Integer> relX = new ArrayList<Integer>();
		ArrayList<Integer> relY = new ArrayList<Integer>();
		relX.addAll(Arrays.asList(-1, 1, 0, 0));
		relY.addAll(Arrays.asList(0, 0, -1, 1));
		
		while(!relX.isEmpty()){
			for(int i=0;i<relX.size();i++){
				
				int krelX = kingX+relX.get(i)*distance;
				int krelY = kingY+relY.get(i)*distance;
				if( (krelX>=0&&krelX<8)
						&&(krelY>=0&&krelY<8)){
					
					if(boardCopy[krelX][krelY] instanceof Rook && boardCopy[krelX][krelY].color!=engineColor){
						return true;
					}
					if(boardCopy[krelX][krelY] instanceof Queen && boardCopy[krelX][krelY].color!=engineColor){
						return true;
					}
					if(boardCopy[krelX][krelY]!=null){
						relX.remove(i);
						relY.remove(i);
						i--;
					}
				}else{
					relX.remove(i);
					relY.remove(i);
					i--;
				}
			}
			distance++;
		}
		
		// the engine verifies if the king is attacked by the bishop or the queen
		relX.clear();
		relY.clear();
		
		relX.addAll(Arrays.asList(-1,-1,1,1));
		relY.addAll(Arrays.asList(-1,1,-1,1));
		distance=1;
		while(!relX.isEmpty()){
			for(int i=0;i<relX.size();i++){
				int krelX=kingX+relX.get(i)*distance;
				int krelY=kingY+relY.get(i)*distance;
				if( (krelX>=0&&krelX<8)
						&&(krelY>=0&&krelY<8)){
					
					if(boardCopy[krelX][krelY] instanceof Bishop && boardCopy[krelX][krelY].color!=engineColor){
						return true;
					}
					if(boardCopy[krelX][krelY] instanceof Queen && boardCopy[krelX][krelY].color!=engineColor){
						return true;
					}
					if(boardCopy[krelX][krelY]!=null){
						relX.remove(i);
						relY.remove(i);
						i--;
					}
				}else{
					relX.remove(i);
					relY.remove(i);
					i--;
				}
			}
			distance++;
		}
		
		return false;
	}
	
	/** 
	 * Function that clones the board at a specific time and returns its copy. 
	 * 		Used in simulating a move and verifying if it lead to mate
	 * @return
	 */
	public Piece[][] cloneBoard(Piece[][] board){
		
		Piece[][] newBoard = new Piece[8][8];
		
		for(int i =0 ; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(board[i][j] == null){
					newBoard[i][j] = null;
				}else{
					newBoard[i][j] = board[i][j].clonePiece();
				}
			}
		}
		return newBoard;
	}
	
	
	/**
	 * Interprets the opponent's move and marks it on the internal board.
	 * @param oppMove
	 * @throws IOException 
	 */
	public void opponentMove(String oppMove) throws IOException{
		
		

		if(moveType(oppMove) == MovesType.normal){
			oppMove = oppMove.substring(oppMove.length()-4, oppMove.length());
		}else{
			oppMove = oppMove.substring(oppMove.length()-5, oppMove.length() - 1);
		}
		history += oppMove + " ";
		
		int from;
		int to;
		if(engineColor==Color.BLACK){
		 from = blackPositionToIndex(oppMove.substring(0, 2));
		 to = blackPositionToIndex(oppMove.substring(2,4));
		} else{
			from = whitePositionToIndex(oppMove.substring(0, 2));
			to = whitePositionToIndex(oppMove.substring(2,4));
		}

		Piece p = board[from / 10][from % 10];
		
		boolean castlingShort = false;
		boolean castlingLong = false ;
		
		if(p instanceof King){
			kingXO = to/10;
			kingYO = to%10;
		}
		
		// Conditions are being put so the engine can verify what kind of move has received from Winboard
		if (p instanceof King && p.notMoved == true 
				&& board[7][7] != null && board[7][7].notMoved == true){
				// the opponent makes a castling Short
			
			castlingShort = true;
			
			
		}else if (p instanceof King && p.notMoved == true 
				&& board[7][0] != null && board[7][0].notMoved == true){
				// the opponent makes a castling Long
			
			castlingLong = true;
			
		}else if (p instanceof Pawn && board[to / 10][to % 10] == null
				&& (from / 10 == 3) && (to / 10 == 2)){
				// an "en passant" move is being made by the opponent
			
				if (board[to / 10 + 1][to % 10] instanceof Pawn){
					board[to / 10 + 1][to % 10] = null; 
					board[to / 10][to % 10] = p;
				}
		
		}
		
		if(to == 76 && castlingShort == true && (board[from/10 ][(from%10)+1] == null)
				&&(board[from/10][(from%10) + 2] == null) ){
			// the castling short move is being processed
			
			board[7][6] = p;
			board[7][5] = board[7][7];
			board[7][7] = null;
		}else if(to ==72 && castlingLong == true && (board[from/10 ][(from%10)-1]==null)
				&&(board[from/10][(from%10)-2]==null) && (board[from/10 ][(from%10)-3]==null)){
			// the castling long  move is being processed
			
			board[7][2] = p;
			board[7][3] = board[7][0];
			board[7][0] = null;
		}else{
			// a regular move is being processed
			if( (p instanceof Pawn) && ((to/10) == 0)){
				board[to/10][to % 10] = engineColor == Color.WHITE? new Queen(Color.BLACK) : new Queen(Color.WHITE);
			}else{
				board[to/10][to % 10] = p;
			}
			if(p instanceof King || p instanceof Rook) {
				p.notMoved = false;
			}
		}
		
		board[from / 10][from % 10] = null;

	}
	

	
	/**
	 * Is used when the engine plays the black colour.
	 * Transforms the internal representation of the piece on the board into a string that can be sent back to Winboard.
	 * (Example: 00 -> a8, 77 -> h1)
	 * @param i
	 * @param j
	 * @return
	 */
	private String blackIndexToPosition(int i,int j){
		return (char)((int)'a'+j)+Integer.toString(8-i);
	}
	/**
	 * Same as IndexToPosition, but reversed.
	 * (Example: a8 -> 00, h1 -> 77)
	 * @param poz
	 * @return
	 */
	private int blackPositionToIndex(String poz){
		char[] position=poz.toCharArray();
		return (8-((int)position[1]-48))*10 + ((int)(position[0]) -97) ;
	}
	
	/**
	 * Is used when the engine plays the white colour.
	 * Transforms the internal representation of the piece on the board into a string that can be sent back to Winboard.
	 * (Example: 00 -> h1, 77 -> a8)
	 * @param i
	 * @param j
	 * @return
	 */
	public static String whiteIndexToPosition(int i, int j){
		return (char)((int)'a' + 7 - j) + Integer.toString(i+1);
	}
	
	/**
	 * Same as IndexToPosition, but reversed.
	 * (Example: h1 -> 00, a8 -> 77)
	 * @param poz
	 * @return
	 */
	public static int whitePositionToIndex(String poz ){
		char[] position=poz.toCharArray();
		return ((int)position[1] - 49)*10 + (104-(int)position[0]);
	}
	
	
	
	/**
	 * Creates a new internal representation of the board, depending on the colour.
	 * It's used when the user begins a new game.
	 */
	private void setBoard() {
		board = new Piece[8][8];
		Color colorOpponent;
		if (engineColor == Color.BLACK){
			colorOpponent = Color.WHITE;
			board[0][3] = new Queen(engineColor);
			board[0][4] = new King(engineColor);
			kingXU = 0;
			kingYU = 4;
			
			kingXO = 7;
			kingYO = 4;
			
			board[7][3] = new Queen(colorOpponent);
			board[7][4] = new King(colorOpponent);
		}
		else{
			colorOpponent = Color.BLACK;
			board[0][4] = new Queen(engineColor);
			board[0][3] = new King(engineColor);
			kingXU = 0;
			kingYU = 3;
			kingXO = 7;
			kingYO = 3;
			
			board[7][4] = new Queen(colorOpponent);
			board[7][3] = new King(colorOpponent);
			
		}
			
		board[0][0] = new Rook(engineColor);
		board[0][1] = new Knight(engineColor);
		board[0][2] = new Bishop(engineColor);
		board[0][5] = new Bishop(engineColor);
		board[0][6] = new Knight(engineColor);
		board[0][7] = new Rook(engineColor);
			
		board[7][0] = new Rook(colorOpponent);
		board[7][1] = new Knight(colorOpponent);
		board[7][2] = new Bishop(colorOpponent);
		board[7][5] = new Bishop(colorOpponent);
		board[7][6] = new Knight(colorOpponent);
		board[7][7] = new Rook(colorOpponent);
		
		for (int i = 0; i <= 7; i++) {
			board[1][i] = new Pawn(engineColor);
			board[6][i] = new Pawn(colorOpponent);
		}
	}
	
	public Piece[][] getBoard() {
		return board;
	}
	
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Color getEngineColor() {
		return engineColor;
	}
	public void setEngineColor(Color engineColor) {
		this.engineColor = engineColor;
	}
	public void setActivePlayer(Color activePlayer) {
		this.activePlayer = activePlayer;
	}
	public Color getActivePlayer() {
		return this.activePlayer;
	}
	
}