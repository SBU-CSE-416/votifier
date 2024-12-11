import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts';

const HouseholdIncomeHistogram = ({ data, w = 600, h = 300, title = "Title Needed" }) => (
  <div>
    <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>
      {title}
    </h2>
    <BarChart width={w} height={h} data={data} barCategoryGap="0%">
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="bin" label={{ value: "Income Ranges", position: 'insideBottom', offset: -5 }} />
      <YAxis label={{ angle: -90, position: 'insideLeft', offset: -7 }} />
      <Tooltip />
      <Bar dataKey="value" fill="#46ACC2" />
    </BarChart>
  </div>
);

export default HouseholdIncomeHistogram;
