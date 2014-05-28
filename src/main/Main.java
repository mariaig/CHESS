package main;

import enums.*;
import game.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Team Rocket
 *
 */
public class Main {
	
	/**
	 * Entry point - set up the connection with Winboard
	 * 			   - waits for commands from Winboard and also sends when it's engine's
	 * 					turn to move
	 * @author Team Rocket
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader (System.in));
		System.out.println("feature usermove=1");
		
		String command, move = "";
		boolean vsComputer = false;
		Game g = new Game();
		
		while(true){
			try {      
				// read the command from Winboard
				command = in.readLine();

				if(command.equals("new")){
					g = new Game();
	
				}else if(command.equals("computer")){
					// Tell the engine that it is playing vs a computer program
					vsComputer = true;
					
				}else if(command.equals("xboard")){
					// Set the engine on xboard mode
					g.setMode(Mode.xboard);
				
				}else if(command.equals("force")){
					// Set the engine on force mode
					g.setMode(Mode.force);
		
				}else if(command.contains("go")){
					// Leave force mode and set the engine to play the color that is on move.
					g.setMode(Mode.xboard);
					if(vsComputer == true){
						if(g.getEngineColor() == g.getActivePlayer()){
							transmitNextMoveToWinboard(g);
						}else{
							g.opponentMove(move);
							transmitNextMoveToWinboard(g);
						}
						vsComputer = false;
					}else{
							transmitNextMoveToWinboard(g);
					}
								
				}else if(command.startsWith("usermove")){
					if(vsComputer == true){
						move = command;
					}else{
						g.opponentMove(command);
						transmitNextMoveToWinboard(g);
					}
					
				}else if(command.contains("white")){
					// Set the engine to play white. Set white on move
			        g=new Game(Color.WHITE);
				
				}else if(command.contains("black")){
					//Set the engine to play black. Set white on move
					g=new Game(Color.BLACK);
					
				}else if(command.equals("resign")){
					//The opponent resigns.
					break;
					
				}else if(command.equals("quit")){
					// The chess engine exits
					break;
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Method used to print to the standard output the engine's next available move
	 * @param g
	 * @throws IOException 
	 */
	private static void transmitNextMoveToWinboard(Game g) throws IOException{
		String output=g.nextMove();
		if(output.equals("resign")){
			System.out.println("resign");
		}
		System.out.println("move "+output);
	}
}