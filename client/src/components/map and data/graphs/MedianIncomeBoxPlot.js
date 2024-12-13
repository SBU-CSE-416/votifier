import React from 'react';
import { VictoryChart, VictoryBoxPlot, VictoryTheme } from 'victory';

const MedianIncomeBoxPlot = ({w, h, title}) => (
  <div>
    {/* <h2 style={{ textAlign: 'center', marginBottom: '0px' }}>
      {title || "Median Income Distribution"}
    </h2>
    <VictoryChart
    theme={VictoryTheme.material}
    domainPadding={20}
    width={w}  
    height={h} 
  >
    <VictoryBoxPlot
      data={medianIncomeData}
      x="x"
      y="y"
      boxWidth={10}
      style={{
        min: { stroke: "#c43a31" },   
        max: { stroke: "#c43a31" },
        q1: { fill: "#46ACC2" },       
        q3: { fill: "#46ACC2" },      
        median: { stroke: "#000" }     
      }}
    />
  </VictoryChart> */}
  </div>
  
);

export default MedianIncomeBoxPlot;
