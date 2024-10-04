import React, { useEffect, useState } from 'react';
import { MapContainer, TileLayer, GeoJSON, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import DataPg from './DataPg';
import PlaceholderMessage from './PlaceHolderMessage';
import "../../stylesheets/map and data/map.css";
import "../../stylesheets/BackButton.css"

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
    //let map = L.map('map').setView([37.1, -95.7], 4);
    let map = L.map('map', {
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
      whiteBox.style.right = '1000px';   // Position as needed
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
   /*
    const checkMapSize = () => {
      const center = map.getCenter(); // Get the cdcurrent center
      const currentZoom = map.getZoom(); // Get the current zoom level
      if (center.lat !== 37.1 || center.lng !== -95.7 || currentZoom !== 4) {
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
    const intervalId = setInterval(checkMapSize, 1000);*/


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

      map.dragging.disable();          // Disable dragging
      map.scrollWheelZoom.disable();   // Disable scroll to zoom
      map.doubleClickZoom.disable();   // Disable double-click zoom
      map.zoomControl.remove();


    }

    function onEachFeature(feature, layer, state) {
      layer.on({
        click: (e) => {
          if(state === 'maryland'){
            click += 1;
          }
          highlightFeature(e);
          zoomToFeature(e, state);
          setClickedFeature(feature.properties);
          console.log("Passed feature: "+e);
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


    //buttton variable
    const goBackButton = document.createElement('button');
    goBackButton.textContent = 'Go Back';
    goBackButton.style.position = 'absolute';
    goBackButton.style.top = '200px';
    goBackButton.style.left = '10px';
    goBackButton.style.zIndex = '1000';
    // Event handler for the go back button
    const handleGoBack = () => {
      map.setView([37.1, -95.7], 4);
        //mapInstance.removeLayer(geojsonStateMaryland);
      // Example: Reset state or change view
      // resetToDefaultView();
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

      map.dragging.enable();          
      map.scrollWheelZoom.enable();   
      map.doubleClickZoom.enable();   
      L.control.zoom().addTo(map); 
    };
    // Attach the event listener
    goBackButton.addEventListener('click', handleGoBack);
    // Add the button to the map container or body
    document.body.appendChild(goBackButton);






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
      //clearInterval(intervalId);
      map.remove();
      goBackButton.removeEventListener('click', handleGoBack);
      document.body.removeChild(goBackButton);
    };
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
        population: '6.161 million',
        income: '$94,991',
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
        population: '5.142 million',
        income: '$64,115',
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
