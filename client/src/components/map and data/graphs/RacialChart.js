import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

const RacialBarChart = ({w, h, title}) => {
  const data = [
    { race: 'White', population: 600000 },
    { race: 'Black or African American', population: 400000 },
    { race: 'Asian', population: 200000 },
    { race: 'Native American', population: 100000 },
    { race: 'Other', population: 50000 }
  ];

  return (
    <div>
        <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>
          {title || "Title Needed"}
        </h2>
        <BarChart
          width={w}
          height={h}
          data={data}
          margin={{
            top: 20, right: 30, left: 20, bottom: 5,
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="race" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="population" fill="#8884d8" />
        </BarChart>
    
  </div>
  );
};

export default RacialBarChart;
