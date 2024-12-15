/**
 * Converts a database variable string into a more client-readable format.
 * @param {string} variable - The database variable string.
 * @returns {string} - The client-readable format.
 */
export const formatVariable = (variable) => {
    // Split the variable by underscores and capitalize each word
    return variable
        .toLowerCase()
        .split('_')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ');
};