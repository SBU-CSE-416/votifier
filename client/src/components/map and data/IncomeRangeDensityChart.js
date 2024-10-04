import React from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const candidateSupportData = [
  { supportLevel: 0.0, 'Less than $10,000': 12, '$10,000 - $39,999': 8, '$40,000 - $79,999': 10, 'More than $80,000': 5 },
  { supportLevel: 0.1, 'Less than $10,000': 20, '$10,000 - $39,999': 15, '$40,000 - $79,999': 12, 'More than $80,000': 8 },
  { supportLevel: 0.2, 'Less than $10,000': 30, '$10,000 - $39,999': 25, '$40,000 - $79,999': 18, 'More than $80,000': 12 },
  { supportLevel: 0.3, 'Less than $10,000': 40, '$10,000 - $39,999': 35, '$40,000 - $79,999': 20, 'More than $80,000': 15 },
  { supportLevel: 0.4, 'Less than $10,000': 35, '$10,000 - $39,999': 30, '$40,000 - $79,999': 22, 'More than $80,000': 15 },
  { supportLevel: 0.5, 'Less than $10,000': 30, '$10,000 - $39,999': 25, '$40,000 - $79,999': 20, 'More than $80,000': 12 },
  { supportLevel: 0.6, 'Less than $10,000': 20, '$10,000 - $39,999': 15, '$40,000 - $79,999': 32, 'More than $80,000': 30 },
  { supportLevel: 0.7, 'Less than $10,000': 12, '$10,000 - $39,999': 10, '$40,000 - $79,999': 38, 'More than $80,000': 32 },
];

const SupportByIncomeGroupChart = ({ w, h, title }) => (
  <div>
    <h3 style={{ textAlign: 'center', marginBottom: '10px' }}>{title || `Support for A`}</h3>
    <ResponsiveContainer width={w} height={h}>
      <LineChart
        data={candidateSupportData}
        margin={{ top: 10, right: 30, left: 20, bottom: 20 }}
      >
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="supportLevel" label={{ value: "Support Level", position: 'insideBottom', offset: -5 }} />
        <YAxis label={{ value: "Probability Density", angle: -90, position: 'insideLeft', offset: 0, dy: 70 }} />
        <Tooltip />
        
        <Line type="monotone" dataKey="Less than $10,000" stroke="#82ca9d" strokeWidth={2} />
        <Line type="monotone" dataKey="$10,000 - $39,999" stroke="#8884d8" strokeWidth={2} />
        <Line type="monotone" dataKey="$40,000 - $79,999" stroke="#ffc658" strokeWidth={2} />
        <Line type="monotone" dataKey="More than $80,000" stroke="#ff7f0e" strokeWidth={2} />
      </LineChart>
    </ResponsiveContainer>
  </div>
);

const IncomeRangeDensityChart = ({w, h}) => (
  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
    <h2 style={{ marginBottom: '30px' }}>Support for Candidates by Income Group</h2>
    <SupportByIncomeGroupChart w={w} h={h} title="Support for A" />

  </div>
);

export default IncomeRangeDensityChart;
