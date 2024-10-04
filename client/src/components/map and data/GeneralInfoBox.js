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
const GeneralInfoBox = () => {
  return (
    <div className="info-box-grid">
      <InfoBoxFrame title="Total Population" value="5.37 M" subtitle="+1.7% from 2022" />
      <InfoBoxFrame title="Median Household Income" value="$54,864" subtitle="+2% from last year" />
      <InfoBoxFrame title="Homeownership Rate" value="69.5%" subtitle="+0.5% from last year" />
      <InfoBoxFrame title="Unemployment Rate" value="3.5%" subtitle="-0.2% from last year" />
      <InfoBoxFrame title="Poverty Rate" value="13.2%" subtitle="-0.3% from last year" />
      <InfoBoxFrame title="Education Attainment" value="28.2%" subtitle="+1% from last year" />
    </div>
  );
};

export default GeneralInfoBox;
