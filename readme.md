Scalable File Download App

Set up :
--------
1. Fork the repo.
2. Clone the repo to your local computer

Once downloaded. You may import it in eclipse. 

For simplicity I have not included instructions for creating a fat jar. 


For demo purpose you may run this app from within the eclipse. Thats the best to see the demo.

Assumption: This is not a full fledged app with validations,security etc. its a demo to show how to setup such a project and how to give an understanding of turning this into a scalable solution.

How to run the App.
Prerequisites
1. Set up a My SQL DB in your local or a reachable server.
2. Change the following in application.properties according to your setup:-

spring.datasource.url=jdbc:mysql://localhost:3306/<<your schema name>>
spring.datasource.username=<<username>>
spring.datasource.password=<<password>>


Run the App

1. Run from within eclipse com.sap.resource.Application.java
2. Once server has started successfully. Open a Firefox Browser . and type
http://localhost:8080/swagger-ui.html .This will open up a swagger documentation of the APIs.

I ... Do a dummy Upload to break a file into parts. Will send a figure to show the Architecture in mind
II ... Download using the Swagger interface
Check the video:-
