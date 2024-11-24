import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, GeoJSON, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import DataPg from "./DataPg";
import "../../stylesheets/map and data/map.css";
import "../../stylesheets/BackButton.css";
import axios from "axios";
import LeftSideMenu from "./LeftSideMenu";

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

export default function MapPg() {
  const [state, setState] = useState(initialState);
  const [hoverState, setHoverState] = useState({ districtName: "" });
  const [dataVisible, setDataVisible] = useState(false);
  const [showDistricts, setShowDistricts] = useState(false);
  const [showPrecincts, setShowPrecincts] = useState(false);

  //LeftSideMenu selectors
  const [selectedView, setView] = useState("");
  const [selectedPlan, setPlan] = useState("");
  const [selectedHeatmap, setHeatmap] = useState("");
  const [selectedStateCode, setStateCode] = useState(null);

  const [geojsonMaryland, setGeojsonMaryland] = useState(null);
  const [geojsonSouthCarolina, setGeojsonSouthCarolina] = useState(null);
  const [geojsonMarylandCongress, setGeojsonMarylandCongress] = useState(null);
  const [geojsonSouthCarolinaCongress, setGeojsonSouthCarolinaCongress] =
    useState(null);

  const [geojsonMarylandPrecinct, setGeojsonMarylandPrecinct] = useState(null);
  const [geojsonSouthCarolinaPrecinct, setGeojsonSouthCarolinaPrecinct] =
    useState(null);

  const [disableNavigation, setDisableNavigation] = useState(false);

  const defaultView = [37.7, -94.7];
  const defaultZoom = 4.5;

  useEffect(() => {
    async function fetchData() {
      try {
        const response1 = await axios.get("http://localhost:8000/api/map/45/state");

        setGeojsonSouthCarolina(response1.data);
        console.log("SC boundary data from server:", response1.data);

        const response2 = await axios.get("http://localhost:8000/api/map/24/state");

        setGeojsonMaryland(response2.data);
        console.log("MD boundary data from server:", response2.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchData();
  }, []);

  useEffect(() => {
    if (selectedView === "district") {
      setShowDistricts(true);
      setShowPrecincts(false);
    } 
    else if (selectedView === "precincts") {
      setShowDistricts(false);
      setShowPrecincts(true);
    }
  }, [selectedView]);

  useEffect(() => {}, [hoverState]);

  function BackButtonControl({ resetView }) {
    const map = useMap()

    useEffect(() => {
      const backButton = L.control({ position: "topright" });
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

        const handleResetView = () => {
          // Handle map reset
          resetView(map);
        };
    
        window.addEventListener('reset-map-view', handleResetView);

        //calls the reset map view function
        button.onclick = () => {
          resetView(map);
        };
        return button;
      };

      //adds the buttons to the map
      backButton.addTo(map);

      return () => {
        //remove the button from the map or else the button will be created over and over again
        map.removeControl(backButton);
        window.removeEventListener('reset-map-view', handleResetView);
      };
    }, [map, resetView]);

    return null;
  }

  //controls the feature
  const FeatureInteraction = ({
    geojsonData,
    onFeatureClick,
    disableNavigation,
    featureType,
  }) => {
    const map = useMap();

    useEffect(() => {
      if (disableNavigation) {
        map.dragging.disable();
        map.scrollWheelZoom.disable();
        map.doubleClickZoom.disable();
        map.touchZoom.disable();
        map.boxZoom.disable();
      } 
      else {
        map.dragging.enable();
        map.scrollWheelZoom.enable();
        map.doubleClickZoom.enable();
        map.touchZoom.enable();
        map.boxZoom.enable();
      }
    }, [disableNavigation, map]);

    const geojsonStyle = {
      fillColor: featureType === "district" ? "#FF5733" : featureType === "precinct" ? "#FF5733" :  "#3388ff",
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
      layer.bringToFront();
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

    // if (featureType === "district" || featureType === "state") {
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
    // }
    // else if (featureType === "precinct") {
    //   return(
    //     <>
    //       {geojsonData && (
    //         <GeoJSON
    //           data={geojsonData}
    //           style={geojsonStyle}
    //           onEachFeature={(feature, layer) => {
    //             const properties = feature.properties;

    //             layer.unbindTooltip();
    //             layer.bindTooltip(
    //               `${properties.NAME || "Precinct " + properties.PRECINCT}`,
    //               {
    //                 permanent: false,
    //                 direction: "auto",
    //                 sticky: true,
    //               }
    //             );

    //             layer.on("mouseover", () => {
    //               highlightFeature(layer);
    //               layer.openTooltip();
    //             });

    //             layer.on("mouseout", () => {
    //               resetHighlight(layer);
    //               layer.closeTooltip();
    //             });

    //             layer.on("click", () => handleFeatureClick(feature, layer));
    //           }}
    //         />
    //       )}
    //     </>
    //   );
    // }
  };

  const fetch_precinct_boundary = async (fips_code) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/map/${fips_code}/precincts`
      );
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };
  const fetch_district_boundary = async (fips_code) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/map/${fips_code}/districts`
      );
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };
  const fetch_state_demographics = async (fips_code) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/charts/${fips_code}/summary`
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
    setShowPrecincts(false);
    setStateCode(null);
  };
  const onFeatureClick = async (feature) => {
    const properties = feature.properties;
    console.log("inside onFeatureClick");

    if (properties.NAME === "Maryland") {
      const md_district_res = await fetch_district_boundary(24);
      console.log(
        "MD districts boundary data from server:",
        md_district_res.data
      );
      setGeojsonMarylandCongress(md_district_res.data);

      const state_data = await fetch_state_demographics(24);
      console.log("Maryland demographics data:", state_data.data);
      setStateCode(24);
      setState(state_data.data);
      setShowDistricts(true);
    } 
    else if (properties.NAME === "South Carolina") {
      const sc_district_res = await fetch_district_boundary(45);
      console.log(
        "SC districts boundary data from server:",
        sc_district_res.data
      );
      setGeojsonSouthCarolinaCongress(sc_district_res.data);

      const state_data = await fetch_state_demographics(45);
      console.log("South Carolina demographics data:", state_data.data);
      setStateCode(45);
      setState(state_data.data);
      setShowDistricts(true);
    }

    if (selectedView === "precincts") {
      console.log("inside onFeatureClick precinct");
      if (properties.NAME === "Maryland") {
        const mdPrecinctDataRes = await fetch_precinct_boundary(24);
        console.log("precinct, MD data:", mdPrecinctDataRes.data);
        setGeojsonMarylandPrecinct(mdPrecinctDataRes.data);
        setShowPrecincts(true);
      } else if (properties.NAME === "South Carolina") {
        const scPrecinctDataRes = await fetch_precinct_boundary(45);
        console.log("precinct, SC data:", scPrecinctDataRes.data);
        setGeojsonSouthCarolinaPrecinct(scPrecinctDataRes.data);
        setShowPrecincts(true);
      }
    }

    setDataVisible(true);
    setDisableNavigation(true);
  };

  return (
    <div style={{ display: "flex" }}>
      {
        <LeftSideMenu
          dataVisible={dataVisible}
          setHeatmap={setHeatmap}
          setPlan={setPlan}
          selectedPlan={selectedPlan}
          selectedHeatmap={selectedHeatmap}
          setView={setView}
          selectedView={selectedView}
          selectedStateCode={selectedStateCode}
          setStateCode={selectedStateCode}
        />
      }

      <div
        style={{
          position: "relative",
          width: dataVisible ? "41vw" : "90vw",
        }}
      >
        <MapContainer
          center={defaultView}
          zoom={defaultZoom}
          zoomControl={false} //Removes + - Zoom btns in top left
          style={{
            height: "95vh",
            width: "100%",
            transition: "width 0.5s ease",
          }}
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

          {/* District Boundaries */}
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

          {/* Precinct Boundaries */}
          {showPrecincts && geojsonSouthCarolinaPrecinct && (
            <FeatureInteraction
              geojsonData={geojsonSouthCarolinaPrecinct}
              onFeatureClick={onFeatureClick}
              disableNavigation={disableNavigation}
              setHoverState={setHoverState}
              setState={setState}
              featureType="precincts"
            />
          )}

          {showPrecincts && geojsonMarylandPrecinct && (
            <FeatureInteraction
              geojsonData={geojsonMarylandPrecinct}
              onFeatureClick={onFeatureClick}
              disableNavigation={disableNavigation}
              setHoverState={setHoverState}
              setState={setState}
              featureType="precincts"
            />
          )}

          <BackButtonControl resetView={handleResetView} />
        </MapContainer>
      </div>

      {dataVisible && <DataPg state={state} />}
    </div>
  );
}
