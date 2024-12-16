import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const HistogramChart = ({ data, title = "Title Needed" }) => (
  
  <div style={{ margin: '30px', width:"100%", height:"100%"}}>
    <h2 style={{ textAlign: 'center', marginBottom: '10px', fontFamily: 'Arial, sans-serif', color: '#333' }}>
      {title}
    </h2>
    <ResponsiveContainer width={"90%"} height={"70%"}>
      <BarChart data={data} barCategoryGap="20%">
        <CartesianGrid strokeDasharray="3 3" stroke="#eee" />
        <XAxis 
          dataKey="range" 
          label={{
            value: "Income Ranges",
            position: "insideBottom",
            offset: 0,
            style: { fontSize: '14px', fontWeight: 'bold' },
          }}
          tick={{ fontSize: 12 }}
        />
        <YAxis 
          label={{
            value: "Percentage",
            angle: -90,
            offset: 0,
            position: "insideLeft",
            style: { fontSize: '14px', fontWeight: 'bold' },
          }}
          tick={{ fontSize: 12 }}
        />
        <Tooltip />
        <Bar dataKey="percentage" fill="#46ACC2" />
      </BarChart>
    </ResponsiveContainer>
  </div>
);

export default HistogramChart;
