import React, { useEffect, useState } from 'react';
import 'leaflet/dist/leaflet.css'; // Import Leaflet CSS
import L from 'leaflet'; // Import Leaflet library

export default function MapPg() {
  const [districtName, setDistrictName] = useState('Click on a location to see its name.');
  const [population, setPopulation] = useState('Population: 0');
  const [income, setIncome] = useState('Median Income: 0');
  const [political_lean, setpolitical] = useState('Political Lean: 0');
  const [total_precinct, settotalprecinct] = useState('Total Precinct: 0');
  const [barData, setBarData] = useState({ bar1: 0, bar2: 0, bar3: 0, bar4: 0 });
  const [mapInstance, setMapInstance] = useState(null);


  useEffect(() => {
    let map = L.map('map').setView([37.1, -95.7], 4);
    setMapInstance(map);

    let geojsonStateMaryland, geojsonStateSouthCarolina;
    let geojsonCongressionalMaryland, geojsonCongressionalSouthCarolina;
    const marylandBounds = [[37.9116, -79.4870], [39.4623, -75.0410]];
    const southCarolinaBounds = [[32.0343, -83.3533], [35.2152, -78.4336]];

    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}{r}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="https://carto.com/">CartoDB</a>'
    }).addTo(map);

    const stateGeoJsonUrlMaryland = '/jack_mary_state.geojson';
    const congressionalDistrictMaryland = '/jack_mary_congress.geojson';
    const stateGeoJsonUrlSouthCarolina = '/jack_south_state.geojson';
    const congressionalDistrictSouthCarolina = '/jack_south_congress.json';

    function style(feature) {
      return {
        fillColor: '#3388ff',
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7
      };
    }

    function highlightFeature(e) {
      var layer = e.target;
      layer.bringToFront();
      layer.setStyle({
        weight: 5,
        color: '#000000',
        dashArray: '',
        fillOpacity: 0.7,
      });

      const properties = layer.feature.properties;
      if (properties.name === "Maryland" || properties.name === "South Carolina") {
        setDistrictName(`State: ${properties.name}`);
        if (properties.name === "Maryland") {
          setPopulation('Population: 6.165 million (2022)');
          setIncome('Median Income: 47,513 USD (2022)');
        } else {
          setPopulation('Population: 5.283 million (2022)');
          setIncome('Median Income: 33,511 USD (2022)');
        }
      } else {
        const congdistrictname1 = properties.NAMELSAD || properties.DISTRICT;
        setDistrictName(`Congressional District: ${congdistrictname1}`);
      }
      updateBarGraph();
    }

    function resetHighlight(e) {
      geojsonStateMaryland && geojsonStateMaryland.resetStyle(e.target);
      geojsonStateSouthCarolina && geojsonStateSouthCarolina.resetStyle(e.target);
      setDistrictName('Click on location to see its name.');
      setPopulation('Population: 0');
      setIncome('Median Income: 0');
    }

    /*
    function resetcongressional(e){
      geojsonStateMaryland && geojsonStateMaryland.resetStyle(e.target);
      geojsonStateSouthCarolina && geojsonStateSouthCarolina.resetStyle(e.target);

    }*/

    function zoomToFeature(e, state) {
      if (state === 'maryland') {
        map.fitBounds(marylandBounds);
        geojsonStateMaryland && map.removeLayer(geojsonStateMaryland);
        geojsonCongressionalMaryland.addTo(map);
      } else if (state === 'southCarolina') {
        map.fitBounds(southCarolinaBounds);
        geojsonStateSouthCarolina && map.removeLayer(geojsonStateSouthCarolina);
        geojsonCongressionalSouthCarolina.addTo(map);
      }
    }

    function onEachFeature(feature, layer, state) {
      layer.on({
        click: (e) => {
          highlightFeature(e);
          zoomToFeature(e, state);
        },
        mouseover: highlightFeature,
        mouseout: resetHighlight
      });
    }

    function updateBarGraph() {
      const bar1 = Math.floor(Math.random() * 101);
      const bar2 = Math.floor(Math.random() * 101);
      const bar3 = Math.floor(Math.random() * 101);
      const bar4 = Math.floor(Math.random() * 101);
      setBarData({ bar1, bar2, bar3, bar4 });
    }

    // Fetch only state boundaries first
    fetch(stateGeoJsonUrlMaryland)
      .then(response => response.json())
      .then(data => {
        geojsonStateMaryland = L.geoJSON(data, {
          style,
          onEachFeature: (feature, layer) => {
            onEachFeature(feature, layer, 'maryland');
          }
        }).addTo(map);
      })
      .catch(error => console.error('Error loading GeoJSON:', error));

    fetch(stateGeoJsonUrlSouthCarolina)
      .then(response => response.json())
      .then(data => {
        geojsonStateSouthCarolina = L.geoJSON(data, {
          style,
          onEachFeature: (feature, layer) => {
            onEachFeature(feature, layer, 'southCarolina');
          }
        }).addTo(map);
      })
      .catch(error => console.error('Error loading GeoJSON:', error));

    // Load congressional districts but do not add to the map until a state is clicked
    fetch(congressionalDistrictMaryland)
      .then(response => response.json())
      .then(data => {
        geojsonCongressionalMaryland = L.geoJSON(data, {
          style,
          onEachFeature: (feature, layer) => {
            onEachFeature(feature, layer, 'maryland');
          }
        });
      })
      .catch(error => console.error('Error loading GeoJSON:', error));

    fetch(congressionalDistrictSouthCarolina)
      .then(response => response.json())
      .then(data => {
        geojsonCongressionalSouthCarolina = L.geoJSON(data, {
          style,
          onEachFeature: (feature, layer) => {
            onEachFeature(feature, layer, 'southCarolina');
          }
        });
      })
      .catch(error => console.error('Error loading GeoJSON:', error));

    return () => {
      map.remove();
    };
  }, []);

  const resetMapViewToDefault = () => {
    if (mapInstance) {
      mapInstance.setView([37.1, -95.7], 4);
    }
    setDistrictName('Click on location to see its name.');
    setPopulation('Population: 0');
    setIncome('Median Income: 0');
    setBarData({ bar1: 0, bar2: 0, bar3: 0, bar4: 0 });

  };

  return (
    <div style={{ display: 'flex' }}>
      <div id="map" style={{ height: '95vh', width: '1000px' }}></div>
      <div id="info" style={{ height: '600px', width: '35%', padding: '10px', fontSize: '18px' }}>
        <h3>Selected Location</h3>
        <p>{districtName}</p>
        <p>{population}</p>
        <p>{income}</p>
        <p>{political_lean}</p>
        <p>{total_precinct}</p>

        <div id="barGraphContainer">
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar1}%`, backgroundColor: '#3388ff' }}>{barData.bar1}%</div></div>
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar2}%`, backgroundColor: '#3388ff' }}>{barData.bar2}%</div></div>
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar3}%`, backgroundColor: '#3388ff' }}>{barData.bar3}%</div></div>
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar4}%`, backgroundColor: '#3388ff' }}>{barData.bar4}%</div></div>
        </div>
        <button onClick={resetMapViewToDefault} style={{ marginTop: '10px', padding: '10px', backgroundColor: '#3388ff', color: 'white', border: 'none', cursor: 'pointer' }}>
          Reset
        </button>
      </div>
    </div>
  );
}
