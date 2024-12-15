# Votifier

## Installation

### Prerequisites
- Ensure JDK 17 or higher is installed.
- Ensure Maven is installed (https://maven.apache.org/download.cgi) and added to system variables in environment variables. To add Maven so that you can use `mvn` in Git Bash in VS Code, use the command in git bash:
```sh 
export MAVEN_HOME=/c/Program\ Files/Maven/apache-maven-3.9.9
```

### Client Side
- cd into client directory, install dependencies: `npm install`, `npm install react-chartjs-2 chart.js`, `npm install d3`

### Server Side
- cd into server directory, install dependencies:
`npm install`, `pip install pymongo`, `pip install tqdm`

### Database Setup
- Start MongoDB, `mongod`
- Populate the database (can be done once, or on update of database contents) by navigating to votifier/server/data directory, run the `populate_db.py` script

## Running the Development Site Locally

### Client Side
- cd into client directory, run `npm start`

### Server Side
- Start MongoDB, `mongod`
- cd into server directory, run the Spring Boot application `./mvnw spring-boot:run`

-----
### Languages, Librarys, & Frameworks used on the Project
Client: React, Leaflet, Axios, Recharts, Chart.js, D3.js, Victory
</br>Server: Java, MongoDB, Maven, Spring Boot
</br>Proprocessing: Python (Pandas, GeoPandas, PyMongo, TQDM, NumPy, Matplotlib, Seaborn, Scikit-learn, PyMC, JAX, NumPyro, Numba, NetCDF4, Graphviz)
