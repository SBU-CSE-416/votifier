import React, { useEffect, useRef } from 'react';
import * as d3 from 'd3';

const sampleData = [
  { demographic: 'Indian', value: 0.3 },
  { demographic: 'Indian', value: 0.4 },
  { demographic: 'Indian', value: 0.5 },
  { demographic: 'East Asian', value: 0.6 },
  { demographic: 'East Asian', value: 0.7 },
  { demographic: 'East Asian', value: 0.8 },
  { demographic: 'Non-Asian', value: 0.2 },
  { demographic: 'Non-Asian', value: 0.3 },
  { demographic: 'Non-Asian', value: 0.6 },
];

const EcologicalInferenceChartD3 = ({w, h, title}) => {
  const ref = useRef(); 

  useEffect(() => {
    const margin = { top: 30, right: 30, bottom: 40, left: 50 },
      width = w - margin.left - margin.right,
      height = h - margin.top - margin.bottom;

    // Select the element and clear any existing content
    const svg = d3.select(ref.current)
      .attr('width', width + margin.left + margin.right)
      .attr('height', height + margin.top + margin.bottom)
      .append('g')
      .attr('transform', `translate(${margin.left},${margin.top})`);

    // Set up the x-axis and y-axis scales
    const x = d3.scaleLinear()
      .domain([0, 1]) // Adjust based on your data range
      .range([0, width]);

    const y = d3.scaleLinear()
      .range([height, 0])
      .domain([0, 30]); // Adjust based on your data density range

    // Create the x and y axes
    svg.append('g')
      .attr('transform', `translate(0, ${height})`)
      .call(d3.axisBottom(x));

    svg.append('g')
      .call(d3.axisLeft(y));

    // Color scheme for different groups
    const color = d3.scaleOrdinal()
      .domain(['Indian', 'East Asian', 'Non-Asian'])
      .range(['#1f77b4', '#ff7f0e', '#2ca02c']);

    // Compute the kernel density estimation for each group
    const kde = kernelDensityEstimator(kernelEpanechnikov(0.1), x.ticks(40));

    const groups = d3.groups(sampleData, d => d.demographic);
    groups.forEach(([key, values]) => {
      const density = kde(values.map(d => d.value));

      svg.append('path')
        .datum(density)
        .attr('fill', 'none')
        .attr('stroke', color(key))
        .attr('stroke-width', 2)
        .attr('stroke-linejoin', 'round')
        .attr('d', d3.line()
          .curve(d3.curveBasis)
          .x(d => x(d[0]))
          .y(d => y(d[1]))
        );
    });

    function kernelDensityEstimator(kernel, X) {
      return function (V) {
        return X.map(x => [x, d3.mean(V, v => kernel(x - v))]);
      };
    }

    function kernelEpanechnikov(k) {
      return function (v) {
        return Math.abs(v /= k) <= 1 ? (0.75 * (1 - v * v)) / k : 0;
      };
    }

  }, []);

  return (
    <div>
      <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>
      {title || "Title Needed"}
    </h2>
      <svg ref={ref}></svg>
    </div>
  );
};

export default EcologicalInferenceChartD3;