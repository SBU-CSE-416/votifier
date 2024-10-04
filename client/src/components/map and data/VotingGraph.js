import React from 'react';
import { Scatter } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const VotingGraph = () => {
  const data = {
    datasets: [
      {
        label: 'Candiate A',
        data: [
          { x: 0, y: 55 },
          { x: 10, y: 50 },
          { x: 25, y: 45 },
          { x: 50, y: 35 },
          { x: 75, y: 30 },
          { x: 100, y: 20 },
        ],
        backgroundColor: 'rgba(75,192,192,1)',
        borderColor: 'rgba(75,192,192,1)',
        showLine: true, 
        tension: 0.4,
      },
      {
        label: 'Candidate B',
        data: [
          { x: 0, y: 45 },
          { x: 10, y: 50 },
          { x: 25, y: 55 },
          { x: 50, y: 65 },
          { x: 75, y: 70 },
          { x: 100, y: 80 },
        ],
        backgroundColor: 'rgba(255,99,132,1)',
        borderColor: 'rgba(255,99,132,1)',
        showLine: true, 
        tension: 0.4, 
      },
    ],
  };

  const options = {
    scales: {
      x: {
        type: 'linear',
        position: 'bottom',
        title: {
          display: true,
          text: 'Percent Latino (%)',
        },
      },
      y: {
        beginAtZero: true,
        max: 100,
        title: {
          display: true,
          text: 'Vote Share (%)',
        },
      },
    },
    plugins: {
      legend: {
        display: true,
        position: 'top',
      },
      tooltip: {
        callbacks: {
          label: (context) => {
            return `Percent Latino: ${context.raw.x}%, Vote Share: ${context.raw.y}%`;
          },
        },
      },
    },
  };

  return (
    <div>
      <h2>Measuring Racially Polarized Voting</h2>
      <Scatter data={data} options={options} style={{ width: '600px', height: '400px' }}/>
    </div>
  );
};

export default VotingGraph;
