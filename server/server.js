const express = require('express');
const cors = require("cors");
const connectDB = require("./config/db");
const mongoose = require("mongoose");
const port = 8000;
const corsOptions = {
    origin: 'http://localhost:3000', 
    credentials: true, 
  };
  
connectDB();

const app = express();
app.use(cors(corsOptions));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use((req, res, next) => {
    console.log('Request Body:', req.body);
    next();
});
const server = app.listen(port, () => console.log(`Server listening on port ${port}`));

// Start the server
/*
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});*/


const shutdown = async () => {
    console.log('\nServer is shutting down...');

    
    server.close(async () => {
        try {
            await mongoose.connection.close();
            console.log('Server closed. Database instance disconnected');
        } catch (error) {
            console.error('Error during database disconnection:', error);
        }
        process.exit(0);
    });
};

process.on('SIGINT', shutdown);  
process.on('SIGTERM', shutdown); 