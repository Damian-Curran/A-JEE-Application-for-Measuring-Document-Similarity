package ie.gmit.sw;

import java.util.List;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class Database {
	private static  ObjectContainer con;
	public void storer(Document d)
	{
		ObjectContainer db = connector();
		
		db.store(d);
		System.out.println("Stored " + d);
		System.out.println(d.getTitles());
		
	}
	
	public List<Document> taker()
	{
		ObjectContainer db = connector();
		
		//retrieve set of pilots
		List <Document> docs = db.query(Document.class);
		System.out.println("list of database reads " + docs);
		System.out.println("length of list " + docs.size());
		
		return docs;
	}
	
	public ObjectContainer connector()
	{
		if(con != null) return con;
		
		try{
			con = Db4oEmbedded.openFile(Db4oEmbedded .newConfiguration(), "db4o");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return con;
	}
}
