import { useState } from "react";
import "../../stylesheets/map and data/leftSideMenu.css";
import BackButton from "./BackButton";

export default function LeftSideMenu(props) {
  let dataVisible = props.dataVisible;

  const handleViewChange = (event) => {
    props.setView(event.target.value);
  };
  const handleHeatmapChange = (event) => {
    props.setHeatmap(event.target.value);
  };
  const handlePlanChange = (event) => {
    props.setPlan(event.target.value);
  }
  const checkSelectedState = () => {
    let stateName = "";
    if (props.selectedStateCode) {
      if (props.selectedStateCode === 45) {
        stateName = "South Carolina";
      } 
      else if (props.selectedStateCode === 24) {
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
      {dataVisible ? (
        <div className="dropdown-content">
          <div className="left-container">
            <BackButton></BackButton>
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
                value={props.selectedPlan}
                onChange={handlePlanChange}
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
                value={props.selectedView}
                onChange={handleViewChange}
                className="dropdown-select"
              >
                <option value="district">Districts</option>
                <option value="precincts">Precincts</option>
              </select>
          </div>
          <div className="left-label">
            <label>
              <span>Selected Heatmap</span>
            </label>
          </div>
          <div className="left-dropdown">
            <select
                value={props.selectedHeatmap}
                onChange={handleHeatmapChange}
                className="dropdown-select"
              >
                <option value="none">None</option>
                <option value="demographic">Demographic</option>
                <option value="economic">Economic</option>
                <option value="poverty">Poverty Level</option>
                <option value="politicalIncome">Political/Income</option>
            </select>
          </div>
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
