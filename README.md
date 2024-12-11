Instructions on how to run the project locally.

Dependencies
- node.js
- npm
- pymongo
- apache maven
- java jdk 17 or higher
- python (pip)

Requirements
- cd into client directory, run `npm install`
- cd into server directory, run `npm install`
- cd into server dir, run `pip install pymongo`, `pip install tqdm`
- ensure JDK 17 or higher is installed
- ensure Maven is installed ( https://maven.apache.org/download.cgi ) and added to system variables in environment variable. To add Maven so that you can use mvn in gitbash in vsc, use cmd `export MAVEN_HOME=/c/Program\ Files/Maven/apache-maven-3.9.9` into git bash.

CLIENT SIDE
- to run the development view, cd into client and run $npm start

SERVER SIDE
- start mongodb.exe
- cd into server/data, run `populate_db.py` to fill out the db in mongodb
- cd into the server, run the cmd `./mvnw spring-boot:run`
- (note for Kaitlyn, the above cmd works in powershell, not git bash for laptop)

-----
### Languages, Librarys, & Frameworks used on the Project
Client: React, Leaflet, Axios, Recharts,
</br>Server: Java, MongoDB, 
</br>Proprocessing: Python (Pandas, GeoPandas
