package ie.gmit.sw;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class Shinglizer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Set<Integer> getShingle(String document) throws FileNotFoundException
	{
        
        String line = "";
        int count = 0;
        Set<Integer> sets = new TreeSet<Integer>();
        
        String[] arr = document.split(" ");
        
        for(String ss : arr)
        {
        	count++;
        	line += " " + ss;
        	if(count%3 == 0)
        	{
        		sets.add(line.hashCode());
        		line = "";
        	}
        }
        
        return sets;
	}
}
