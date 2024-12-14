// utilities/KernelDensityEstimationUtils.js
import * as d3 from 'd3';

export const kernelDensityEstimator = (kernel, X) => {
    return function(V) {
        return X.map(function(x) {
            return [x, d3.mean(V, function(v) { return kernel(x - v); })];
        });
    };
};

export const epanechnikovKernel = (scale) => {
    return function(u) {
        return Math.abs(u /= scale) <= 1 ? 0.75 * (1 - u * u) / scale : 0;
    };
};

export const calculateKDE = (data, bandwidth = 0.1) => {
    const kde = kernelDensityEstimator(epanechnikovKernel(bandwidth), d3.range(0, 1, 0.01));
    return kde(data);
};