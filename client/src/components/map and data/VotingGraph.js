import React from 'react';
import { ScatterChart, Scatter, XAxis, YAxis, CartesianGrid, Tooltip, Legend, Line } from 'recharts';

const VotingGraph = () => {
  const dataA = [
    { x: 0, y: 55 },
    { x: 10, y: 50 },
    { x: 25, y: 45 },
    { x: 50, y: 35 },
    { x: 75, y: 30 },
    { x: 100, y: 20 },
  ];

  const dataB = [
    { x: 0, y: 45 },
    { x: 10, y: 50 },
    { x: 25, y: 55 },
    { x: 50, y: 65 },
    { x: 75, y: 70 },
    { x: 100, y: 80 },
  ];

  return (
    <div>
      <h2>Measuring Racially Polarized Voting</h2>
      <ScatterChart
        width={600}
        height={400}
        margin={{ top: 20, right: 20, bottom: 20, left: 20 }}
      >
        <CartesianGrid />
        <XAxis type="number" dataKey="x" name="Percent Latino (%)" unit="%" />
        <YAxis type="number" dataKey="y" name="Vote Share (%)" unit="%" domain={[0, 100]} />
        <Tooltip cursor={{ strokeDasharray: '3 3' }} />
        <Legend />
        
        {/* Scatter Points */}
        <Scatter name="Candidate A" data={dataA} fill="rgba(75,192,192,1)" />
        <Scatter name="Candidate B" data={dataB} fill="rgba(255,99,132,1)" />
        
        {/* Lines for Candidate A and Candidate B */}
        <Line type="monotone" dataKey="y" data={dataA} stroke="rgba(75,192,192,1)" dot={false} />
        <Line type="monotone" dataKey="y" data={dataB} stroke="rgba(255,99,132,1)" dot={false} />
        
      </ScatterChart>
    </div>
  );
};

export default VotingGraph;
