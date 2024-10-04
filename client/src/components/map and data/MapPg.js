import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, GeoJSON, Tooltip, useMap } from 'react-leaflet';
import L from 'leaflet'; // Import Leaflet to access L.Control
import 'leaflet/dist/leaflet.css';
import DataPg from './DataPg';
import "../../stylesheets/map and data/map.css";

// Custom Leaflet control component for the "Back" button
function BackButtonControl({ resetView }) {
  const map = useMap(); // Get access to the map instance

  useEffect(() => {
    // Create a new Leaflet control
    const backButton = L.control({ position: 'topright' });

    // Define the onAdd function to create and attach the button element
    backButton.onAdd = () => {
      const button = L.DomUtil.create('button', 'leaflet-bar leaflet-control leaflet-control-custom');
      button.innerText = 'Back';
      button.style.backgroundColor = '#fff';
      button.style.border = '2px solid #3388ff';
      button.style.cursor = 'pointer';
      button.style.padding = '8px';
      button.title = 'Go back to default view';
      
      // Attach click handler to reset the view
      button.onclick = () => {
        // Use the map object to set the view directly
        resetView(map); // Call the resetView function with the map object
      };
      return button;
    };

    // Add the control to the map
    backButton.addTo(map);

    // Cleanup: Remove the control when the component is unmounted
    return () => {
      map.removeControl(backButton);
    };
  }, [map, resetView]);

  return null; // This component does not render anything directly
}

// Custom component to handle feature interactions and map zoom
function FeatureInteraction({ geojsonData, onFeatureClick, districtName, disableNavigation }) {
  const map = useMap(); // Get the map instance from the context

  // Handle enabling and disabling map navigation
  useEffect(() => {
    if (disableNavigation) {
      map.dragging.disable();
      map.scrollWheelZoom.disable();
      map.doubleClickZoom.disable();
      map.touchZoom.disable();
      map.boxZoom.disable();
    } else {
      map.dragging.enable();
      map.scrollWheelZoom.enable();
      map.doubleClickZoom.enable();
      map.touchZoom.enable();
      map.boxZoom.enable();
    }
  }, [disableNavigation, map]);

  const geojsonStyle = {
    fillColor: '#3388ff',
    weight: 2,
    opacity: 1,
    color: 'white',
    dashArray: '3',
    fillOpacity: 0.7,
  };

  // Function to highlight a feature when hovered
  const highlightFeature = (layer) => {
    layer.setStyle({
      weight: 5,
      color: '#000000',
      dashArray: '',
      fillOpacity: 0.7,
    });
  };

  // Function to reset highlight when not hovered
  const resetHighlight = (layer) => {
    layer.setStyle(geojsonStyle);
  };

  // Handle feature click and zoom into the clicked feature
  const handleFeatureClick = (feature, layer) => {
    highlightFeature(layer);
    const bounds = layer.getBounds();
    map.fitBounds(bounds);
    onFeatureClick(feature); // Call the parent function to handle the feature click
  };

  return (
    <>
      {geojsonData && (
        <GeoJSON
          data={geojsonData}
          style={geojsonStyle}
          onEachFeature={(feature, layer) => {
            layer.on({
              mouseover: () => highlightFeature(layer),
              mouseout: () => resetHighlight(layer),
              click: () => handleFeatureClick(feature, layer),
            });
          }}
        >
          <Tooltip sticky>{districtName}</Tooltip>
        </GeoJSON>
      )}
    </>
  );
}

// Main component to render the Map and GeoJSON features
export default function MapPg() {
  const [districtName, setDistrictName] = useState('Click on a location to see its name.');
  const [population, setPopulation] = useState('Population: 0');
  const [income, setIncome] = useState('Median Income: 0');
  const [politicalLean, setPoliticalLean] = useState('Political Lean: 0');
  const [totalPrecinct, setTotalPrecinct] = useState('Total Precinct: 0');
  const [geojsonMaryland, setGeojsonMaryland] = useState(null);
  const [geojsonSouthCarolina, setGeojsonSouthCarolina] = useState(null);
  const [disableNavigation, setDisableNavigation] = useState(false);

  const defaultView = [37.1, -95.7];
  const defaultZoom = 4;

  // Load GeoJSON data on component mount using useEffect
  useEffect(() => {
    fetchGeojsonData('/jack_mary_state.geojson', setGeojsonMaryland);
    fetchGeojsonData('/jack_south_state.geojson', setGeojsonSouthCarolina);
  }, []);

  const fetchGeojsonData = async (url, setState) => {
    try {
      const response = await fetch(url);
      const data = await response.json();
      setState(data);
    } catch (error) {
      console.error(`Error loading GeoJSON from ${url}:`, error);
    }
  };

  // Event handler for resetting map view to default
  const handleResetView = (map) => {
    map.setView(defaultView, defaultZoom); 
    setDistrictName('Click on a location to see its name.');
    setPopulation('Population: 0');
    setIncome('Median Income: 0');
    setPoliticalLean('Political Lean: 0');
    setTotalPrecinct('Total Precinct: 0');
    setDisableNavigation(false); 
  };

  const onFeatureClick = (feature) => {
    const properties = feature.properties;
    if (properties.name === 'Maryland') {
      setDistrictName('State: Maryland');
      setPopulation('Population: 6.165 million (2022)');
      setIncome('Median Income: 47,513 USD (2022)');
      setPoliticalLean('Political lean: Democratic');
      setTotalPrecinct('Total Precinct: 1,990');
    } else if (properties.name === 'South Carolina') {
      setDistrictName('State: South Carolina');
      setPopulation('Population: 5.283 million (2022)');
      setIncome('Median Income: 33,511 USD (2022)');
      setPoliticalLean('Political lean: Republican');
      setTotalPrecinct('Total Precinct: 1,297');
    }

    setDisableNavigation(true);
  };

  return (
    <div style={{ position: 'relative' }}>
      <MapContainer center={defaultView} zoom={defaultZoom} style={{ height: '95vh', width: '60vw' }}>
        <TileLayer
          url="https://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}{r}.png"
          attribution="&copy; <a href='https://carto.com/'>CartoDB</a>"
        />

        <FeatureInteraction
          geojsonData={geojsonMaryland}
          onFeatureClick={onFeatureClick}
          districtName={districtName}
          disableNavigation={disableNavigation}
        />

        <FeatureInteraction
          geojsonData={geojsonSouthCarolina}
          onFeatureClick={onFeatureClick}
          districtName={districtName}
          disableNavigation={disableNavigation}
        />

        <BackButtonControl resetView={handleResetView} />
      </MapContainer>

      <DataPg resetMapViewToDefault={districtName} />
    </div>
  );
}
