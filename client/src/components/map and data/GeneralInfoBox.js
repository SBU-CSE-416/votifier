import React from "react";
import "../../stylesheets/map and data/GeneralInfoBox.css";

const GeneralInfoBox = ({ state }) => {
  const filteredStateData = Object.entries(state).filter(
    ([key]) => key !== "hous_INCOME_DIS" && key !== "id"
  );

  const maxColumnsPerTable = 8;
  const splitData = [];
  for (let i = 0; i < filteredStateData.length; i += maxColumnsPerTable) {
    splitData.push(filteredStateData.slice(i, i + maxColumnsPerTable));
  }

  return (
    <div className="info-table-container">
      {splitData.map((chunk, index) => (
        <table key={index} className="info-table">
          <thead>
            <tr>
              {chunk.map(([key], colIndex) => (
                <th key={colIndex} className="info-title">
                  {key.replaceAll("_", " ")}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            <tr>
              {chunk.map(([_, value], colIndex) => (
                <td key={colIndex} className="info-value">
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
      ))}
    </div>
  );
};

export default GeneralInfoBox;
