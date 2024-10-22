import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import DataPg from "./DataPg";
import PlaceholderMessage from "./PlaceHolderMessage";
import "../../stylesheets/map and data/map.css";
import "../../stylesheets/BackButton.css";
import axios from "axios";
const initialState = {
  box1: {
    title: "State Name",
    value: "",
  },
  box2: {
    title: "Population",
    value: "0",
  },
  box3: {
    title: "Median Household Income",
    value: "0",
  },
  box4: {
    title: "Political Lean",
    value: "NaN",
  },
  box5: {
    title: "Total Precinct",
    value: "0",
  },
  box6: {
    title: "Voting Population",
    value: "0",
  },
};

function BackButtonControl({ resetView }) {
  const map = useMap();

  useEffect(() => {
    const backButton = L.control({ position: "topright" });
    const ensembleButton = L.control({ position: "topright" });
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
  featureType,
}) {
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
                properties.NAME ||
                "Congressional District " + properties.DISTRICT
              }`,
              {
                permanent: false,
                direction: "auto",
                sticky: true,
              }
            );
            //   layer.bindPopup(
            //     `<strong>District:</strong> ${properties.name || "District " + properties.DISTRICT}<br>
            //      <strong>Details:</strong> ${properties.details || "No additional details available."}`
            // );

            layer.on("mouseover", () => {
              highlightFeature(layer);
              layer.openTooltip();
            });

            layer.on("mouseout", () => {
              resetHighlight(layer);
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

  useEffect(() => {
    async function fetchData() {
      try {
        const response1 = await axios.get("http://localhost:8000/45");

        setGeojsonSouthCarolina(response1.data);
        console.log("SC boundary data from server:", response1.data);

        const response2 = await axios.get("http://localhost:8000/24");

        setGeojsonMaryland(response2.data);
        console.log("MD boundary data from server:", response2.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchData();
  }, []);

  useEffect(() => {}, [hoverState]);

  const fetch_district_boundary = async (state_code) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/${state_code}/districts`
      );
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };
  const fetch_state_deomographics = async (state_code) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/${state_code}/demographics`
      );
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
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
  const onFeatureClick = async (feature) => {
    const properties = feature.properties;

    if (properties.name === "Maryland") {
      const response3 = await fetch_district_boundary(24);
      console.log("MD districts boundary data from server:", response3.data);
      setGeojsonMarylandCongress(response3.data);

      const state_data = await fetch_state_deomographics(24);
      console.log("Maryland demographics data:", state_data.data);
      setState(state_data.data);
      setShowDistricts(true);
    } else if (properties.name === "South Carolina") {
      const response4 = await fetch_district_boundary(45);
      console.log("SC districts boundary data from server:", response4.data);
      setGeojsonSouthCarolinaCongress(response4.data);

      const state_data = await fetch_state_deomographics(45);
      console.log("South Carolina demographics data:", state_data.data);
      setState(state_data.data);
      setShowDistricts(true);
    }

    setDataVisible(true);
    setDisableNavigation(true);
  };

  return (
    <div style={{ display: "flex" }}>
      <MapContainer
        center={defaultView}
        zoom={defaultZoom}
        zoomControl={false}
        style={{ height: "95vh", width: "50vw" }}
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
