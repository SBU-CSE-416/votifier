const express = require('express')
const router = express.Router()

const { getState, getDistricts } = require('../controllers/mapController');

router.get('/:stateid/', getState)

router.get('/:stateid/districts', getDistricts)

module.exports = router