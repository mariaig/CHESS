package game;

/**
 * Entity that encapsulates a move: initial position -> final position
 * @author Team Color
 *
 */
public class Move {

	public int initialX;
	public int initialY;
	
	public int finalX;
	public int finalY;
	
	public Move(){}
	public Move(int initialX, int initialY, int finalX, int finalY) {
		super();
		this.initialX = initialX;
		this.initialY = initialY;
		this.finalX = finalX;
		this.finalY = finalY;
	}


	@Override
	public String toString() {
		return "Move [initialX=" + initialX + ", initialY=" + initialY
				+ ", finalX=" + finalX + ", finalY=" + finalY + "]";
	}
	
	
}
