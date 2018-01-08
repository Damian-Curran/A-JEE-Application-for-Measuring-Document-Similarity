package ie.gmit.sw;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import javax.servlet.*;
import javax.servlet.http.*;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ServicePollHandler extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String RESULT_QUEUE = "results";
	Map<String, Document> jaccardResults = new HashMap<String, Document>();
	
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html"); 
		PrintWriter out = resp.getWriter(); 
		
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		
		try {
			checkQueue();
			System.out.println("in try jacard" + jaccardResults.size());
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean showResults = false;
		
		for (Map.Entry<String, Document> entry : jaccardResults.entrySet())
		{
		    System.out.println(entry.getKey() + "/asdsadsadadasd" + entry.getValue().getTaskNum());
		    if(taskNumber.equals(entry.getValue().getTaskNum()))
		    {
		    	showResults = true;
		    }
		}
		
		System.out.println("what is size? " + jaccardResults.size());
		System.out.println("what is boolean state? " + showResults);
		System.out.println("what is taskNumber? " + taskNumber);
		
		if(jaccardResults.size() > 0 && showResults == true)
		{
			//PrintWriter out1 = resp.getWriter(); 
			System.out.println("we have a winner");
			
			out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
			out.print("</head>");		
			out.print("<body>");
			out.print("<H1>Job#: " + taskNumber + " Completed" + "</H1>");
			out.print("<H3>Document Title: " + title + "</H3>");
			out.print("<ol>");

			System.out.println("the isze is " + jaccardResults.size());
			
			for(Entry<String, Document> entry : jaccardResults.entrySet())
			{
				out.print("<li><b>Similarity between " + entry.getKey() + " is " + entry.getValue().getJacResult() + "%");
			}
			
			jaccardResults.remove(taskNumber);

			out.print("</ol>");
		}
		else
		{
			int counter = 1;
			if (req.getParameter("counter") != null){
				counter = Integer.parseInt(req.getParameter("counter"));
				counter++;
				//System.out.println("in if counting counter");
			}

			
			out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
			out.print("</head>");		
			out.print("<body>");
			out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
			out.print("<H3>Document Title: " + title + "</H3>");
			out.print("<b><font color=\"ff0000\">A total of " + counter + " polls have been made for this request.</font></b> ");
			out.print("Place the final response here... a nice table (or graphic!) of the document similarity...");
			
			out.print("<form name=\"frmRequestDetails\">");
			out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
			out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
			out.print("<input name=\"counter\" type=\"hidden\" value=\"" + counter + "\">");
			out.print("</form>");								
			out.print("</body>");	
			out.print("</html>");	
			
			out.print("<script>");
			out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 5000);"); //Refresh every 5 seconds
			out.print("</script>");
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//doGet(req, resp);
 	}
	
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
	    channel.queueDeclare(RESULT_QUEUE, false, false, false, null);
	    //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    //creates a defaultconsumer
	    Consumer consumer = new DefaultConsumer(channel) {
	      //we override the method to manipulate variables
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	    	  //manipulate AMQP basic properties to get message id stored and correlation id
	    	  AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
						.correlationId(properties.getCorrelationId()).messageId(properties.getMessageId()).headers(properties.getHeaders()).build(); 	  
	    	  
	    	  String message = new String(body, "UTF-8");
	    	  Document instance = new Document();
	    	  
	    	  //jaccardResults = replyProps.getHeaders();
	    	  System.out.println("headers info " + jaccardResults);
	    	  
	    	  for (Map.Entry<String, Object> entry : replyProps.getHeaders().entrySet()) {
	    	      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	    	      instance.setJacResult(entry.getValue());
	    	      instance.setTaskNum(replyProps.getCorrelationId());
	    	      
	    	      jaccardResults.put(entry.getKey(), instance);
	    	      System.out.println("inside for loop jaccardresult " + jaccardResults.size());
	    	  }
	    	  
	    	  
	        System.out.println(" [x] Received '" + message + "'" + replyProps.getMessageId());
	        System.out.println("the headers for the results are:" + " " + replyProps.getHeaders());
	      }
	    };
	    channel.basicConsume(RESULT_QUEUE, true, consumer);
	}
}