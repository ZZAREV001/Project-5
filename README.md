SAFETY NET ALERT PROGRAM

A browser web application to find and modify data concerning a small city
An application like Postman can be used to visualize the result. Otherwise, a web browser will show the results.

**1) Get information from the web service.**
The application sends the result in JSON form when the user writes the following URLs (GET requests):

http://localhost:8080/firestation?stationNumber=<station_number>
Write the number of a fire station inside the URL. Get a list of people managed by this fire station.

http://localhost:8080/childAlert?address=<address>
Write the address of a person who lives in the town. Get a list of children and their coordinates.

http://localhost:8080/phoneAlert?firestation=<firestation_number>
Return a list of phone numbers managed by the given fire station.

http://localhost:8080/fire?address=<address>
Return a list of the residents and their associated fire station for the provided address.

http://localhost:8080/flood/stations?stations=<a_list_of_station_numbers>
Regroup a list of residents with their coordinates and medical records for a given station number.

http://localhost:8080/personInfo?firstName=<firstName>&lastName=<lastName>
For each firstname and lastname written in the URL, the web server will provide coordinates and medical records.

http://localhost:8080/communityEmail?city=<city>
Return all residents' emails.

**2) Read, update, and delete data in the web service.**

These endpoints allow the user to change the current data in memory (creating, updating, and deleting):

http://localhost:8080/person
This endpoint creates, updates, or deletes a person's memory.

http://localhost:8080/firestation
This endpoint creates, updates, or deletes a fire station in memory.

http://localhost:8080/medicalRecord
This endpoint creates, updates, or deletes a medical record in memory.

**3) Architecture of the Safety Net Alert**

![Architecture of the Safety Net Alert](/Users/GoldenEagle/IdeaProjects/projet-5-bis/Architecture-project-5.png "Architecture Diagram")

