import React from 'react';
import "../../stylesheets/map and data/GeneralInfoBox.css"

const InfoBoxFrame = ({ title, value, subtitle }) => {
  return (
    <div className="info-box">
      <div className="info-title">{title}</div>
      <div className="info-value">{value}</div>
      <div className="info-subtitle">{subtitle}</div>
    </div>
  );
};

const GeneralInfoBox = ({ state }) => {
  // array of state boxes
  const stateBoxes = Object.values(state);

  return (
    <div className="info-box-grid">
      {stateBoxes.map((box, index) => (
        <InfoBoxFrame 
          key={index}
          title={box.title} 
          value={box.value} 
        />
      ))}
    </div>
  );
};

export default GeneralInfoBox;
