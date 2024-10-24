import React from 'react';
import '../../stylesheets/map and data/PlaceholderMessage.css';

const PlaceholderMessage = () => {
  return (
    <div className="placeholder-container">
      <div>
        <h2 style={{"fontSize":"34px"}}>No details are available.</h2>
        <p style={{"fontSize":"20px"}}>Please click a highlighted state on the map to view its details.</p>
      </div>
    </div>
  );
};

export default PlaceholderMessage;
