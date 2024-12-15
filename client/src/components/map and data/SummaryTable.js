import React from "react";
import "../../stylesheets/map and data/SummaryTable.css";
import { formatVariable } from "../../utilities/ReformatVariableNamesUtil";
const SummaryTable = ({ stateSummaryData }) => {
  const filteredStateData = Object.entries(stateSummaryData).filter(
    ([key]) => 
      key !== "house_HOLD_INCOME_DISTRIBUTION" && 
      key !== "id" &&
      key !== "poverty_LEVEL" &&
      key !== "native_AMERICAN_PERCENT" &&
      key !== "islander_PERCENT"

  );
  console.log("filteredStateData: ", filteredStateData);
  const maxColumnsPerTable = 8;
  const splitData = [];
  for (let i = 0; i < filteredStateData.length; i += maxColumnsPerTable) {
    splitData.push(filteredStateData.slice(i, i + maxColumnsPerTable));
  }

  return (
    <div className="summary-table-container">
      {splitData.map((chunk, index) => (
        <table key={index} className="summary-table">
          <thead>
            <tr>
              {chunk.map(([key], colIndex) => (
                <th key={colIndex} className="summary-title">
                  {formatVariable(key)}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            <tr>
              {chunk.map(([key, value], colIndex) => (
                <td key={colIndex} className="summary-value">
                  {typeof value === "number"
                    ? key.includes("PERCENT")
                      ? `${value.toLocaleString()}%`
                      : value.toLocaleString()
                    : typeof value === "object"
                    ? JSON.stringify(value)
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

export default SummaryTable;
