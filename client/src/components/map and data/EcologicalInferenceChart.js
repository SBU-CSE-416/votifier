import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

// Example data for South Carolina with support for each candidate by demographic group
const data = [
  { demographic: 'Indian', Hardy: 0.15, Kolstad: 0.30, Nadeem: 0.55 },
  { demographic: 'East Asian', Hardy: 0.10, Kolstad: 0.60, Nadeem: 0.30 },
  { demographic: 'Non-Asian', Hardy: 0.50, Kolstad: 0.20, Nadeem: 0.30 },
];

const renderCustomizedLabel = ({ x, y, width, value }) => {
  return (
    <text x={x + width / 2} y={y} dy={-6} fill="#666" textAnchor="middle" fontSize={12}>
      {`${(value * 100).toFixed(0)}%`}
    </text>
  );
};

const CandidateSupportChart = ({ candidate }) => (
  <ResponsiveContainer width="100%" height={250}>
    <BarChart
      data={data}
      margin={{ top: 20, right: 30, left: 20, bottom: 10 }}
    >
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="demographic" />
      <YAxis domain={[0, 1]} tickFormatter={(tick) => `${(tick * 100).toFixed(0)}%`} />
      <Tooltip formatter={(value) => `${(value * 100).toFixed(2)}%`} />
      <Legend />
      <Bar dataKey={candidate} fill="#8884d8" label={renderCustomizedLabel} />
    </BarChart>
  </ResponsiveContainer>
);

const EcologicalInferenceChart = () => (
  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
    <h2>Support for Hardy</h2>
    <CandidateSupportChart candidate="Hardy" />

    <h2>Support for Kolstad</h2>
    <CandidateSupportChart candidate="Kolstad" />

    <h2>Support for Nadeem</h2>
    <CandidateSupportChart candidate="Nadeem" />
  </div>
);

export default EcologicalInferenceChart;
