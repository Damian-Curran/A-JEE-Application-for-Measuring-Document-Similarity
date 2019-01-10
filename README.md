# A-JEE-Application-for-Measuring-Document-Similarity

For this project we were instructed to create a JEE web application which compared 1 or more documents against eachother using the Jaccard Index comparison method.

The main technologies used to achieve our wanted outcome are: Eclipse, Apache Tomcat and lastly RabbitMQ.

We will be focusing on using Java RMI(Remote Method Invocation), a Java API that lets us perform remote method invocation.

RabbitMQ is an open source message broker. We use it for adding tasks to a queue.
Hopefully you will have a better understanding of RabbitMQ with the use of the image below.

![](Images/rabbitmq.png)

## Installing needed technologies
Firstly we will need Eclipse: https://www.ntu.edu.sg/home/ehchua/programming/howto/EclipseJava_HowTo.html

Apache Tomcat: http://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/

Then you will need to install Erlang and RabbitMQ: https://www.rabbitmq.com/install-windows.html

## Using this repository
git clone https://github.com/Damian404/A-JEE-Application-for-Measuring-Document-Similarity.git

Import clone into eclipse

Run rabbitmq service

Run client, serverSetup as java applications

Run clientServlet on Apache Tomcat server 
