# A-JEE-Application-for-Measuring-Document-Similarity

For this project we were instructed to create a JEE web application which compared 1 or more documents against eachother using the Jaccard Index comparison method.

The main technologies used to achieve our wanted outcome are: Eclipse, Apache Tomcat and lastly RabbitMQ.

There is threading added to help with the possible server load.

RabbitMQ is an open source message broker. We use it for adding tasks to a queue.
Hopefully you will have a better understanding of RabbitMQ with the use of the image below.

![](Images/rabbitmq.png)

## Installing needed technologies
Firstly we will need Eclipse: https://www.ntu.edu.sg/home/ehchua/programming/howto/EclipseJava_HowTo.html

Apache Tomcat: http://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/

Then you will need to install Erlang and RabbitMQ: https://www.rabbitmq.com/install-windows.html

## Using this repository
git clone https://github.com/Damian404/A-JEE-Application-for-Measuring-Document-Similarity.git

To use this project, copy the war file into webapps inside your tomcat folder, the directory should look like this:

C:\apache-tomcat-8.5.24\webapps

In your cmd navigate to you tomcats bin folder, an example directory to bin is shown below:

C:\apache-tomcat-8.5.24\bin

Now use the command: startup

This will start the tomcat server.

Open your browser and type:
http://localhost:8080/Jaccard/

Before the service runs correctly, you will need to start the rabbitmq service.

If rabbitmq was installed correctly this can be done by tapping the windows key and typing rabbitmq where you will see: RabbitMQ Service - start

Click said RabbitMQ Service - start result.
