from pymongo import MongoClient
import json
import pymongo
from tqdm import tqdm

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

collection = db["precincts_boundaries"]

collection.delete_many({})

south_carolina_precincts_path = 'states/south_carolina/geodata/south_carolina_precincts.geojson'

with open(south_carolina_precincts_path, 'r') as file:
    geojson_data = json.load(file)

# geojson_data["NAME"] = "South Carolina"

if geojson_data["type"] == "FeatureCollection":
    features = geojson_data.get("features", [])
    
    # Use tqdm to iterate with a progress bar
    for feature in tqdm(features, desc="Inserting South Carolina precincts"):
        feature["NAME"] = "South Carolina"  # Add state name to each feature
        try:
            collection.insert_one(feature)
        except pymongo.errors.WriteError as e:
            # Log or ignore the error based on your requirements
            pass

    print(f"Inserted {len(features)} precincts into MongoDB.")
else:
    print("GeoJSON file is not a FeatureCollection.")

# After inserting, create the index
collection.create_index([("geometry", "2dsphere")])
print("South Carolina Precinct GeoJSON data inserted index created")

maryland_precincts_path = 'states/maryland/geodata/maryland_precincts.geojson'

with open(maryland_precincts_path, 'r') as file:

    geojson_data = json.load(file)

# geojson_data["NAME"] = "Maryland"

if geojson_data["type"] == "FeatureCollection":
    features = geojson_data.get("features", [])
    
    
    for feature in tqdm(features, desc="Inserting Maryland precincts"):
        feature["NAME"] = "Maryland"
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


maryland_state_summary_path = "states/maryland/summary/maryland_summary.json"

with open(maryland_state_summary_path, 'r') as file:
    summary_data = json.load(file)

collection.insert_one(summary_data)

print("Maryland Summary data inserted")


collection = db["districts_summary"]

collection.delete_many({})

sc_election_cong_path = "states/south_carolina/congressional_districts/summary/sc_congressional_districts_summary.json"

with open(sc_election_cong_path, 'r') as file:
    sc_election_cong_data = json.load(file)

collection.insert_one(sc_election_cong_data)

print("South Carolina Congressional Districts Summary data inserted")


md_election_cong_path = "states/maryland/congressional_districts/summary/md_congressional_districts_summary.json"

with open(md_election_cong_path, 'r') as file:
    md_election_cong_data = json.load(file)

collection.insert_one(md_election_cong_data)

print("Maryland Congressional Districts Summary data inserted")


collection = db["gingles"]

collection.delete_many({})

sc_gingles_path = "states/south_carolina/gingles/sc_gingles_precinct_analysis.json"

with open(sc_gingles_path, 'r') as file:
    sc_gingles_data = json.load(file)

collection.insert_one(sc_gingles_data)

print("South Carolina Gingles data inserted")

md_gingles_path = "states/maryland/gingles/md_gingles_precinct_analysis.json"

with open(md_gingles_path, 'r') as file:
    md_gingles_data = json.load(file)

collection.insert_one(md_gingles_data)

print("Maryland Gingles data inserted")
