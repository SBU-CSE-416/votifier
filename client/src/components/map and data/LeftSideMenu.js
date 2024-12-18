import { useContext, useEffect } from "react";
import "../../stylesheets/map and data/LeftSideMenu.css";
import BackButton from "./BackButton";
import { MapStoreContext } from "../../stores/MapStore";
import HeatMapLegend from "../HeatMapLegend";

export default function LeftSideMenu(props) {
  const { store } = useContext(MapStoreContext);

  useEffect(() => {
    if (store.selectedMapView === "precincts"){
      store.setFirstTabView = "summary";
      store.setSelectedDistrict(null);
    }
  }, [store.selectedMapView]);

  const checkSelectedState = () => {
    let stateName = "";
    if (store.selectedStateCode) {
      if (store.selectedStateCode === 45) {
        stateName = "South Carolina";
      } 
      else if (store.selectedStateCode === 24) {
        stateName = "Maryland";
      }
      return (
        <p className="side-nav-state-text">{stateName}</p>
      );
    }
  };
  return (
    <div className="side-nav-container">
      {/* States Dropdown Menu*/}
      {store.isDataVisible ? (
        <div className="dropdown-content">
          <div className="left-container">
          <BackButton handleResetView={props.handleResetView}> </BackButton>
          </div>
          <div className="left-container">
            {checkSelectedState()}
          </div>
          <div className="left-label">
            <label>
              <span>Selected Map View</span>
            </label>
          </div>
          <div className="left-dropdown">
            <select
              value={store.selectedMapView}
              onChange={(event) => store.setMapView(event.target.value)}
              className="dropdown-select"
              style={{width: '80%'}}
            >
              <option value="districts">Districts</option>
              <option value="precincts">Precincts</option>
            </select>
          </div>
          {store.selectedMapView === "precincts" && 
            <>
              <div className="left-label">
                <label>
                  <span>Selected Heatmap</span>
                </label>
              </div>
              <div className="left-dropdown">
                <select
                  value={store.selectedHeatmap}
                  onChange={(event) => store.setSelectedHeatmap(event.target.value)}
                  className="dropdown-select"
                  style={{width: '80%'}}
                >
                  <option value="none">None</option>
                  <option value="demographic">Demographic</option>
                  <option value="economicIncome">Economic/Income</option>
                  <option value="economicRegions">Economic/Regions</option>
                  <option value="economicPoverty">Economic/Poverty</option>
                  <option value="economicPolitical">Economic/Political</option>
                </select>
              </div>
            </>
          }
          {(store.selectedHeatmap === "demographic" && store.selectedMapView==="precincts") ? 
            <>
              <div className="left-label">
                <label>
                  <span>Selected Racial Group for Heatmap</span>
                </label>
              </div>
              <div className="left-dropdown">
                <select
                  value={store.selectedDemographic}
                  onChange={(event) => store.setSelectedDemographic(event.target.value)}
                  className="dropdown-select"
                  style={{width: '80%'}}
                >
                  <option value="WHITE">White</option>
                  <option value="BLACK">Black/African American</option>
                  <option value="ASIAN">Asian</option>
                  <option value="HISPANIC">Hispanic/Latino</option>
                </select>
              </div>
            </>
          : null}

          {(store.selectedMapView==="precincts" && store.selectedHeatmap !== "none") &&
            <HeatMapLegend type={store.selectedHeatmap} />
          }

        </div>
      ) : (
        <div className="dropdown-content">
          <p className="side-nav-state-text">Select a State</p>
          <p className="guide-text">Click on a state on the map to view detailed information.</p>
        </div>
      )}
    </div>
  );
}
