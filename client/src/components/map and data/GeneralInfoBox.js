import React from 'react';
import "../../stylesheets/map and data/GeneralInfoBox.css";

const GeneralInfoBox = ({ state }) => {
  const stateBoxes = Object.values(state);
  return (
    <div className="info-table-container">
      <table className="info-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Value</th>
          </tr>
        </thead>
        <tbody>
          {stateBoxes.map((box, index) => (
            <tr key={index}>
              <td>{box.title}</td>
              <td>{box.value}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default GeneralInfoBox;
