import React from "react";
import "../stylesheets/HeatMapLegend.css";

const HeatMapLegend = ({type}) => {
    const legends = {
        demographic: [
            { label: "0%-9%", color: "#FFF6F7" },
            { label: "10%-19%", color: "#FDE1E0" },
            { label: "20%-29%", color: "#FFC3BF" },
            { label: "30%-39%", color: "#FB9EB8" },
            { label: "40%-49%", color: "#F768A2" },
            { label: "50%-59%", color: "#DF3595" },
            { label: "60%-69%", color: "#B10085" },
            { label: "70%-79%", color: "#7C007A" },
            { label: "80%-89%", color: "#51006D" },
            { label: "90%-100%", color: "#1C0227" },
        ],
        economicIncome: [
            { label: "$0-$34,999", color: "#D8FFD6" },
            { label: "$35,000-$59,999", color: "#B4EAB0" },
            { label: "$60,000-$99,999", color: "#91D48A" },
            { label: "$100,000-$124,999", color: "#6DBF64" },
            { label: "$125,000-$149,999", color: "#47AA3C" },
            { label: "$150,000 or more", color: "#0A9400" },
        ],
        economicRegions: [
            { label: "Urban", color: "#2a3eb3" },
            { label: "Suburban", color: "#008f9d" },
            { label: "Rural", color: "#80d043" },
        ],
        economicPoverty: [
            { label: "0-10%", color: "#f5f4f0" },
            { label: "10-20%", color: "#FFFDE7" },
            { label: "20-30%", color: "#FFF9C4" },
            { label: "30-40%", color: "#FFF59D" },
            { label: "40-50%", color: "#FFEE58" },
            { label: "50-60%", color: "#FFEB3B" },
            { label: "60-70%", color: "#FDD835" },
            { label: "70%+", color: "#FBC02D" },
        ],
        economicPolitical: {
            Republican: [
                { label: "$0-$34,999", color: "#FFCDD2" },
                { label: "$35,000-$59,999", color: "#EF9A9A" },
                { label: "$60,000-$99,999", color: "#E57373" },
                { label: "$100,000-124,999", color: "#EF5350" },
                { label: "$125,000-$149,999", color: "#F44336" },
                { label: "$150,000 or more", color: "#D32F2F" },
            ],
            Democratic: [
                { label: "$0-$34,999", color: "#BBDEFB" },
                { label: "$35,000-$59,999", color: "#90CAF9" },
                { label: "$60,000-$99,999", color: "#64B5F6" },
                { label: "$100,000-$124,999", color: "#42A5F5" },
                { label: "$125,000-$149,999", color: "#2196F3" },
                { label: "$150,000 or more", color: "#1976D2" },
            ],
        },
    };

    const legendItems = legends[type];

    return (
        <div className="heatmap-legend">
        <h4>{type.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())} Heatmap Legend</h4>
        {Array.isArray(legendItems) ? (
          legendItems.map((item, index) => (
            <div key={index} className="legend-item">
              <span className="legend-color" style={{ backgroundColor: item.color }}></span>
              <span className="legend-label">{item.label}</span>
            </div>
          ))
        ) : (
          Object.entries(legendItems).map(([category, items], index) => (
            <div key={index}>
              <h5>{category}</h5>
              {items.map((item, subIndex) => (
                <div key={subIndex} className="legend-item">
                  <span className="legend-color" style={{ backgroundColor: item.color }}></span>
                  <span className="legend-label">{item.label}</span>
                </div>
              ))}
            </div>
          ))
        )}
      </div>
    );
};

export default HeatMapLegend;