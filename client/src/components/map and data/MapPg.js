import React, { useEffect, useState, useContext } from "react";
import { MapContainer, TileLayer, GeoJSON, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import DataPg from "./DataPg";
import "../../stylesheets/map and data/Map.css";
import "../../stylesheets/BackButton.css";
import axios from "axios";
import LeftSideMenu from "./LeftSideMenu";
import { MapStoreContext } from "../../stores/MapStore";
import { stateCodeMapping } from "../../utilities/FederalInfomationProcessingStandardEnumUtil";
import { use } from "react";

function MapResizer({store }) {
  const map = useMap();

  useEffect(() => {
    setTimeout(() => {
      map.invalidateSize();
    }, 0);
  }, [store.isDataVisible, map]);

  return null;
}
export default function MapPg() {
  const { store } = useContext(MapStoreContext);

  const [stateSummaryData, setStateSummaryData] = useState(null);
  const [hoverState, setHoverState] = useState({ districtName: "" });

  const [geojsonMaryland, setGeojsonMaryland] = useState(null);
  const [geojsonSouthCarolina, setGeojsonSouthCarolina] = useState(null);
  const [geojsonMarylandCongress, setGeojsonMarylandCongress] = useState(null);
  const [geojsonSouthCarolinaCongress, setGeojsonSouthCarolinaCongress] =
    useState(null);

  const [geojsonMarylandPrecinct, setGeojsonMarylandPrecinct] = useState(null);
  const [geojsonSouthCarolinaPrecinct, setGeojsonSouthCarolinaPrecinct] =
    useState(null);

  const [disableNavigation, setDisableNavigation] = useState(false);

  //WIP STATES
  const [heatmapData, setHeatmapData] = useState(null);
  const [selectedDistrict, setSelectedDistrict] = useState(null);
  
  const defaultView = [37.7, -94.7];
  const defaultZoom = 4.5;

  useEffect(() => {
    console.log("Data visibility updated:", store.isDataVisible);
  }, [store.isDataVisible]);

  useEffect(() => {
    console.log("Selected View Updated:", store.selectedMapView);
    const handle_precincts_view = async () => {
      if (store.selectedMapView === "precincts") {
        console.log("inside precinct view handler");
        var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        if (stateAbbreviation === "MD") {
          const mdPrecinctDataRes = await fetchPrecinctBoundary(stateAbbreviation);
          console.log("precinct, MD data:", mdPrecinctDataRes.data);
          setGeojsonMarylandPrecinct(mdPrecinctDataRes.data);
          console.log("MD precinct boundary data from server:", mdPrecinctDataRes.data);
        } else if (stateAbbreviation === "SC") {
          const scPrecinctDataRes = await fetchPrecinctBoundary(stateAbbreviation);
          console.log("precinct, SC data:", scPrecinctDataRes);
          setGeojsonSouthCarolinaPrecinct(scPrecinctDataRes.data);
          console.log("SC precinct boundary data from server:", scPrecinctDataRes.data);
        }
      }
  
      setDisableNavigation(true);
    };

    handle_precincts_view();
  }, [store.selectedMapView]);

  useEffect(() => {
  },[store.selectedDistrict]);

  useEffect(() => {
    async function fetchData() {
      try {
        const response1 = await axios.get("http://localhost:8000/api/map/SC/boundary/state");

        setGeojsonSouthCarolina(response1.data);
        console.log("SC boundary data from server:", response1.data);

        const response2 = await axios.get("http://localhost:8000/api/map/MD/boundary/state");

        setGeojsonMaryland(response2.data);
        console.log("MD boundary data from server:", response2.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    }

    fetchData();
  }, []);

  const fetch_demographic_heatmap = async (stateAbbreviation, racialGroup) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/map/${stateAbbreviation}/heatmap/demographic/${racialGroup}`
      );
      const data = res.data;
      console.log("demographic heatmap data:", data);
      return data;
    } catch (error) {
      console.error("Error fetching demographic heatmap data:", error);
      return null;
    }
  };

  const fetch_economicIncome_heatmap = async (stateAbbreviation) => {
    try{
      const res = await axios.get(
        `http://localhost:8000/api/map/${stateAbbreviation}/heatmap/economic-income`
      );
      const data = res.data;
      console.log("economicIncome heatmap data:", data);
      return data;
    } catch (error) {
      console.error("Error fetching economicIncome heatmap data:", error);
      return null;
    }
  };

  const fetch_economicRegions_heatmap = async (stateAbbreviation) => {
    try{
      const res = await axios.get(
        `http://localhost:8000/api/map/${stateAbbreviation}/heatmap/regions`
      );
      const data = res.data;
      console.log("economicRegions heatmap data:", data);
      return data;
    } catch (error) {
      console.error("Error fetching economicRegions heatmap data:", error);
      return null;
    }
  };

  const fetch_economicPoverty_heatmap = async (stateAbbreviation) => {
    try{
      const res = await axios.get(
        `http://localhost:8000/api/map/${stateAbbreviation}/heatmap/economic-poverty`
      );
      const data = res.data;
      console.log("economicPoverty heatmap data:", data);
      return data;
    } catch (error) { 
      console.error("Error fetching economicPoverty heatmap data:", error);
      return null;
    }
  };

  const fetch_economicPolitical_heatmap = async (stateAbbreviation) => {
    try{
      const res = await axios.get(
        `http://localhost:8000/api/map/${stateAbbreviation}/heatmap/economic-political`
      );
      const data = res.data;
      console.log("economicPolitical heatmap data:", data);
      return data;
    } catch (error) {
      console.error("Error fetching economicPolitical heatmap data:", error);
      return null;
    }
  };


  useEffect(() => { 
    console.log("handleHeatMapChange:", store.selectedHeatmap);
    if(store.selectedMapView !== "precincts"){
      return;
    }

    const handleHeatmapChange = async () => {
      const stateAbbreviation = stateCodeMapping[store.selectedStateCode];
      console.log("in heatmap, stateAbbreviation: ", stateAbbreviation);
      var heatmapData = null;
      if( store.selectedHeatmap === "none"){
        console.log("none heatmap handler");
        return;
      }
      else if(store.selectedHeatmap === "demographic"){
        console.log("demographic heatmap HANDLER:", stateAbbreviation, store.selectedDemographic);
        const demographicGroup = store.selectedDemographic;
        heatmapData = await fetch_demographic_heatmap(stateAbbreviation, demographicGroup);
      }else if(store.selectedHeatmap === "economicIncome:"){
        console.log("economicPoverty heatmap HANDLER:", stateAbbreviation);
        heatmapData = await fetch_economicIncome_heatmap(stateAbbreviation);
      }else if(store.selectedHeatmap === "economicRegions"){
        console.log("economicRegions heatmap HANDLER:", stateAbbreviation);
        heatmapData = await fetch_economicRegions_heatmap(stateAbbreviation);
      }else if(store.selectedHeatmap === "economicPoverty"){
        console.log("economicPoverty heatmap HANDLER:", stateAbbreviation);
        heatmapData = await fetch_economicPoverty_heatmap(stateAbbreviation);
      }else if(store.selectedHeatmap === "economicPolitical"){
        console.log("economicPolitical heatmap HANDLER:", stateAbbreviation);
        heatmapData = await fetch_economicPolitical_heatmap(stateAbbreviation);
      }
  
      console.log("heatmapData: ", heatmapData);
      setHeatmapData(heatmapData);
    };
    handleHeatmapChange();
  }, [store.selectedHeatmap, store.selectedMapView, store.selectedDemographic, store.selectedStateCode]);

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
        button.style.display = "none"
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
    featureType,
  }) => {
    console.log("inside FeatureInteraction for feature:",store.selectedMapView);
    const map = useMap();
    if(store.isDataVisible===true && store.selectedMapView==="districts" && geojsonMarylandCongress){
      geojsonData = geojsonMarylandCongress;
      featureType = "district";
    }
    else if(store.isDataVisible===true && store.selectedMapView==="districts" && geojsonSouthCarolinaCongress){
      geojsonData = geojsonSouthCarolinaCongress;
      featureType = "district";
    }
    else if(store.isDataVisible===true && store.selectedMapView==="precincts" && geojsonMarylandPrecinct){
      geojsonData = geojsonMarylandPrecinct;
      featureType = "precinct";
    }
    else if(store.isDataVisible===true && store.selectedMapView==="precincts" && geojsonSouthCarolinaPrecinct){
      geojsonData = geojsonSouthCarolinaPrecinct;
      featureType = "precinct";
    }
    console.log("current geojson: ",geojsonData )
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
    }, [map]);
    console.log("featureType: ", featureType);
    const geojsonStyle = {
      fillColor: featureType === "district" ? "#FF5733" : featureType === "precinct" ? "#FF5733" :  "#3388ff",
      weight: 0.5,
      opacity: 1,
      color: "#FFFFFF",
      dashArray: "",
      fillOpacity: featureType === "precinct" ? 0.5 : 0.7,
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
      map.fitBounds(bounds, {
        maxZoom:10, 
      });
      console.log("FEATURE CONTENTS:", feature);
      if(feature.properties.NAME === "South Carolina" || feature.properties.NAME === "Maryland"){
        store.setMapView("districts");
      }
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
  };

  const fetchPrecinctBoundary = async (state_abbreviation) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/map/${state_abbreviation}/boundary/precincts`
      );
      
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };
  const fetchDistrictBoundary = async (state_abbreviation) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/map/${state_abbreviation}/boundary/districts`
      );
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };
  const fetchStateSummary = async (state_abbreviation) => {
    try {
      const res = await axios.get(
        `http://localhost:8000/api/data/${state_abbreviation}/summary`
      );
      return res;
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  function MapResizer({store }) {
    const map = useMap();
  
    useEffect(() => {
      setTimeout(() => {
        map.invalidateSize();
      }, 0);
    }, [store.isDataVisible, map]);
  
    return null;
  }

  const handleResetView = (map) => {
    map.setView(defaultView, defaultZoom);
    setHoverState({ districtName: "" });
    store.setDataVisibility(false);
    store.setMapView("states");
    setDisableNavigation(false);
    store.setSelectedStateCode(null);
    store.setSelectedDistrict(null);
    store.setSelectedHeatmap("none");
  };
  const onFeatureClick = async (feature) => {
    const properties = feature.properties;
    console.log("inside onFeatureClick");
    console.log("initial store.selectedMapView in onFeatureClick: ", store.selectedMapView);

    if (store.selectedMapView === "districts" || store.selectedMapView === "states") {
      if (properties.NAME === "Maryland") {
        const md_district_res = await fetchDistrictBoundary("MD");
        console.log(
          "MD districts boundary data from server:",
          md_district_res.data
        );
        setGeojsonMarylandCongress(md_district_res.data);

        const state_summary_data = await fetchStateSummary("MD");
        console.log("Maryland demographics data:", state_summary_data.data);
        store.setSelectedStateCode(24);
        console.log("store.selectedStateCode updated: ", store.selectedStateCode);
        setStateSummaryData(state_summary_data.data);
      } 
      else if (properties.NAME === "South Carolina") {
        const sc_district_res = await fetchDistrictBoundary("SC");
        console.log(
          "SC districts boundary data from server:",
          sc_district_res.data
        );
        setGeojsonSouthCarolinaCongress(sc_district_res.data);

        const state_summary_data = await fetchStateSummary("SC");
        console.log("South Carolina demographics data:", state_summary_data.data);
        store.setSelectedStateCode(45);
        console.log("store.selectedStateCode updated: ", store.selectedStateCode);
        setStateSummaryData(state_summary_data.data);
      }
    }else
    if (store.selectedMapView === "precincts") {
      //NOTE THIS IS DEAD CODE, NEVER ACCESSED.
      console.log("inside onFeatureClick precinct");
      console.log("properties.NAME: ", properties.NAME);
      if (properties.NAME === "Maryland") {
        const mdPrecinctDataRes = await fetchPrecinctBoundary("MD");
        console.log("precinct, MD data:", mdPrecinctDataRes.data);
        setGeojsonMarylandPrecinct(mdPrecinctDataRes.data);
        console.log("MD precinct boundary data from server:", mdPrecinctDataRes.data);
      } else if (properties.NAME === "South Carolina") {
        const scPrecinctDataRes = await fetchPrecinctBoundary("SC");
        console.log("precinct, SC data:", scPrecinctDataRes);
        setGeojsonSouthCarolinaPrecinct(scPrecinctDataRes.data);
        console.log("SC precinct boundary data from server:", scPrecinctDataRes.data);
      }
    }

    store.setDataVisibility(true);
    setDisableNavigation(true);
  };
  return (
    <div style={{ display: "flex" }}>
      {
        <LeftSideMenu
          handleResetView={handleResetView}
        />
      }

      <div
        style={{
          position: "relative",
          width: store.isDataVisible ? "39vw" : "86vw",
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
          <MapResizer store={store} />
          <TileLayer
            url="https://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}{r}.png"
            attribution="&copy; <a href='https://carto.com/'>CartoDB</a>"
          />

          {/* State Boundaries */}
          {store.isDataVisible===false && store.selectedMapView==="states" && (
            <FeatureInteraction
              geojsonData={geojsonMaryland}
              featureType="state"
            />
          )}

          {store.isDataVisible===false && store.selectedMapView==="states" && (
            <FeatureInteraction
              geojsonData={geojsonSouthCarolina}
              featureType="state"
            />
          )}

          {/* District Boundaries */}
          {store.isDataVisible===true && store.selectedMapView==="districts" && geojsonMarylandCongress && (
            <FeatureInteraction
              geojsonData={geojsonMarylandCongress}
              featureType="district"
            />
          )}

          {store.isDataVisible===true && store.selectedMapView==="districts" && geojsonSouthCarolinaCongress && (
            <FeatureInteraction
              geojsonData={geojsonSouthCarolinaCongress}
              featureType="district"
            />
          )}

          {console.log("store.selectedMapView, precinct's geoJson: ", store.selectedMapView, geojsonMarylandPrecinct, geojsonSouthCarolinaPrecinct)  }
          {/* Precinct Boundaries */}
          {store.isDataVisible===true && store.selectedMapView==="precincts" && geojsonSouthCarolinaPrecinct && (
            <FeatureInteraction
              geojsonData={geojsonSouthCarolinaPrecinct}
              featureType="precinct"
            />
          )}

          {store.isDataVisible===true && store.selectedMapView==="precincts" && geojsonMarylandPrecinct && (
            <FeatureInteraction
              geojsonData={geojsonMarylandPrecinct}
              featureType="precinct"
            />
          )}

          <BackButtonControl resetView={handleResetView} />
        </MapContainer>
      </div>
      {store.isDataVisible===true && <DataPg stateSummaryData={stateSummaryData} />}
    </div>
  );
}
