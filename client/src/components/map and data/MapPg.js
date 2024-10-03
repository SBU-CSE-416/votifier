import React, { useEffect, useState } from 'react';
import 'leaflet/dist/leaflet.css'; // Import Leaflet CSS
import L from 'leaflet'; // Import Leaflet library
import DataPg from './DataPg';
import "../../stylesheets/map and data/map.css"

export default function MapPg() {
  const [districtName, setDistrictName] = useState('Click on a location to see its name.');
  const [population, setPopulation] = useState('Population: 0');
  const [income, setIncome] = useState('Median Income: 0');
  const [political_lean, setpolitical] = useState('Political Lean: 0');
  const [total_precinct, settotalprecinct] = useState('Total Precinct: 0');
  const [barData, setBarData] = useState({ bar1: 0, bar2: 0, bar3: 0, bar4: 0 });

  const [mapInstance, setMapInstance] = useState(null);

  //const [geojsonStateMaryland, setgeojsonStateMaryland] = useState(null);
  //const [geojsonStateSouthCarolina, setgeojsonStateSouthCarolina] = useState(null);
  //const [geojsonCongressionalMaryland, setgeojsonCongressionalMaryland] = useState(null);
  //const [geojsonCongressionalSouthCarolina, setgeojsonCongressionalSouthCarolina] = useState(null);

  //total precinct in each district
  var mary_district_precincts = [302,230,213,253,241,218,334,199];
  var south_district_precincts = [172,150,114,156,130,153,100];

  const defaultView = [37.1, -95.7];
  const defaultZoom = 4;

  var enteredstate = false;
  var currentState = 'us';

  var precinct_number = 0;
  var hover_box = 0;
  var click = 0;

  useEffect(() => {
    //let map = L.map('map').setView([37.1, -95.7], 4);
    let map = L.map('map', {
      dragging: false,            // Disable dragging
      scrollWheelZoom: false,     // Disable scroll to zoom
      doubleClickZoom: false,     // Disable double-click zoom
      zoomControl: false          // Hide zoom control
    }).setView([37.1, -95.7], 4);
    setMapInstance(map);

    let geojsonStateMaryland, geojsonStateSouthCarolina;
    let geojsonCongressionalMaryland, geojsonCongressionalSouthCarolina;
    let geojsonPrecinctMaryland;
    const marylandBounds = [[37.9116, -79.4870], [39.4623, -75.0410]];
    //const marylandBounds = [[39.7,-79.5],[39.,-74.9]];
    const southCarolinaBounds = [[32.0343, -83.3533], [35.2152, -78.4336]];

    /*const maryland_districts = [
      [[39.8, -79.6], [37.9, -77.0]],  // District 1
      [[39.5, -76.9], [39.1, -76.4]],  // District 2
      [[39.5, -77.0], [38.8, -76.2]],  // District 3
      [[39.2, -77.0], [38.6, -76.4]],  // District 4
      [[39.0, -77.1], [38.2, -76.0]],  // District 5
      [[39.7, -79.5], [39.2, -77.2]],  // District 6
      [[39.5, -77.0], [39.2, -76.7]],  // District 7
      [[39.4, -77.5], [38.9, -76.8]]   // District 8
    ];*/
    //const maryland_districts = [[39.7,-79.5],[39.7,-74.9]]
    const maryland_districts = [[37.9116, -79.4870], [39.4623, -75.0410]];
  

    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}{r}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="https://carto.com/">CartoDB</a>'
    }).addTo(map);

    const stateGeoJsonUrlMaryland = '/jack_mary_state.geojson';
    const congressionalDistrictMaryland = '/jack_mary_congress.geojson';
    const stateGeoJsonUrlSouthCarolina = '/jack_south_state.geojson';
    const congressionalDistrictSouthCarolina = '/jack_south_congress.json';
    const precinctMaryland = '/jack_maryland_precinct.geojson';

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
          setpolitical('Political lean: Democratic');
          settotalprecinct('Total Precinct: 1,990');
          currentState = "maryland";
        } else {
          setPopulation('Population: 5.283 million (2022)');
          setIncome('Median Income: 33,511 USD (2022)');
          setpolitical('Political lean: Republican');
          settotalprecinct('Total Precinct: 1,297');
          currentState = "southcarolina";
        }
      } else {
        const congdistrictname1 = properties.NAMELSAD || properties.DISTRICT;
        setDistrictName(`Congressional District: ${congdistrictname1}`);

        if(currentState == "maryland"){
          precinct_number = parseInt(congdistrictname1)-1;
          settotalprecinct("Total Precinct: "+mary_district_precincts[congdistrictname1-1]);
         

        }else if(currentState == "southcarolina"){
          let extractedNumber = parseInt(congdistrictname1.slice(-1)); 
          precinct_number = extractedNumber-1;
          settotalprecinct("Total Precinct: "+south_district_precincts[extractedNumber-1]);
        }
      }

      var whiteBox = document.createElement('div');
      whiteBox.id = 'white-box'; // Give it an id to remove it later
      whiteBox.style.position = 'absolute';
      whiteBox.style.bottom = '100px';  // Position as needed
      whiteBox.style.right = '700px';   // Position as needed
      whiteBox.style.width = '200px';
      whiteBox.style.height = '100px';
      whiteBox.style.backgroundColor = 'white';
      whiteBox.style.border = '1px solid black';
      whiteBox.style.zIndex = '1000';  // Ensure it stays on top

      // Add the white box to the body (or any container element)
      document.body.appendChild(whiteBox);
      console.log("current state"+currentState);
      var pElement = document.createElement('p');
      if(layer.feature.properties.name === "Maryland"){
        pElement.textContent = layer.feature.properties.name +"\nPopulation: 6.165 million \nPolitical lean: Democratic"; // Set the text content of the <p> element
      }else if(layer.feature.properties.name === "South Carolina"){
        pElement.textContent = layer.feature.properties.name +"\nPopulation: 5.283 million \nPolitical lean: Republican"; // Set the text content of the <p> element
      }else{
        pElement.textContent = "Congressional District: "+(precinct_number+1);
      }
      pElement.style.margin = '10px'; // Optional: add margin to the <p> element

      // Append the <p> element to the white box
      whiteBox.appendChild(pElement);
      updateBarGraph();
    }

    function resetHighlight(e) {
      geojsonStateMaryland && geojsonStateMaryland.resetStyle(e.target);
      geojsonStateSouthCarolina && geojsonStateSouthCarolina.resetStyle(e.target);
      setDistrictName('Click on location to see its name.');
      setPopulation('Population: 0');
      setIncome('Median Income: 0');
      setpolitical('Political lean: 0');
      settotalprecinct('Total Precinct: 0');
      var whiteBox = document.getElementById('white-box');
      if (whiteBox) {
        whiteBox.remove();
      }
    }

    /*
    const currentCenter = map.getCenter();
    const currentZoom = map.getZoom();
    const isDefaultView = currentCenter.lat === defaultView[0] && currentCenter.lng === defaultView[1] && currentZoom === defaultZoom;
    if (isDefaultView) {
      if (geojsonCongressionalMaryland) {
        map.removeLayer(geojsonCongressionalMaryland);
      }
      if (geojsonCongressionalSouthCarolina) {
        map.removeLayer(geojsonCongressionalSouthCarolina);
      }
    }*/
    const checkMapSize = () => {
      const center = map.getCenter(); // Get the cdcurrent center
      const currentZoom = map.getZoom(); // Get the current zoom level
      if (center.lat !== 37.1 || center.lng !== -95.7 || currentZoom !== 4) {
        console.log("hello");
        enteredstate = true;
      }else{
        if(enteredstate){
          if(geojsonCongressionalMaryland){
            resetHighlight(geojsonStateMaryland);
            map.removeLayer(geojsonCongressionalMaryland);
            map.removeLayer(geojsonPrecinctMaryland);
            geojsonStateMaryland.addTo(map);
            currentState = 'us';
          }
          if(geojsonCongressionalSouthCarolina){
            resetHighlight(geojsonStateSouthCarolina);
            map.removeLayer(geojsonCongressionalSouthCarolina);
            geojsonStateSouthCarolina.addTo(map);
            currentState = 'us';
          }
          enteredstate = false;
        }
      }
      
    };
    const intervalId = setInterval(checkMapSize, 1000);


    function zoomToFeature(e, state) {
      console.log("current: "+state);
      if(currentState === 'us'){
        currentState = state;
      }
      if (currentState === 'maryland') {
        map.fitBounds(marylandBounds);
        geojsonStateMaryland && map.removeLayer(geojsonStateMaryland);
        geojsonCongressionalMaryland.addTo(map);
        if(click == 2){
          currentState = 'maryland_precinct';
        }
      } else if (currentState === 'southcarolina') {
        map.fitBounds(southCarolinaBounds);
        geojsonStateSouthCarolina && map.removeLayer(geojsonStateSouthCarolina);
        geojsonCongressionalSouthCarolina.addTo(map);
      } else if (currentState === 'maryland_precinct'){
        //map.fitBounds(maryland_districts[precinct_number]);
        map.fitBounds(maryland_districts);
        geojsonCongressionalMaryland && map.removeLayer(geojsonCongressionalMaryland);
        geojsonPrecinctMaryland.addTo(map);
        click = 0;
      }


    }

    function onEachFeature(feature, layer, state) {
      layer.on({
        click: (e) => {
          click += 1;
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

    fetch(precinctMaryland)
      .then(response => response.json())
      .then(data => {
        geojsonPrecinctMaryland = L.geoJSON(data, {
          style,
          onEachFeature: (feature, layer) => {
            onEachFeature(feature, layer, 'maryland_precinct');
          }
        });
      })
    .catch(error => console.error('Error loading GeoJSON:', error));




    return () => {
      clearInterval(intervalId);
      map.remove();
    };
  }, []);

    

  const resetMapViewToDefault = () => {
    if (mapInstance) {
      mapInstance.setView([37.1, -95.7], 4);
      //mapInstance.removeLayer(geojsonStateMaryland);
    }

    setDistrictName('Click on location to see its name.');
    setPopulation('Population: 0');
    setIncome('Median Income: 0');
    setpolitical('Political lean: 0');
    settotalprecinct('Total Precinct: 0');
    setBarData({ bar1: 0, bar2: 0, bar3: 0, bar4: 0 });

  };

  return (
    <div style={{ display: 'flex' }}>
      <div id="map" style={{ height: '95vh', width: '50vw' }}></div>
      <button 
    onClick={resetMapViewToDefault} 
    style={{
        position: "absolute",
        top: "100px",
        left: "250px",
        padding: "10px", // Optional: Adds some padding for better visibility
        zIndex: 1000      // Optional: Ensures the button appears above other elements
    }}>
      
    Go Back
</button>
      <DataPg ></DataPg>
    </div>
  );
}
