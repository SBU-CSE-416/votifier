import { useState } from "react";
import "../../stylesheets/map and data/leftSideMenu.css";

export default function LeftSideMenu(props) {
  let dataVisible = props.dataVisible;

  const handleViewChange = (event) => {
    props.setView(event.target.value);
  };
  const handleHeatMapChange = (event) => {
    props.setHeatMap(event.target.value);
  };
  const checkSelectedState = () => {
    let stateName = "";
    if (props.selectedStateCode) {
      if (props.selectedStateCode === 45) {
        stateName = "South Carolina";
      } else if (props.selectedStateCode === 24) {
        stateName = "Maryland";
      }
      return (
        <div>
          <p>{stateName}</p>
        </div>
      );
    }
  };

  return (
    <div className="side-nav-container">
      {/* States Dropdown Menu*/}
      {dataVisible ? (
        <div className="dropdown-content">
          {checkSelectedState()}
          <label>
            <span>Select Map View:</span>
            <select
              value={props.selectedView}
              onChange={handleViewChange}
              className="dropdown-select"
            >
              <option value="district">District</option>
              <option value="precinct">Precinct</option>
            </select>
          </label>
          <label>
            <span>Select Heat Map:</span>
            <select
              value={props.selectedHeatMap}
              onChange={handleHeatMapChange}
              className="dropdown-select"
            >
              <option value="demographic">Demographic</option>
              <option value="economic">Economic</option>
              <option value="poverty">Poverty</option>
              <option value="politicalIncome">Political/Income</option>
            </select>
          </label>
        </div>
      ) : (
        <div>
          <p>Click on a state on the map to view information.</p>
        </div>
      )}
    </div>
  );
}
