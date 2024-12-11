import { useContext } from "react";
import "../../stylesheets/map and data/LeftSideMenu.css";
import BackButton from "./BackButton";
import { MapStoreContext } from "../../stores/MapStore";

export default function LeftSideMenu(props) {
  const { store } = useContext(MapStoreContext);

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
  console.log("store.selectedMapView:", store.selectedMapView); 
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
              <span>Selected District Plan</span>
            </label>
          </div>
          <div className="left-dropdown">
          
          <select 
                value={store.selectedDistrictPlan}
                onChange={(event) => store.setDistrictPlan(event.target.value)}
                className="dropdown-select"
              >
                <optgroup label="Official Plans">
                  <option value="2022 Congressional Districts">2022 Official Plan</option>
                </optgroup>
                <optgroup label="Ensemble A">
                  <option value="ensembleA_plan1">Plan 1</option>
                  <option value="ensembleA_plan2">Plan 2</option>
                  <option value="ensembleA_plan3">Plan 3</option>
                </optgroup>
                <optgroup label="Ensemble B">
                  <option value="ensembleA_plan1">Plan 1</option>
                  <option value="ensembleA_plan2">Plan 2</option>
                  <option value="ensembleA_plan3">Plan 3</option>
                </optgroup>
              </select>
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
            >
              <option value="districts">Districts</option>
              <option value="precincts">Precincts</option>
            </select>
          </div>
          {store.selectedMapView === "precincts" ? 
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
                >
                  <option value="none">None</option>
                  <option value="demographic">Demographic</option>
                  <option value="economicIncome">Economic/Income</option>
                  <option value="economicRegions">Economic/Regions</option>
                  <option value="economicPoverty">Economic/Poverty</option>
                  <option value="politicalIncome">Political/Income</option>
                </select>
              </div>
            </>
          : null}
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
                >
                  <option value="WHITE">White</option>
                  <option value="BLACK">Black/African American</option>
                  <option value="ASIAN">Asian</option>
                  <option value="HISPANIC">Hispanic/Latino</option>
                </select>
              </div>
            </>
          : null}

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
