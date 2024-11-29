from pymongo import MongoClient
import json

client = MongoClient("mongodb://localhost:27017/")
db = client["votifier"]
collection = db["state_boundaries"]

collection.delete_many({})

south_carolina_state_path = 'states/south_carolina/geodata/south_carolina_state.geojson'

with open(south_carolina_state_path, 'r') as file:
    geojson_data = json.load(file)


geojson_data["NAME"] = "South Carolina"
collection.insert_one(geojson_data)

collection.create_index([("geometry", "2dsphere")])

print("South Carolina GeoJSON data inserted index created")

maryland_state_path = 'states/maryland/geodata/maryland_state.geojson'

with open(maryland_state_path, 'r') as file:
    geojson_data = json.load(file)


geojson_data["NAME"] = "Maryland"
collection.insert_one(geojson_data)

collection.create_index([("geometry", "2dsphere")])

print("Maryland GeoJSON data inserted index created")

collection = db["cd_boundaries"]

collection.delete_many({})
south_carolina_cd_path = 'states/south_carolina/geodata/south_carolina_cd.geojson'

with open(south_carolina_cd_path, 'r') as file:
    geojson_data = json.load(file)

geojson_data["NAME"] = "South Carolina"

collection.insert_one(geojson_data)

collection.create_index([("geometry", "2dsphere")])

print("South Carolina Congressional District GeoJSON data inserted index created")

maryland_cd_path = 'states/maryland/geodata/maryland_cd.geojson'

with open(maryland_cd_path, 'r') as file:
    geojson_data = json.load(file)

geojson_data["NAME"] = "Maryland"

collection.insert_one(geojson_data)

collection.create_index([("geometry", "2dsphere")])

print("Maryland Congressional District GeoJSON data inserted index created")

collection = db["precinct_boundaries"]

collection.delete_many({})

south_carolina_precinct_path = 'states/south_carolina/geodata/south_carolina_precincts.geojson'

with open(south_carolina_precinct_path, 'r') as file:
    geojson_data = json.load(file)

# geojson_data["NAME"] = "South Carolina"

if geojson_data["type"] == "FeatureCollection":
    features = geojson_data.get("features", [])
    
    # Insert each feature (precinct) as a separate document
    for feature in features:
        feature["NAME"] = "South Carolina"  # Add state name to each feature
        collection.insert_one(feature)

    print(f"Inserted {len(features)} precincts into MongoDB.")
else:
    print("GeoJSON file is not a FeatureCollection.")

collection.create_index([("geometry", "2dsphere")])

print("South Carolina Precinct GeoJSON data inserted index created")

maryland_precinct_path = 'states/maryland/geodata/maryland_precincts.geojson'

with open(maryland_precinct_path, 'r') as file:

    geojson_data = json.load(file)

# geojson_data["NAME"] = "Maryland"

if geojson_data["type"] == "FeatureCollection":
    features = geojson_data.get("features", [])
    
    # Insert each feature (precinct) as a separate document
    for feature in features:
        feature["NAME"] = "Maryland"  # Add state name to each feature
        collection.insert_one(feature)

    print(f"Inserted {len(features)} precincts into MongoDB.")
else:
    print("GeoJSON file is not a FeatureCollection.")

collection.create_index([("geometry", "2dsphere")])

print("Maryland Precinct GeoJSON data inserted index created")

collection = db["state_summary"]

collection.delete_many({})


south_carolina_state_summary_path = "states/south_carolina/summary/south_carolina_summary.json"

with open(south_carolina_state_summary_path, 'r') as file:
    summary_data = json.load(file)

collection.insert_one(summary_data)

print("South Carolina Summary data inserted")




