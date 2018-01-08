package ie.gmit.sw;

import java.util.Set;

public class Document {
	private String docs;
	private String titles;
	private Set<Integer> shingle;
	private String result;
	private Object jacResult;
	private String taskNum;
	 
	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	public Object getJacResult() {
		return jacResult;
	}

	public void setJacResult(Object jacResult) {
		this.jacResult = jacResult;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Document(String title, String doc)
	{
		docs = doc;
		titles = title;
	}

	public Document() {
		// TODO Auto-generated constructor stub
	}

	public Document(String title, Set<Integer> shingles) {
		titles = title;
		shingle = shingles;
	}

	public Set<Integer> getShingle() {
		return shingle;
	}

	public void setShingle(Set<Integer> shingle) {
		this.shingle = shingle;
	}

	public String getDocs() {
		return docs;
	}

	public void setDocs(String docs) {
		this.docs = docs;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public void setDouble(String result) {
		this.result = result;
	}	
	
	public String getDouble() {
		return result;
	}	
	
}
