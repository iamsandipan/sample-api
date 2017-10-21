Scalable File Download App

Set up :
--------
1. Fork the repo.
2. Clone the repo to your local computer

Once downloaded. You may import it in eclipse. 

For simplicity I have not included instructions for creating a fat jar. 

For demo purpose you may run this app from within the eclipse. Thats the best to see the demo.


Assumptions and Thinking:
------------------------

This is not a full fledged app with validations,security etc. its a demo to show how to setup such a project and how to give an understanding of turning this into a scalable solution

Scalability And Durability:
---------------------------
The assumption behind this is: we will be able to download parts of a file from distributed 
locations, aggregate them and recreate the original file.

During upload multipart request, each part will be stored in physically separated locations,
with a or more table to track the location of the parts 

In this example the upload api is just a dummy and it creates two parts of a file imitating the
upload. for the sake of simplicity i have copied them in the same folder, and also inserted the 
trackable information in the DB.

Once download request is received, the DB is queries and all the parts are fetched and 
aggregated to get the whole file.Ideally the fetching will be from geographically separated
servers to fulfill the scalability and fault tolerance and durability features

The durability may be achieved by setting up systems to copy the same file part in different 
geographical locations.

High Availability:
------------------
High Availability can be achieved by multiple api instance under a load balancer configuration.


How to run the App.
------------------
Prerequisites:
--------------
1. Set up a My SQL DB in your local or a reachable server.Create a schema

CREATE SCHEMA shhemaname DEFAULT CHARACTER SET utf8 ;

Add appropriate privilege for the user to run DDLs.For this demo purpose
you may give the user all priviledges. 

2. Change the following in src/main/resources/application.properties according to your setup:-

spring.datasource.url=jdbc:mysql://localhost:3306/schemaname

spring.datasource.username=your username

spring.datasource.password=your password


Run the App:
------------

1. Run from within eclipse com.sap.resource.Application.java
2. Once server has started successfully. Open a Firefox Browser . and type
http://localhost:8080/swagger-ui.html .This will open up a swagger documentation of the APIs.

I ... Do a dummy Upload to break a file into parts. Will send a figure to show the Architecture in mind
II ... Download using the Swagger interface
Check the video:-
