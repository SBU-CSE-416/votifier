import React from 'react';
import { VictoryChart, VictoryBoxPlot, VictoryTheme } from 'victory';

const medianIncomeData = [
  { x: 'State A', y: [30000, 40000, 50000, 60000, 70000] }, // min, Q1, median, Q3, max
  { x: 'State B', y: [20000, 30000, 45000, 50000, 65000] },
  { x: 'State C', y: [35000, 40000, 50000, 70000, 80000] },
  { x: 'State D', y: [10000, 20000, 30000, 40000, 50000] },
  { x: 'State E', y: [25000, 35000, 40000, 55000, 60000] }
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
        min: { stroke: "#c43a31" },    // Style for minimum line
        max: { stroke: "#c43a31" },    // Style for maximum line
        q1: { fill: "#46ACC2" },       // Style for Q1 box
        q3: { fill: "#46ACC2" },       // Style for Q3 box
        median: { stroke: "#000" }     // Style for median line
      }}
    />
  </VictoryChart>
);

export default MedianIncomeBoxPlot;
