import React from 'react';
import "../../stylesheets/map and data/GeneralInfoBox.css";

const GeneralInfoBox = ({ state }) => {
  const stateBoxes = Object.values(state);
  return (
    <div className="info-table-container">
      <table className="info-table">
        <thead>
          <tr>
            {stateBoxes.map((box, index) => (
              <th key={index} className="info-title">{box.title}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          <tr>
            {stateBoxes.map((box, index) => (
              <td key={index} className="info-value">{box.value}</td>
            ))}
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default GeneralInfoBox;
