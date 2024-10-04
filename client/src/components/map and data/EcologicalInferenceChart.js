
import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const densityData = [
  { demographic: '0.0', Indian: 0, 'East Asian': 2, 'Non-Asian': 3 },
  { demographic: '0.1', Indian: 1, 'East Asian': 5, 'Non-Asian': 7 },
  { demographic: '0.2', Indian: 3, 'East Asian': 10, 'Non-Asian': 13 },
  { demographic: '0.3', Indian: 5, 'East Asian': 18, 'Non-Asian': 20 },
  { demographic: '0.4', Indian: 7, 'East Asian': 23, 'Non-Asian': 25 },
  { demographic: '0.5', Indian: 10, 'East Asian': 28, 'Non-Asian': 27 },
  { demographic: '0.6', Indian: 6, 'East Asian': 20, 'Non-Asian': 23 },
  { demographic: '0.7', Indian: 4, 'East Asian': 15, 'Non-Asian': 18 },
  { demographic: '0.8', Indian: 2, 'East Asian': 10, 'Non-Asian': 12 },
  { demographic: '0.9', Indian: 1, 'East Asian': 5, 'Non-Asian': 7 },
  { demographic: '1.0', Indian: 0, 'East Asian': 2, 'Non-Asian': 3 },
];

const EcologicalInferenceChart = ({w,h, title}) => (
  <div>
    <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>
      {title || "Title Needed"}
    </h2>
      <ResponsiveContainer width={w} height={h}>
        <LineChart
          data={densityData}
          margin={{ top: 20, right: 30, left: 20, bottom: 20 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="demographic" label={{position: 'insideBottom', offset: -5 }} />
          <YAxis label={{ angle: -90, position: 'insideLeft', offset: -5 }} />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="Indian" stroke="#82ca9d" strokeWidth={2} />
          <Line type="monotone" dataKey="East Asian" stroke="#8884d8" strokeWidth={2} />
          <Line type="monotone" dataKey="Non-Asian" stroke="#ffc658" strokeWidth={2} />
        </LineChart>
      </ResponsiveContainer>
  </div>
  
);
export default EcologicalInferenceChart;
