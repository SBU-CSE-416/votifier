import { useState } from "react";
import "../../stylesheets/map and data/leftSideMenu.css";

export default function LeftSideMenu(props) {
  let dataVisible = props.dataVisible;
  const [selectedView, setView] = useState("district");
  const [selectedHeatMap, setHeatMap] = useState("");

  const handleViewChange = (event) => {
    setView(event.target.value);
  };
  const handleHeatMapChange = (event) => {
    setHeatMap(event.target.value);
  };

  return (
    <div className="side-nav-container">
      {/* States Dropdown Menu*/}
      {dataVisible ? (
        <div className="dropdown-content">
          <label>
            <span>Select Map View:</span>
            <select
              value={selectedView}
              onChange={handleViewChange}
              className="dropdown-select"
            >
              <option value="district">District</option>
              <option value="precinct">Precinct</option>
            </select>
          </label>
          <label>
            <span>Select HeatMap:</span>
            <select
              value={selectedHeatMap}
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
