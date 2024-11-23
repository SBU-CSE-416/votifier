const express = require("express");
const router = express.Router();

const { getState, getDistricts } = require("../controllers/mapController");

router.get("/:fips_code/", getState);

router.get("/:fips_code/districts", getDistricts);

module.exports = router;
