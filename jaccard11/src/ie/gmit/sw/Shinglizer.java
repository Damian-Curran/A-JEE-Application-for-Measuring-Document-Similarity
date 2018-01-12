package ie.gmit.sw;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Damian Curran
 *
 */
public class Shinglizer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/** 
	 * Creates Shingles of size 3 and returns the set of hashed shingles
	 * 
	 * @param document
	 * @return 
	 * @throws FileNotFoundException
	 */
	public Set<Integer> getShingle(String document) throws FileNotFoundException {
		// sets string variable line to act as holder for every 3rd word
		String line = "";
		// counts how many words are read in
		int count = 0;
		// instaniates a set of integers set
		Set<Integer> sets = new TreeSet<Integer>();

		// sets an array to split the document passed in
		String[] arr = document.split(" ");

		// for loop which handles the shinglizing
		for (String ss : arr) {
			// Increments counter
			count++;

			// adds word to line
			line += " " + ss;

			// if statement which checks if 3 words have been counted
			if (count % 3 == 0) {
				// adds the 3 worded lines hashcode to the set
				sets.add(line.hashCode());
				// sets line back to empty
				line = "";
			}
		}

		// returns the set of shingles
		return sets;
	}
}
