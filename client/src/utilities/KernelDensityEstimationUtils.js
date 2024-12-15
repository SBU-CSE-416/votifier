// utilities/KernelDensityEstimationUtils.js
import * as d3 from 'd3';

function kernelEpanechnikov(k) {
    return x => Math.abs(x /= k) <= 1 ? 0.75 * (1 - x * x) / k : 0;
}

function kernelDensityEstimator(kernel, X) {
    return function(V) {
        return X.map(x => [x, d3mean(V, v => kernel(x - v))]);
    };
}

function d3mean(array, f) {
    let sum = 0;
    let count = 0;
    for (const a of array) {
        const value = f ? f(a) : a;
        if (value != null && !Number.isNaN(value)) {
            sum += value;
            count++;
        }
    }
    return count ? sum / count : undefined;
}

export const calculateKDE = (values) => {
    if (!values || values.length === 0) return [];

    const domain = [Math.min(...values), Math.max(...values)];
    const bandwidth = (domain[1] - domain[0]) / 20;
    const xTicks = [];
    const steps = 50;
    const stepSize = (domain[1] - domain[0]) / steps;
    for (let i = domain[0]; i <= domain[1]; i += stepSize) {
        xTicks.push(i);
    }

    const kde = kernelDensityEstimator(kernelEpanechnikov(bandwidth), xTicks)(values);
    return kde; 
}