import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts';

const data = [
  { bin: '0-10', population: 30000 },
  { bin: '11-20', population: 40000 },
  { bin: '21-30', population: 100000 },
  { bin: '31-40', population: 150000 },
  { bin: '41-50', population: 120000 },
  { bin: '51-60', population: 70000 },
  { bin: '61-70', population: 60000 },
  { bin: '71-80', population: 30000 },
  { bin: '80+', population: 2000 }
];

const HistogramChart = ({w, h, title}) => (
  <div>
    <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>
      {title || "Title Needed"}
    </h2>
      <BarChart width={w} height={h} data={data} barCategoryGap="0%">
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="bin" label={{ value: "Age Ranges", position: 'insideBottom', offset: -5 }} />
      <YAxis label={{ angle: -90, position: 'insideLeft', offset: -7}} />
      <Tooltip />
      <Bar dataKey="population" fill="#46ACC2" />
    </BarChart>
  </div>
  
);

export default HistogramChart;
