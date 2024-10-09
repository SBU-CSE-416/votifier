const path = require('path');

const stateIdToName = {
    24: "maryland",
    45: "south_carolina",
};

function getState(req, res) {    
    const stateId = req.params.stateid; 
    try {
        const stateName = stateIdToName[stateId];
        const filePath = `../data/states/${stateName}/geodata/${stateName}_state.geojson`;
        newPath = path.join(__dirname, filePath);

        res.sendFile(newPath, (err) => {
            if (err) {
              res.status(500).send("File not found");
            }
        });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
}

function getDistricts(req, res){
    const stateId = req.params.stateid; 
    try {
        const stateName = stateIdToName[stateId]

        const filePath = `../data/states/${stateName}/geodata/${stateName}_cds.geojson`;
        newPath = path.join(__dirname, filePath);

        res.sendFile(newPath, (err) => {
            if (err) {
              res.status(500).send("File not found");
            }
        });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
}

module.exports = { getState, getDistricts }