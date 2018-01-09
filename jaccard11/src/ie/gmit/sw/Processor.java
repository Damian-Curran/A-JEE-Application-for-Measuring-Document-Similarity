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
	
	//sets class variables of queue names
	private final static String INPUT_NAME = "docToCompare";
	private final static String RESULT_QUEUE = "results";
	//creates thread variable t
	Thread t;
	
	//method which checks a queue
	//starts thread for each item pulled from queue
	//uses shingle class to get shingles of document uploaded
	public void checkQueue() throws IOException, TimeoutException
	{
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
	    	  
	    	  //gets message sent in queue from body handleDelivery param
	    	  String message = new String(body, "UTF-8");
	    	  
	    	  //creates new thread for each item pulled from queue
	    	  t = new Thread(replyProps.getCorrelationId()){
	    		  public void run(){
	    			  //creates new shingilizer
	    			  Shinglizer s = new Shinglizer();
	    			  //instantiates a set of integer for storing shingles returned
	    		  	    Set<Integer> shingles = null;
						try {
							//pass message into shingles and returns sets of shingles size 3
							shingles = s.getShingle(message);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	    		  	    
						//prints jaccardIndex of same document
	    		  	    System.out.println("Real similarity (Jaccard index)" +
	    	            Minhasher.jaccardIndex(shingles, shingles));
	    		  	    
	    		  	    //creates new document
	    		  	    Document d = new Document();
	    		  	    //sets document d shingle and title
	    		  	    d.setShingle(shingles);
	    		  	    d.setTitles(replyProps.getMessageId());
	    		  	    
	    		  	    //creates database handler
	    		  	    Database data = new Database();
	    		  	    //sets boolean to use later to check if map contains needed task
	    		  	    boolean error = true;
	    		  	    
	    		  	    //while error, retry connection
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
	    		  	    
	    		  	    //creates map of String, object
	    		  	    Map<String, Object> results = new HashMap<String, Object>();
	    		  	    
	    		  	    //creates list docs to store db variables
	    		  	    List<Document> docs = data.taker();
	    		  	    //for each des in docs list
	    		  	    for(Document des : docs)
	    		  	    {
	    		  	    	//puts each title and jaccardIndex(once computed) into the map 
	    		  	    	results.put(des.getTitles(), Minhasher.jaccardIndex(shingles, des.getShingle())*100);
	    		  	    }
	    			  	
	    		  	    //try/catch on calling result queue
	    		  	    //which puts the finished task to an outward queue to show user
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
	    	  //starts thread
	    	  t.start();
	  	    
	    	  //system out to show user message received
	        System.out.println(" [x] Received '" + message + "'" + replyProps.getMessageId());
	        
	      }
	    };
	    channel.basicConsume(INPUT_NAME, true, consumer);
	    
	    
	}
	
	//method to add finished products to an outgoing queue
	public void resultQueue(Map<String, Object> results, String taskId) throws IOException, TimeoutException
	{
		//starts new factory
		ConnectionFactory factory1 = new ConnectionFactory();
		//sets host of factory
		factory1.setHost("localhost");
		//creates connection from factory
		Connection connection1 = factory1.newConnection();
		//creates channel from connection
		Channel channel1 = connection1.createChannel();
		
		//sets taskNumber by using param passed in
		String taskNumber = taskId;
		//sets doc placeholder in queue
		String doc = "Results";
		
		//sets props correlationId to be that of the ID received from the publisher(clientServlet)
		AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(taskNumber).headers(results).build();
		
		//declare what queue you are outputting to
		channel1.queueDeclare(RESULT_QUEUE, false, false, false, null);
		//publish channel
		channel1.basicPublish("", RESULT_QUEUE, props, doc.getBytes("UTF-8"));
		//shows output sent in console
		System.out.println(" [x] Sent output'" + taskNumber + "'");

		channel1.close();
		connection1.close();
	}

}
