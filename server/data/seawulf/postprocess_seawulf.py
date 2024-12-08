import geopandas as gpd
import pandas as pd
import argparse
from geopandas import GeoDataFrame as gdf
import alphashape
from shapely.geometry import MultiPoint
import matplotlib.pyplot as plt
import json

# geojson_file_path = 'path/to/your/geojson/file.geojson'



def postprocessing_process(precinct_boundaries_filepath, ensemble_id, plan_id):
    precinct_boundary_gdf = gpd.read_file(precinct_boundaries_filepath)
    buffer_distance = 0.0003
    
    # district_precincts_filepath = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_precincts_results.json"
    district_precincts_filepath = "district_precincts_results_md.json"
    # output_filepath = f"results/ensemble_{ensemble_id}/plan_{plan_id}/post/proposed_districts.geojson"
    output_filepath = "proposed_districts_md_full.geojson"

    with open(district_precincts_filepath, 'r') as district_precincts_file:
      district_precincts = json.load(district_precincts_file)

    districts_gdfs = []
    for district_id, precincts in district_precincts.items():

      district_precincts_gdf = precinct_boundary_gdf[precinct_boundary_gdf['UNIQUE_ID'].isin(precincts)]
      dissolved_geometry = district_precincts_gdf.dissolve(by='UNIQUE_ID').buffer(buffer_distance).simplify(tolerance=buffer_distance)
      union_geometry = dissolved_geometry.geometry.unary_union

      if union_geometry.geom_type == 'MultiPolygon':
        alpha = 12  # Lower values create tighter boundaries
        points = [point for polygon in union_geometry.geoms for point in polygon.exterior.coords]
        
        hull = alphashape.alphashape(points, alpha)
        print(f"Multi polygon for district {district_id} :(")
        # Extract the exterior of the largest polygon
        boundary_geometry = hull.exterior
        # boundary_geometry = max(union_geometry.geoms, key=lambda x: x.area).exterior
      else:
        # Use the exterior boundary directly for a single polygon
        boundary_geometry = union_geometry.exterior
      district_boundary_gdf = gpd.GeoDataFrame({
        'NAME': [f"Congressional District {district_id}"],  # Replace with desired name
        'geometry': [boundary_geometry]
      }, crs=district_precincts_gdf.crs)  # Keep the same CRS
      district_boundary_gdf.to_file(f"proposed_districts_md_{district_id}.geojson", driver='GeoJSON')
      district_precincts_gdf.to_file(f"proposed_districts_md_{district_id}_precincts.geojson", driver='GeoJSON')
      districts_gdfs.append(district_boundary_gdf)

    # district_colors = ['#FF5733', '#33FF57', '#3357FF', '#F0FF33', '#FF33F0', '#33FFF0', '#F033FF']
    all_districts_gdf = gpd.GeoDataFrame(pd.concat(districts_gdfs, ignore_index=True))
    all_districts_gdf.to_file(output_filepath, driver='GeoJSON')
    # print(all_districts_gdf)
   
    return

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--state_precincts_geojson_file', type=str, required=True)
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--plan_id', type=int, required=True)
    args = arguments_parser.parse_args()
    postprocessing_process(args.state_precincts_geojson_file, args.ensemble_id, args.plan_id)