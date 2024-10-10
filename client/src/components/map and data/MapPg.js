import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import DataPg from "./DataPg";
import PlaceholderMessage from "./PlaceHolderMessage";
import "../../stylesheets/map and data/map.css";
import "../../stylesheets/BackButton.css";
import axios from "axios";
//initial state
const initialState = {
  districtName: "",
  population: "0",
  income: "0",
  politicalLean: "0",
  totalPrecinct: "0",
  homeownershipRate: "0%",
  unemploymentRate: "0%",
  povertyRate: "0%",
};

//go back button
function BackButtonControl({ resetView }) {
  const map = useMap();

  useEffect(() => {
    const backButton = L.control({ position: "topright" });
    const ensembleButton = L.control({ position: "topright" });

    //create the ensemble button
    ensembleButton.onAdd = () => {
      const button = L.DomUtil.create(
        "button",
        "leaflet-bar leaflet-control leaflet-control-custom"
      );
      button.innerText = "Compare with Ensemble Data";
      button.style.fontSize = "15px";
      button.style.backgroundColor = "#fff";
      button.style.border = "2px solid #3388ff";
      button.style.cursor = "pointer";
      button.style.padding = "8px";
      button.style.width = "150px";
      button.style.height = "50px";
      button.title = "Go back to default view";
      return button;
    };

    //create the back button
    backButton.onAdd = () => {
      const button = L.DomUtil.create(
        "button",
        "leaflet-bar leaflet-control leaflet-control-custom"
      );
      button.innerText = "Go Back";
      button.style.fontSize = "15px";
      button.style.backgroundColor = "#fff";
      button.style.border = "2px solid #3388ff";
      button.style.cursor = "pointer";
      button.style.padding = "8px";
      button.style.width = "150px";
      button.style.height = "50px";
      button.title = "Go back to default view";

      //calls the reset map view function
      button.onclick = () => {
        resetView(map);
      };
      return button;
    };

    //adds the buttons to the map
    backButton.addTo(map);
    ensembleButton.addTo(map);

    return () => {
      //remove the button from the map or else the button will be created over and over again
      map.removeControl(backButton);
      map.removeControl(ensembleButton);
    };
  }, [map, resetView]);

  return null;
}

//controls the feature
function FeatureInteraction({
  geojsonData,
  onFeatureClick,
  disableNavigation,
  setHoverState,
  setState,
  featureType,
}) {
  const map = useMap();

  //disables the moving function of map
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
    fillColor: featureType === "district" ? "#FF5733" : "#3388ff",
    weight: 2,
    opacity: 1,
    color: "white",
    dashArray: "3",
    fillOpacity: 0.7,
  };

  const highlightFeature = (layer) => {
    layer.setStyle({
      weight: 3,
      color: "#000000",
      dashArray: "",
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
            layer.bindTooltip(
              `${
                properties.name ||
                properties.NAMELSAD ||
                "Congressional District " + properties.DISTRICT
              }`,
              {
                permanent: false,
                direction: "auto",
                sticky: true,
              }
            );

            layer.on("mouseover", () => {
              highlightFeature(layer);
              setHoverState({
                districtName: `${
                  properties.name ||
                  properties.NAMELSAD ||
                  "Congressional District " + properties.DISTRICT
                }`,
              });
              layer.openTooltip();
            });

            layer.on("mouseout", () => {
              resetHighlight(layer);
              setHoverState({ districtName: "" });
              layer.closeTooltip();
            });

            layer.on("click", () => handleFeatureClick(feature, layer));
          }}
        />
      )}
    </>
  );
}

export default function MapPg() {
  const [state, setState] = useState(initialState);
  const [hoverState, setHoverState] = useState({ districtName: "" });
  const [dataVisible, setDataVisible] = useState(false);
  const [showDistricts, setShowDistricts] = useState(false);

  const [geojsonMaryland, setGeojsonMaryland] = useState(null);
  const [geojsonSouthCarolina, setGeojsonSouthCarolina] = useState(null);
  const [geojsonMarylandCongress, setGeojsonMarylandCongress] = useState(null);
  const [geojsonSouthCarolinaCongress, setGeojsonSouthCarolinaCongress] =
    useState(null);
  const [disableNavigation, setDisableNavigation] = useState(false);

  const defaultView = [37.1, -95.7];
  const defaultZoom = 4;

  //calls the fetch function with link
  /*
  useEffect(() => {
    fetchGeojsonData('/jack_mary_state.geojson', setGeojsonMaryland);
    fetchGeojsonData('/jack_south_state.geojson', setGeojsonSouthCarolina);
    fetchGeojsonData('/jack_mary_congress.geojson', setGeojsonMarylandCongress);
    fetchGeojsonData('/jack_south_congress.geojson', setGeojsonSouthCarolinaCongress);
    
  }, []);
  console.log('GeojsonMaryland:', geojsonMaryland);
  console.log('GeojsonSouthCarolina:', geojsonSouthCarolina);
  console.log('GeojsonSouthCarolinaCongress:', geojsonSouthCarolinaCongress);
  console.log('GeojsonMarylandCongress:', geojsonMarylandCongress);*/

  useEffect(() => {
    async function fetchData() {
      try {
        const response1 = await axios.get("http://localhost:8000/45");
        console.log("SC boundary data from server:", response1.data);
        setGeojsonSouthCarolina(response1.data);

        const response2 = await axios.get("http://localhost:8000/24");
        console.log("MD boundary data from server:", response2.data);
        setGeojsonMaryland(response2.data);

        //
        // const response3 = await axios.get("http://localhost:8000/24/districts");
        // console.log("SC districts boundary data from server:", response3.data);
        // setGeojsonMarylandCongress(response3.data);

        // const response4 = await axios.get("http://localhost:8000/45/districts");
        // console.log("MD districts boundary data from server:", response4.data);
        // setGeojsonSouthCarolinaCongress(response4.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }
    fetchData();
  }, []);

  useEffect(() => {
    //console.log("State updated: ", state);
  }, [state]);

  useEffect(() => {
    //console.log("Hover State updated: ", hoverState);
  }, [hoverState]);

  //fetchs the data given the url
  const fetchGeojsonData = async (url, setState) => {
    try {
      const response = await fetch(url);
      const data = await response.json();
      //console.log("GeoJSON data loaded from: ", url, data);
      setState(data);
    } catch (error) {
      //console.error(`Error loading GeoJSON from ${url}:`, error);
    }
  };

  const handleResetView = (map) => {
    map.setView(defaultView, defaultZoom);
    setState(initialState);
    setHoverState({ districtName: "" });
    setDataVisible(false);
    setDisableNavigation(false);
    setShowDistricts(false);
  };

  const fetch_district_boundary = async (state_code) => {
    const res = await axios.get(
      `http://localhost:8000/${state_code}/districts`
    );
    return res;
  };

  const onFeatureClick = (feature) => {
    const properties = feature.properties;
    //console.log('Feature clicked:', feature.properties);
    let newState = { ...initialState };

    if (properties.name === "Maryland") {
      const response3 = fetch_district_boundary(24);
      console.log("SC districts boundary data from server:", response3.data);
      setGeojsonMarylandCongress(response3.data);
      setShowDistricts(true);
    } else if (properties.name === "South Carolina") {
      const response4 = fetch_district_boundary(45);
      console.log("MD districts boundary data from server:", response4.data);
      setGeojsonSouthCarolinaCongress(response4.data);
      setShowDistricts(true);
    }
    setState(newState);
    setDataVisible(true);
    setDisableNavigation(true);
  };

  return (
    <div style={{ display: "flex" }}>
      <MapContainer
        center={defaultView}
        zoom={defaultZoom}
        style={{ height: "95vh", width: "60vw" }}
      >
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
