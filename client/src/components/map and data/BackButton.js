import React from 'react';
import "../../stylesheets/BackButton.css"

const BackButton = () => {
    const handleClick = () => {
        const event = new CustomEvent('reset-map-view', { detail: { } });
        window.dispatchEvent(event); 
    };
  
    return (
      <button className="back-button" onClick={handleClick}>
        &lt; Go Back
      </button>
    );
  };

export default BackButton;