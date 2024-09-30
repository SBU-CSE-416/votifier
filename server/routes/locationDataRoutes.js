const express = require('express')
const router = express.router()

const { getLocation } = require('../controllers/locationDataController.js')

router.get('/get-location', getLocation);