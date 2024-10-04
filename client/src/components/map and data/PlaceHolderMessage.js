import React from 'react';
import '../../stylesheets/map and data/PlaceholderMessage.css';

const PlaceholderMessage = () => {
  return (
    <div className="placeholder-container">
      <div>
        <h2>Please Click on a State</h2>
        <p>Select a state from the map to view its details.</p>
      </div>
    </div>
  );
};

export default PlaceholderMessage;
