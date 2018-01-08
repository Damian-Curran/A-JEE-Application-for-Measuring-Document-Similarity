package ie.gmit.sw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.Db4oIOException;
import com.rabbitmq.client.*;

public class Processor{
	
	private final static String INPUT_NAME = "docToCompare";
	private final static String RESULT_QUEUE = "results";
	Thread t;
	
	public void checkQueue() throws IOException, TimeoutException
	{
		Map<String, Document> hm = new HashMap<String, Document>();
		//creates connection factory
		ConnectionFactory factory = new ConnectionFactory();
		//sets the host to be used
	    factory.setHost("localhost");
	    //creates connection to the factory
	    Connection connection = factory.newConnection();
	    //creates channel from connection to be used
	    Channel channel = connection.createChannel();
	    
	    //uses channel to declare a new queue
	    channel.queueDeclare(INPUT_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    //creates a defaultconsumer
	    Consumer consumer = new DefaultConsumer(channel) {
	      //we override the method to manipulate variables
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	    	  //manipulate AMQP basic properties to get message id stored and correlation id
	    	  AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
						.correlationId(properties.getCorrelationId()).messageId(properties.getMessageId()).build(); 	  
	    	  
	    	    
	    	  String message = new String(body, "UTF-8");
	    	  
	    	  System.out.println("before thread");
	    	  
	    	  t = new Thread(replyProps.getCorrelationId()){
	    		  public void run(){
	    			  System.out.println("in thread " + t);
	    			  Shinglizer s = new Shinglizer();
	    		  	    Set<Integer> shingles = null;
						try {
							shingles = s.getShingle(message);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    		  	    
	    		  	    Minhasher minhash = new Minhasher(0.1, 5);
	    		  	    
	    		  	    System.out.println("Real similarity (Jaccard index)" +
	    	            Minhasher.jaccardIndex(shingles, shingles));
	    		  	    
	    		  	    Document d = new Document();
	    		  	    d.setShingle(shingles);
	    		  	    d.setTitles(replyProps.getMessageId());
	    		  	    
	    		  	    Database data = new Database();
	    		  	    boolean error = true;
	    		  	    
	    		  	    do{
	    		  	    	try{
	    			  	    	data.storer(d);
	    			  	    	error=false;
	    			  	    }catch(DatabaseFileLockedException e){
	    			  	    	data.storer(d);
	    			  	    }catch(Db4oIOException e){
	    			  	    	data.storer(d);
	    			  	    }catch(Exception e){
	    			  	    	data.storer(d);
	    			  	    }
	    		  	    }while(error);
	    		  	    
	    		  	    Map<String, Object> results = new HashMap<String, Object>();
	    		  	    
	    		  	    //Document ds = new Document();
	    		  	    
	    		  	    List<Document> docs = data.taker();
	    		  	    for(Document des : docs)
	    		  	    {
	    		  	    	//ds.setDouble(Double.toString(Minhasher.jaccardIndex(shingles, des.getShingle())));
	    		  	    	//ds.setTitles(des.getTitles());
	    		  	    	System.out.println("minhasher jaccard index return " + Minhasher.jaccardIndex(shingles, des.getShingle()));
	    		  	    	results.put(des.getTitles(), Minhasher.jaccardIndex(shingles, des.getShingle())*100);
	    		  	    	
	    		  	    	//System.out.println(des.getTitles());
	    		  	    	//System.out.println(des.getShingle());
	    		  	    }
	    		  	    
	    		  	    System.out.println("the results map returns " + results);
	    			  	
	    		  	    try {
	    					resultQueue(results, replyProps.getCorrelationId());
	    				} catch (TimeoutException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
	    		  }
	    	  };
	    	  
	    	  t.start();
	    	  
	    	
	  	    //hm.put(replyProps.getCorrelationId(), d);
	  	    
	  	    //System.out.println(hm);
	  	    
	        System.out.println(" [x] Received '" + message + "'" + replyProps.getMessageId());
	        
	      }
	    };
	    channel.basicConsume(INPUT_NAME, true, consumer);
	    
	    
	}
	
	public void resultQueue(Map<String, Object> results, String taskId) throws IOException, TimeoutException
	{
		ConnectionFactory factory1 = new ConnectionFactory();
		factory1.setHost("localhost");
		Connection connection1 = factory1.newConnection();
		Channel channel1 = connection1.createChannel();
		
		String taskNumber = taskId;
		String doc = "Results";
		
		//sets props correlationId to be that of the ID received from the publisher(clientServlet)
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(taskNumber).headers(results).build();

		//System.out.println("headers " + " " + results);
		
		channel1.queueDeclare(RESULT_QUEUE, false, false, false, null);
		channel1.basicPublish("", RESULT_QUEUE, props, doc.getBytes("UTF-8"));
		System.out.println(" [x] Sent output'" + taskNumber + "'");

		channel1.close();
		connection1.close();
	}

}
