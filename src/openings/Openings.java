package openings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 
 * @author Team Rocket
 * Class that is used in the first period of the game when opening moves are available
 *
 */
public class Openings {

	/**
	 * Method that identifies the line in the opening file(if there exists one) that matches the history
	 * of moves from the beginning to the current moment. 
	 * @param history
	 * @return	the next move of the engine read from the openings
	 */
	public String getOpenningMove(String history){
		BufferedReader br;
		try {
			String line;
			
			br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("openings.txt")));
			
			while ((line = br.readLine()) != null) {
				 
				if (line.startsWith(history)
						&& line.length() > history.length()) {
					return line.substring(history.length(),
							history.length() + 4);
				}
			}
			br.close();
		} catch (Exception e) {
			
		}
		
		return "";
	}
	
	/**
	 * 	Method that is used in the case that our engine is White and needs to generate a random
	 * move ( the opening lists start with moves of the white pieces).
	 * @return
	 */
	public String getRandomOpen(){
		
		// generate a random line from the opening file
		int rand=(int)(Math.random()*130)+1;
		BufferedReader br;
		try {
			String line = "";
			br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("openings.txt")));

			while (rand > 0 && (line = br.readLine()) != null) {
				rand--;
			}

			// tokenize the random line
			StringTokenizer st = new StringTokenizer(line);
			br.close();
			return st.nextToken();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}