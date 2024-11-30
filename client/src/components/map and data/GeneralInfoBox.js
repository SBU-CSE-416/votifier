import React from "react";
import "../../stylesheets/map and data/GeneralInfoBox.css";

const GeneralInfoBox = ({ state }) => {
  // Filter out the `HOUS_INCOME_DIS` key 
  const filteredStateData = Object.entries(state).filter(
    ([key]) => key !== "HOUS_INCOME_DIS" && key !== "id"
  );

  return (
    <div className="info-table-container">
      <table className="info-table">
        <thead>
          <tr>
            {filteredStateData.map(([key], index) => (
              <th key={index} className="info-title">
                {key.replaceAll("_", " ")}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          <tr>
            {filteredStateData.map(([_, value], index) => (
              <td key={index} className="info-value">
                {typeof value === "number"
                  ? value.toLocaleString() // Format numbers with commas
                  : typeof value === "object"
                  ? JSON.stringify(value) // Convert objects to strings
                  : value}
              </td>
            ))}
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default GeneralInfoBox;
