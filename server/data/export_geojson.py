import requests
import json

# Correct API URL to fetch Maryland county names and boundaries with GeoJSON format
url = "https://tigerweb.geo.census.gov/arcgis/rest/services/TIGERweb/State_County/MapServer/1/query?where=STATE='45'&outFields=NAME,COUNTY&f=geojson"

# Send GET request to fetch the GeoJSON data
response = requests.get(url)

# Check if the response was successful
if response.status_code == 200:
    # Parse the GeoJSON response
    geojson_data = response.json()
    
    # Check if 'features' key is present in the response and has content
    if "features" in geojson_data and len(geojson_data["features"]) > 0:
        # Extract and print county names from the response
        county_names = [feature['properties']['NAME'] for feature in geojson_data['features']]
        print("SC County Names:")
        for name in county_names:
            print(f"- {name}")
        
        # Save the complete GeoJSON data with county boundaries to a new file
        output_file = "south_carolina_counties.geojson"
        with open(output_file, 'w') as file:
            json.dump(geojson_data, file, indent=4)
        
        print(f"GeoJSON data with county boundaries successfully saved to {output_file}")
    else:
        print("No county features found in the response. Please check the query parameters.")
else:
    print(f"Failed to retrieve data. HTTP Status Code: {response.status_code}")
