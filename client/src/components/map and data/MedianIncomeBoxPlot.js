import React from 'react';
import { VictoryChart, VictoryBoxPlot, VictoryTheme } from 'victory';

const medianIncomeData = [
  { x: 'District A', y: [30000, 40000, 50000, 60000, 70000] }, 
  { x: 'District B', y: [20000, 30000, 45000, 50000, 65000] },
  { x: 'District C', y: [35000, 40000, 50000, 70000, 80000] },
  { x: 'District D', y: [10000, 20000, 30000, 40000, 50000] },
  { x: 'District E', y: [25000, 35000, 40000, 55000, 60000] }
];

const MedianIncomeBoxPlot = () => (
  <VictoryChart
    theme={VictoryTheme.material}
    domainPadding={20}
  >
    <VictoryBoxPlot
      data={medianIncomeData}
      x="x"
      y="y"
      boxWidth={20}
      style={{
        min: { stroke: "#c43a31" },   
        max: { stroke: "#c43a31" },
        q1: { fill: "#46ACC2" },       
        q3: { fill: "#46ACC2" },      
        median: { stroke: "#000" }     
      }}
    />
  </VictoryChart>
);

export default MedianIncomeBoxPlot;
