import React from 'react';
import { VictoryChart, VictoryBoxPlot, VictoryTheme } from 'victory';

const medianIncomeData = [
  { x: 'Asian', y: [30000, 40000, 50000, 60000, 70000] }, 
  { x: 'Hispanic', y: [20000, 30000, 45000, 50000, 65000] },
  { x: 'Indian', y: [35000, 40000, 50000, 70000, 80000] },
  { x: 'White', y: [10000, 20000, 30000, 40000, 50000] },
  { x: 'African American', y: [25000, 35000, 40000, 55000, 60000] }
];

const MedianIncomeBoxPlot = ({w, h, title}) => (
  <div>
    <h2 style={{ textAlign: 'center', marginBottom: '0px' }}>
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
  </VictoryChart>
  </div>
  
);

export default MedianIncomeBoxPlot;
