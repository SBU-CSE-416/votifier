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
const GeneralInfoBox = ({state}) => {
  console.log("Info State Data", state)
  return (
    <div className="info-box-grid">
      <InfoBoxFrame 
        title={`Name`} 
        value={state.districtName}
      />
      <InfoBoxFrame 
        title="Total Population" 
        value={state.population} 
      />
      <InfoBoxFrame 
        title="Median Household Income" 
        value={state.income} 
      />
      <InfoBoxFrame 
        title="Homeownership Rate" 
        value={state.homeownershipRate} 
      />
      <InfoBoxFrame 
        title="Unemployment Rate" 
        value={state.unemploymentRate} 
      />
      <InfoBoxFrame 
        title="Poverty Rate" 
        value={state.povertyRate} 
      />
    </div>
  );
  
};

export default GeneralInfoBox;
