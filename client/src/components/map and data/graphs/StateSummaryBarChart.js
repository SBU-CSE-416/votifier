import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from 'recharts';

const rangeColors = {
  "0-35K": "#D8FFD6",
  "35K-60K": "#B4EAB0",
  "60K-100K": "#91D48A",
  "100K-125K": "#6DBF64",
  "125K-150K": "#47AA3C",
  "150K-MORE": "#0A9400"
};

const getColor = (range) => rangeColors[range] || "#283d71";

const HistogramChart = ({ data, title = "Title Needed" }) => (
  
  <div style={{ margin: '10px', width:"100%", height:"60vh"}}>
    <h2 style={{ textAlign: 'center', marginBottom: '0px' }}>
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
        <Bar dataKey="percentage">
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={getColor(entry.range)} />
          ))}
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  </div>
);

export default HistogramChart;
