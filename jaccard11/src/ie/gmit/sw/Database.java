package ie.gmit.sw;

import java.util.List;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

/**
 * @author Damian Curran
 *
 */
public class Database {
	// sets class variable con
	private static ObjectContainer con;

	/**
	 * Stores the parameter d into database
	 * 
	 * @param d
	 */
	public void storer(Document d) {
		// checks database aconnection
		ObjectContainer db = connector();

		// stores object d which is a document object with fields name and
		// shingles
		db.store(d);
	}

	/**
	 * Queries database for all results
	 * 
	 * @return
	 */
	public List<Document> taker() {
		// checks database aconnection
		ObjectContainer db = connector();

		// retrieve set of Document
		List<Document> docs = db.query(Document.class);

		// return the set queried from database
		return docs;
	}

	/**
	 * Checks if connection to database exists
	 * 
	 * @return
	 */
	public ObjectContainer connector() {
		// checks if a document is already focused on
		// if it is, returns current focus
		if (con != null)
			return con;

		// sets con to new focus
		try {
			con = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "db4o");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// returns new focus
		return con;
	}
}
