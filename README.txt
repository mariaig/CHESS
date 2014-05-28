@author Team Rocket
@project CHESS - stage 1

	Structure
		Team Rocket has structured its project into 4 main packages:
			piece - classes that represent the types of pieces used in a chess game
						(King, Queen, Pown etc)
			game - contains the game class that encapsulates all the proprieties that a 
						game of chess could have
			enums - enumerations used (Color, Mode, Move)
			main - contains the Main class - the entry point in the project(the connection 
						is set up, the game is started etc)
						
	****************************************************************************************
	
	Internal representation of the board
		Team Rocket has chosen to represent the board as follows:
			* the board is a 8x8 matrix of Piece where Piece could be of every type King, Queen etc
				and the empty space is represented by null
			* always the engine's pieces are set at the top of the board, whether it is 
				playing white or black
			* the algorithm is the same for both situations only the transformations from index 
				to positions used in chess(e4, h6) are different
	
	****************************************************************************************
		
	Solving approach
		Team Rocket decided that the Pawn would be best piece to choose to move until there are no more
	valid moves to make. 
		We have a simple approach: 
			* first verify if the engine is in check, if true then it should resign
			* iterate through the matrix step by step 
				* find a pawn
				* if it can make a valid move then return the move
			* if all pieces were verified and there are no valid moves for the pawns the engine resigns
		We are aware that the algorithm is as basic as possible but we focused on setting up
	the communication with Winboard
	****************************************************************************************
	
	Development environment
		IDE - Eclipse (various versions depending on the team member)
		Winboard 4.7.3
		We have tested our engine both as the first engine and the second one versus the Pulsar engine that
	was installed with the Winboard
	
	****************************************************************************************
	
	Work management
		Giving the fact that the goal of stage 1 of this project is to get used to Winboard and 
	the way that the engine developed by our team communicates with it we have chosen not to work 
	independently or divide the work but to get together and come up with the best way to structure 
	the project etc.

	****************************************************************************************
	
	Archive
		We have included in the project archive the following:
			* the sources structured as packages
			* README
			* a jar file representing the jar generated in eclipse




@project CHESS - stage 2

	Structure
		The Structure of the project has remained the same, there are still the 4 main packages.

	**************************************************************************************************

	Internal representation of the board
		The internal representation of the board has also remained the same.

	****************************************************************************************************

	Solving approach
		Team Rocket has focused its attention in this stage of the project in implementing the 
	methods that return all the valid moves of a specific piece on the board depending on the type
	of it. It may not be the best implementation or the most brilliant but for this stage that was what we could
	done

		Also, team Rocket has implemented the nextMove() method that gives to Winboard the next random move 
	of the engine. In this method, all valid moves are generated and picket a random one to be made

		The checkmate and the stalemate cases are treated...so the engine transmits to Winboard the result

		The most difficult part of he implementation was to make a good method that checks whether or not
	the engine having a specific board it is in check or not. Also,a problem was in simulating all the  moves on a
	clone of the board, because there was some side effects.

	********************************************************************************************************

	Development environment
		IDE - Eclipse (various versions depending on the team member)
		Winboard 4.7.3
		We have tested our engine both as the first engine and the second one. Also we have tested our engine
	vs our engine. It works kinda ok.. :)
	**********************************************************************************************************

	Work management
		As in the previous stage we have chosen to get together and try to work as a team. In our opinion this
	is the best approach.

	**********************************************************************************************************

	Archive
		We have included:
			* the sources structured as packages
			* README
			
			
@project CHESS - stage 3

	Structure
		Beside the packages that ware included until this stage, there is a new one:
			evaluator - contains 2 classes that help evaluating a board given a specific state of it
					  - Pair, Evaluator
	**********************************************************************************************************
	
	Internal representation of the board
		Team Rocket has chosen to add for each type of Piece(Rook, Pawn etc) two variables that define its 
	value depending on the position on the board or on its type. These variables are used in the Evaluator 
	when the board is evaluated by its pieces.
	**********************************************************************************************************
	
	Solving approach
		Team Rocket's focus, this stage, was on implementing draft of the evaluation function and the Negamax
	algorithm.
		First of all, Team Rocket has been documented what is the best approach on evaluation the state of the 
    board and has used the approach explained here[1] as a base line. 
		We have implemented a standard Negamax algorithm that was incorporated in 'nextMove' function. 
	As an addition, after generating all the possible moves the engine verifies if in such a state the engine is
	is check or not and if so, the value returned is the minimum possible.
		Even though, at the moment our implementation is not the best one, we are aware that for the next stages
	we have to change drastically the approach.
	
	[1] http://chessprogramming.wikispaces.com/Simplified+evaluation+function 
	**********************************************************************************************************
	
	Development environment
		IDE - Eclipse (various versions depending on the team member)
		Winboard 4.7.3
		For this stage, we have tested our engine vs Pulsar (from the installation package) but also vs itself.
	Obviously, our engine is check-mated by Pulsar every time. :( 
	**********************************************************************************************************
		
	Work management
		Team Rocket works as a team: gets together, makes a plan on what should be the approach and then 
	divides the work. We work well when we have constant feedback from each other so we have chosen to 
	implement everything only when we gather.
	**********************************************************************************************************
	
	Archive
		We have included:
			* the sources structured as packages
			* README

@project CHESS - stage 4 & 5

	Sructure
		
	**********************************************************************************************************
	Internal representation of the board

		The internal representation of the board is the same as it was at stage 3.

	**********************************************************************************************************		
	Solving approach
		
		Besides the Negamax implemented at stage 3, we have developed an alpha beta pruning algorithm and a more 
	effective evaluation function.
		Team Rocket has incorporated the alpha beta  pruning algorithm in the 'nextMove' function. 
		We have changed our approach and we have tried to obtain better results in a more convenient time.
		
		The evaluation function takes into consideration several parameters of a board:
			** the mobility of each player -  the number of possible moves
			** the positions of the pieces on board.
			** how many pieces of a certain type are still on the board

		Also, we have used some opening lists taken from the Pulsar engine that came with Winboard. We have adapted
	the implementation in the following way:
			** if the engine is WHITE then it generates randomly a first opening move from the file that will be 
		used, if not the engine just looks through the lists of possible scenarios and just extract the next 
		move from it.
			** if in there is no match between the history of moves and the scenarios from the file - 'openings.txt'
		the engine just chooses the best next move by applying the Alpha-beta algorithm.
	**********************************************************************************************************
	Development environment
		IDE - Eclipse (various versions depending on the team member)
		Winboard 4.7.3
		For this stage, we have tested our engine vs Pulsar (from the installation package) but also vs itself.
	Obviously, our engine is check-mated by Pulsar every time. :( 
	**********************************************************************************************************

	Work management
		Team Rocket was glad to work at this project even if the result does not match our expectation.
	**********************************************************************************************************
	
	Archive
		We have included:
			* the sources structured as packages
			* README

