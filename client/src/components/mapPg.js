import React, { useEffect } from 'react';
import 'leaflet/dist/leaflet.css'; // Import Leaflet CSS
import L from 'leaflet'; // Import Leaflet library

export default function MapPg() {
  useEffect(() => {
    let map; // Declare map variable here

    // Check if the map is already initialized
    if (!map) {
      // Initialize the map and set the view to Maryland's coordinates
      map = L.map('map').setView([39.0458, -76.6413], 8);

      // Add OpenStreetMap tile layer
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
      }).addTo(map);
    }

    const precinctsGeoJsonUrl = 'maryland-counties.geojson';

    function style(feature) {
      return {
        fillColor: '#3388ff',
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
      };
    }

    function highlightFeature(e) {
      const layer = e.target;
      layer.setStyle({
        weight: 5,
        color: '#666',
        dashArray: '',
        fillOpacity: 0.7,
      });

      const countyName = layer.feature.properties.name;
      document.getElementById('precinctName').innerText = `County: ${countyName}`;
    }

    function resetHighlight(e) {
      geojson.resetStyle(e.target); // Now `geojson` is defined in the outer scope
    }

    function zoomToFeature(e) {
      map.fitBounds(e.target.getBounds());
    }

    function onEachFeature(feature, layer) {
      layer.on({
        mouseover: highlightFeature,
        mouseout: resetHighlight,
        click: zoomToFeature,
      });
    }

    let geojson; // Declare geojson here
    fetch(precinctsGeoJsonUrl)
      .then((response) => response.json())
      .then((data) => {
        geojson = L.geoJSON(data, {
          style: style,
          onEachFeature: onEachFeature,
        }).addTo(map);
      })
      .catch((error) => {
        console.error('Error loading the GeoJSON file:', error);
      });

    // Cleanup function to remove the map when component unmounts
    return () => {
      if (map) {
        map.remove();
      }
    };
  }, []); // Empty dependency array means this runs once on component mount

  return (
    <div>
      <div id="map" style={{ height: '800px', width: '1500px', float: 'left', left: '1px', top:'2px'}}></div>
      <div id="info" style={{ height: '600px', width: '35%', float: 'right', padding: '10px', fontSize: '18px' }}>
        <h3>Selected Precinct</h3>
        <p id="precinctName">Click on a precinct to see its county name.</p>
      </div>
    </div>
  );
}
