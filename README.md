Instructions on how to run the project locally.

Requirements
- cd into client directory, run `npm install`
- cd into server directory, run `npm install`
- ensure JDK 17 or higher is installed
- ensure Maven is installed ( https://maven.apache.org/download.cgi ) and added to system variables in environment variable. To add Maven so that you can use mvn in gitbash in vsc, use cmd `export MAVEN_HOME=/c/Program\ Files/Maven/apache-maven-3.9.9` into git bash.

CLIENT SIDE
- to run the development view, cd into client and run $npm start

SERVER SIDE
- cd into the server, run the cmd `./mvnw spring-boot:run`
- (note for Kaitlyn, the above cmd works in powershell, not git bash for laptop)

-----
### Languages & Tools used on the Project
Client: React, Leaflet, Axios, Recharts,
Server: Java, MongoDB, 
Proprocessing: Python (Pandas, GeoPandas
