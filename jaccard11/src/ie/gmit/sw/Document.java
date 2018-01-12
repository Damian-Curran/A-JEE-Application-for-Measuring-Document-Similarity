package ie.gmit.sw;

import java.util.Set;

/**
 * @author Damian Curran
 *
 */
public class Document {
	private String docs;
	private String titles;
	private Set<Integer> shingle;
	private String result;
	private Object jacResult;
	private String taskNum;

	// all getters and setters needed for project

	/**
	 * Gets task number
	 * 
	 * @return
	 */
	public String getTaskNum() {
		return taskNum;
	}

	
	/**
	 * Sets task number
	 * 
	 * @param taskNum
	 */
	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	/**
	 * Gets Jaccard Result
	 * 
	 * @return
	 */
	public Object getJacResult() {
		return jacResult;
	}

	/**
	 * Saves Jaccard Result
	 * 
	 * @param jacResult
	 */
	public void setJacResult(Object jacResult) {
		this.jacResult = jacResult;
	}

	/**
	 * Gets Result
	 * 
	 * @return
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Sets Result
	 * 
	 * @param result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * Constructor which takes parameters and sets class variables
	 * 
	 * @param title
	 * @param doc
	 */
	public Document(String title, String doc) {
		docs = doc;
		titles = title;
	}

	public Document() {

	}

	/**
	 * Constructor which takes parameters and sets class variables
	 * 
	 * @param title
	 * @param shingles
	 */
	public Document(String title, Set<Integer> shingles) {
		titles = title;
		shingle = shingles;
	}

	/**
	 * Gets Shingles
	 * 
	 * @return
	 */
	public Set<Integer> getShingle() {
		return shingle;
	}

	/**
	 * Sets Shingles
	 * 
	 * @param shingle
	 */
	public void setShingle(Set<Integer> shingle) {
		this.shingle = shingle;
	}

	/**
	 * Gets Docs which is the file contents
	 * 
	 * @return
	 */
	public String getDocs() {
		return docs;
	}

	/**
	 * Sets Docs which is the file contents
	 * 
	 * @param docs
	 */
	public void setDocs(String docs) {
		this.docs = docs;
	}

	/**
	 * Gets title
	 * 
	 * @return
	 */
	public String getTitles() {
		return titles;
	}

	/**
	 * Sets title
	 * 
	 * @param titles
	 */
	public void setTitles(String titles) {
		this.titles = titles;
	}

	/**
	 * Sets double
	 * 
	 * @param result
	 */
	public void setDouble(String result) {
		this.result = result;
	}

	/**
	 * Gets Double
	 * 
	 * @return
	 */
	public String getDouble() {
		return result;
	}

}
