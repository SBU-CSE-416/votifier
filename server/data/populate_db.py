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


collection = db["gingles_racial_data"]

collection.delete_many({})

sc_gingles_path = "states/south_carolina/gingles/sc_gingles_precinct_analysis.json"

with open(sc_gingles_path, 'r') as file:
    sc_gingles_data = json.load(file)

collection.insert_one(sc_gingles_data)

print("South Carolina Gingles Racial data inserted")

md_gingles_path = "states/maryland/gingles/md_gingles_precinct_analysis.json"

with open(md_gingles_path, 'r') as file:
    md_gingles_data = json.load(file)

collection.insert_one(md_gingles_data)

print("Maryland Gingles Racial data inserted")

collection = db["gingles_economic_data"]

collection.delete_many({})

sc_gingles_economic_path = "states/south_carolina/gingles/sc_gingles_precinct_income_analysis.json"

with open(sc_gingles_economic_path, 'r') as file:
    sc_gingles_economic_data = json.load(file)

collection.insert_one(sc_gingles_economic_data)

print("South Carolina Gingles Economic data inserted")

md_gingles_economic_path = "states/maryland/gingles/md_gingles_precinct_income_analysis.json"

with open(md_gingles_economic_path, 'r') as file:
    md_gingles_economic_data = json.load(file)

collection.insert_one(md_gingles_economic_data)

print("Maryland Gingles Economic data inserted")

collection = db["economic_data"]

collection.delete_many({})

sc_economic_data_path = "states/south_carolina/economic/south_carolina_precincts_household_income.json"

with open(sc_economic_data_path, 'r') as file:
    sc_economic_data_list = json.load(file)

sc_economic_data_doc = {
    "NAME": "South Carolina",
    "data": sc_economic_data_list
}

collection.insert_one(sc_economic_data_doc)

print("South Carolina Economic data inserted")

md_economic_data_path = "states/maryland/economic/maryland_precincts_household_income.json"

with open(md_economic_data_path, 'r') as file:
    md_economic_data_list = json.load(file)

md_economic_data_doc = {
    "NAME": "Maryland",
    "data": md_economic_data_list
}

collection.insert_one(md_economic_data_doc)

print("Maryland Economic data inserted")

collection = db["election_data"]

collection.delete_many({})

sc_election_data_path = "states/south_carolina/election/sc_election_gov_22.json"

with open(sc_election_data_path, 'r') as file:
    sc_election_data = json.load(file)

sc_election_data_doc = {
    "NAME": "South Carolina",
    "election": "2022 Gubernatorial Elections",
    "data": sc_election_data
}

collection.insert_one(sc_election_data_doc)

print("South Carolina Election data inserted")

md_election_data_path = "states/maryland/election/md_election_gov_22.json"

with open(md_election_data_path, 'r') as file:
    md_election_data = json.load(file)

md_election_data_doc = {
    "NAME": "Maryland",
    "election": "2022 Gubernatorial Elections",
    "data": md_election_data
}

collection.insert_one(md_election_data_doc)

print("Maryland Election data inserted")

collection = db["demographic_data"]

collection.delete_many({})

sc_demographic_data_path = "states/south_carolina/demographics/south_carolina_precincts_racial_population.json"

with open(sc_demographic_data_path, 'r') as file:
    sc_demographic_data = json.load(file)

sc_demographic_data_doc = {
    "NAME": "South Carolina",
    "data": sc_demographic_data
}

collection.insert_one(sc_demographic_data_doc)

print("South Carolina Demographic data inserted")

md_demographic_data_path = "states/maryland/demographics/maryland_precincts_racial_population.json"

with open(md_demographic_data_path, 'r') as file:
    md_demographic_data = json.load(file)

md_demographic_data_doc = {
    "NAME": "Maryland",
    "data": md_demographic_data
}

collection.insert_one(md_demographic_data_doc)

print("Maryland Demographic data inserted")

collection = db["region_type_data"]

collection.delete_many({})

sc_region_type_data_path = "states/south_carolina/geodata/south_carolina_precincts_region_type.json"

with open(sc_region_type_data_path, 'r') as file:
    sc_region_type_data = json.load(file)

sc_region_type_data_doc = {
    "NAME": "South Carolina",
    "data": sc_region_type_data
}

collection.insert_one(sc_region_type_data_doc)

print("South Carolina Region Type data inserted")

md_region_type_data_path = "states/maryland/geodata/maryland_precincts_region_type.json"

with open(md_region_type_data_path, 'r') as file:
    md_region_type_data = json.load(file)

md_region_type_data_doc = {
    "NAME": "Maryland",
    "data": md_region_type_data
}

collection.insert_one(md_region_type_data_doc)

print("Maryland Region Type data inserted")