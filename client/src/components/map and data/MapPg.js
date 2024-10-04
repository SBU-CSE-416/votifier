import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, GeoJSON, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import DataPg from './DataPg';
import PlaceholderMessage from './PlaceHolderMessage';
import "../../stylesheets/map and data/map.css";

const initialState = {
  districtName: '',
  population: '0',
  income: '0',
  politicalLean: '0',
  totalPrecinct: '0',
  homeownershipRate: '0%',
  unemploymentRate: '0%',
  povertyRate: '0%',
};

function BackButtonControl({ resetView }) {
  const map = useMap();

  useEffect(() => {
    const backButton = L.control({ position: 'topright' });

    backButton.onAdd = () => {
      const button = L.DomUtil.create('button', 'leaflet-bar leaflet-control leaflet-control-custom');
      button.innerText = 'Back';
      button.style.backgroundColor = '#fff';
      button.style.border = '2px solid #3388ff';
      button.style.cursor = 'pointer';
      button.style.padding = '8px';
      button.title = 'Go back to default view';

      button.onclick = () => {
        resetView(map);
      };
      return button;
    };
    backButton.addTo(map);
    return () => {
      map.removeControl(backButton);
    };
  }, [map, resetView]);

  return null;
}

function FeatureInteraction({ geojsonData, onFeatureClick, disableNavigation, setHoverState, setState, featureType }) {
  const map = useMap();

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
    fillColor: featureType === 'district' ? '#FF5733' : '#3388ff', 
    weight: 2,
    opacity: 1,
    color: 'white',
    dashArray: '3',
    fillOpacity: 0.7,
  };

  const highlightFeature = (layer) => {
    layer.setStyle({
      weight: 3,
      color: '#000000',
      dashArray: '',
      fillOpacity: 0.9,
    });
  };

  const resetHighlight = (layer) => {
    layer.setStyle(geojsonStyle);
  };

  const handleFeatureClick = (feature, layer) => {
    highlightFeature(layer);
    const bounds = layer.getBounds();
    map.fitBounds(bounds);
    onFeatureClick(feature);
  };

  return (
    <>
      {geojsonData && (
        <GeoJSON
          data={geojsonData}
          style={geojsonStyle}
          onEachFeature={(feature, layer) => {
            const properties = feature.properties;

            layer.unbindTooltip();
            layer.bindTooltip(`${properties.name || properties.NAMELSAD || "Congressional District " + properties.DISTRICT  }`, {
              permanent: false,
              direction: 'auto',
              sticky: true,
            });

          
            layer.on('mouseover', () => {
              highlightFeature(layer);
              setHoverState({ districtName: `${properties.name || properties.NAMELSAD ||  "Congressional District " + properties.DISTRICT }` });
              layer.openTooltip();
            });

    
            layer.on('mouseout', () => {
              resetHighlight(layer);
              setHoverState({ districtName: '' }); 
              layer.closeTooltip();
            });

          
            layer.on('click', () => handleFeatureClick(feature, layer));
          }}
        />
      )}
    </>
  );
}


export default function MapPg() {
  const [state, setState] = useState(initialState);
  const [hoverState, setHoverState] = useState({ districtName: '' });
  const [dataVisible, setDataVisible] = useState(false);
  const [showDistricts, setShowDistricts] = useState(false);

  const [geojsonMaryland, setGeojsonMaryland] = useState(null);
  const [geojsonSouthCarolina, setGeojsonSouthCarolina] = useState(null);
  const [geojsonMarylandCongress, setGeojsonMarylandCongress] = useState(null);
  const [geojsonSouthCarolinaCongress, setGeojsonSouthCarolinaCongress] = useState(null);
  const [disableNavigation, setDisableNavigation] = useState(false);

  const defaultView = [37.1, -95.7];
  const defaultZoom = 4;

  useEffect(() => {
    fetchGeojsonData('/jack_mary_state.geojson', setGeojsonMaryland);
    fetchGeojsonData('/jack_south_state.geojson', setGeojsonSouthCarolina);
    fetchGeojsonData('/jack_mary_congress.geojson', setGeojsonMarylandCongress);
    fetchGeojsonData('/jack_south_congress.geojson', setGeojsonSouthCarolinaCongress);
    
  }, []);
  console.log('GeojsonMaryland:', geojsonMaryland);
    console.log('GeojsonSouthCarolina:', geojsonSouthCarolina);
  console.log('GeojsonSouthCarolinaCongress:', geojsonSouthCarolinaCongress);
    console.log('GeojsonMarylandCongress:', geojsonMarylandCongress);
  useEffect(() => {
    console.log("State updated: ", state);
  }, [state]);

  useEffect(() => {
    console.log("Hover State updated: ", hoverState);
  }, [hoverState]);

  const fetchGeojsonData = async (url, setState) => {
    try {
      const response = await fetch(url);
      const data = await response.json();
      console.log("GeoJSON data loaded from: ", url, data);
      setState(data);
    } catch (error) {
      console.error(`Error loading GeoJSON from ${url}:`, error);
    }
  };

  const handleResetView = (map) => {
    map.setView(defaultView, defaultZoom);
    setState(initialState); 
    setHoverState({ districtName: '' });
    setDataVisible(false);
    setDisableNavigation(false);
    setShowDistricts(false);
  };

  const onFeatureClick = (feature) => {
    
    const properties = feature.properties;
    console.log('Feature clicked:', feature.properties);
    let newState = { ...initialState };

    if (properties.name === 'Maryland') {
      newState = {
        ...initialState,
        districtName: 'Maryland',
        population: '6.165 million',
        income: '$94,790',
        politicalLean: 'Democratic',
        totalPrecinct: '1,990',
        homeownershipRate: '68.7%',
        unemploymentRate: '1.8%',
        povertyRate: '8.6%',
      };
      setShowDistricts(true);
    } else if (properties.name === 'South Carolina') {
      newState = {
        ...initialState,
        districtName: 'South Carolina',
        population: '5.283 million',
        income: '$54,864',
        politicalLean: 'Republican',
        totalPrecinct: '1,297',
        homeownershipRate: '69.5%',
        unemploymentRate: '3.5%',
        povertyRate: '13.2%',
      };
      setShowDistricts(true);
    }
    setState(newState);
    setDataVisible(true);
    setDisableNavigation(true);
  };

  return (
    <div style={{ display: 'flex' }}>
      <MapContainer center={defaultView} zoom={defaultZoom} style={{ height: '95vh', width: '60vw' }}>
        <TileLayer
          url="https://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}{r}.png"
          attribution="&copy; <a href='https://carto.com/'>CartoDB</a>"
        />

        {/* State Boundaries */}
        <FeatureInteraction
          geojsonData={geojsonMaryland}
          onFeatureClick={onFeatureClick}
          disableNavigation={disableNavigation}
          setHoverState={setHoverState}
          setState={setState}
          featureType="state"
        />

        <FeatureInteraction
          geojsonData={geojsonSouthCarolina}
          onFeatureClick={onFeatureClick}
          disableNavigation={disableNavigation}
          setHoverState={setHoverState}
          setState={setState}
          featureType="state"
        />

        {showDistricts && geojsonMarylandCongress && (
          <FeatureInteraction
            geojsonData={geojsonMarylandCongress}
            onFeatureClick={onFeatureClick}
            disableNavigation={disableNavigation}
            setHoverState={setHoverState}
            setState={setState}
            featureType="district"
          />
        )}

        {showDistricts && geojsonSouthCarolinaCongress && (
          <FeatureInteraction
            geojsonData={geojsonSouthCarolinaCongress}
            onFeatureClick={onFeatureClick}
            disableNavigation={disableNavigation}
            setHoverState={setHoverState}
            setState={setState}
            featureType="district"
          />
        )}

        <BackButtonControl resetView={handleResetView} />
      </MapContainer>

      {dataVisible ? <DataPg state={state} /> : <PlaceholderMessage />}
    </div>
  );
}
