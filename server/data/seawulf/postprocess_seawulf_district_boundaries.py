import geopandas as gpd
import pandas as pd
import argparse
from geopandas import GeoDataFrame as gdf
import shapely
from shapely.geometry import shape, MultiPoint, Polygon, MultiPolygon, LineString
import matplotlib.pyplot as plt
import json
import matplotlib.patches as mpatches
from seawulf_constants import *

COLORS = ['red', 'orange', 'yellow', 'green', 'cyan', 'blue', 'magenta', 'purple']

def plot_new_district_boundaries(filepath, state_abbr, ensemble_id, plan_id):

    with open(filepath, 'r') as districts_file:
      districts_json = json.load(districts_file)
    districts = []
    i = 0
    for feature in districts_json['features']:
        district_id = feature['properties']['NAME']
        district_geometry = shape(feature['geometry'])
        districts.append((district_id, district_geometry, i))
        i += 1

    # Visual plot display params
    PLOT_TITLE = (f"{state_abbr} | Ensemble {ensemble_id}, Plan {plan_id}")
    GEOMETRY_LINE_WIDTH = 0.5
    fig, ax = plt.subplots(figsize=(15,10))
    for unique_district_id, geometry, i in districts:
        color = COLORS[i]  # Assign a unique color
        print(f"Coloring district {unique_district_id} line in {color}")
        if isinstance(geometry, LineString):
            x, y = geometry.xy
            ax.plot(x, y, color=color, linewidth=GEOMETRY_LINE_WIDTH)

    # Final plot adjustments
    ax.set_aspect('equal', adjustable='datalim')
    plt.title(PLOT_TITLE, fontsize=20)
    plt.savefig(f"results/ensemble_{ensemble_id}/all_plans_plotted/{plan_id}.png")
    plt.close()
    return

def postprocessing_process(precinct_boundaries_filepath, state_abbr, ensemble_id, plan_id):
    precinct_boundary_gdf = gpd.read_file(precinct_boundaries_filepath)
    buffer_distance = 0.0003
    
    district_precincts_filepath = f"results/ensemble_{ensemble_id}/plan_{plan_id}/district_precincts_results.json"
    # district_precincts_filepath = "district_precincts_results_sc.json"
    output_filepath = f"results/ensemble_{ensemble_id}/plan_{plan_id}/proposed_districts.geojson"
    # output_filepath = "proposed_districts_sc_full.geojson"

    with open(district_precincts_filepath, 'r') as district_precincts_file:
      district_precincts = json.load(district_precincts_file)

    districts_gdfs = []
    for district_id, precincts in district_precincts.items():

      district_precincts_gdf = precinct_boundary_gdf[precinct_boundary_gdf['UNIQUE_ID'].isin(precincts)]
      dissolved_geometry = district_precincts_gdf.dissolve(by='UNIQUE_ID').buffer(buffer_distance).simplify(tolerance=buffer_distance)
      union_geometry = dissolved_geometry.geometry.unary_union

      if union_geometry.geom_type == 'MultiPolygon':
        biggest_size = -1
        polygon_index = -1
        polygons = {}
        for i, polygon in enumerate(union_geometry.geoms, start=1):
            # Case 1
            polygons[i] = polygon
            if polygon.area > biggest_size:
               polygon_index = i
               biggest_size = polygon.area
            # Create a GeoDataFrame for the current polygon
            polygon_gdf = gpd.GeoDataFrame({
                'NAME': [f"District {district_id} - Part {i}"],
                'geometry': [polygon]
            }, crs=district_precincts_gdf.crs)

            # Save the polygon to a unique GeoJSON file
            # polygon_gdf.to_file(f"results/ensemble_{ensemble_id}/plan_{plan_id}/proposed_districts_{state_abbr}_{district_id}_{i}.geojson", driver='GeoJSON')
        boundary_geometry = polygons[polygon_index].exterior
        district_boundary_gdf = gpd.GeoDataFrame({
            'NAME': [f"Congressional District {district_id}"],
            'geometry': [boundary_geometry]
        }, crs=district_precincts_gdf.crs)
        district_boundary_gdf.to_file(f"results/ensemble_{ensemble_id}/plan_{plan_id}/proposed_districts_{state_abbr}_{district_id}.geojson", driver='GeoJSON')
        district_precincts_gdf.to_file(f"results/ensemble_{ensemble_id}/plan_{plan_id}/proposed_districts_{state_abbr}_precincts.geojson", driver='GeoJSON')
        districts_gdfs.append(district_boundary_gdf)
      else:
        # Case 0
        boundary_geometry = union_geometry.exterior
        district_boundary_gdf = gpd.GeoDataFrame({
        'NAME': [f"Congressional District {district_id}"],
        'geometry': [boundary_geometry]
        }, crs=district_precincts_gdf.crs)
        district_boundary_gdf.to_file(f"results/ensemble_{ensemble_id}/plan_{plan_id}/proposed_districts_{state_abbr}_{district_id}.geojson", driver='GeoJSON')
        district_precincts_gdf.to_file(f"results/ensemble_{ensemble_id}/plan_{plan_id}/proposed_districts_{state_abbr}_precincts.geojson", driver='GeoJSON')
        districts_gdfs.append(district_boundary_gdf)

    all_districts_gdf = gpd.GeoDataFrame(pd.concat(districts_gdfs, ignore_index=True))
    all_districts_gdf.to_file(output_filepath, driver='GeoJSON')
    return output_filepath

if __name__ == "__main__":
    arguments_parser = argparse.ArgumentParser()
    arguments_parser.add_argument('--state_precincts_geojson_file', type=str, required=True)
    arguments_parser.add_argument('--state_abbr', type=str, required=True)
    arguments_parser.add_argument('--ensemble_id', type=int, required=True)
    arguments_parser.add_argument('--plan_id', type=int, required=True)
    args = arguments_parser.parse_args()
    filepath = postprocessing_process(args.state_precincts_geojson_file, args.state_abbr, args.ensemble_id, args.plan_id)
    # filepath = "proposed_districts_sc_full.geojson"
    plot_new_district_boundaries(filepath, args.state_abbr, args.ensemble_id, args.plan_id)