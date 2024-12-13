import { createContext, useState, useEffect } from 'react';

// Map Store Context
export const MapStoreContext = createContext({});

// Reducer action types
export const MapStoreReducerAction = {
    SET_MAP_VIEW: "SET_MAP_VIEW",
    SET_SELECTED_STATE_CODE: "SET_SELECTED_STATE_CODE",
    SET_SELECTED_DISTRICT: "SET_SELECTED_DISTRICT",
    SET_SELECTED_PRECINCT: "SET_SELECTED_PRECINCT",
    SET_HEATMAP: "SET_HEATMAP",
    SET_DEMOGRAPHIC: "SET_DEMOGRAPHIC", // for heatmap
    SET_DATA_VISIBILITY: "SET_DATA_VISIBILITY",
};

// Context Provider
function MapStoreContextProvider(props) {
    const [store, setStore] = useState({
        selectedMapView: "districts",
        selectedStateCode: null,
        selectedDistrict: null,
        selectedPrecinct: null,
        selectedHeatmap: "none", 
        selectedDemographic: "white",
        isDataVisible: false,
    });

    useEffect(() => {
        console.log("Map Store mounted with default values");
    }, []);

    /**
     * Reducer for the selectedMapView state.
     * Updates properties of the global store based on the action type provided.
     * @param {MapStoreReducerAction} actionType The type identifier for the desired action
     * @param {Any} payload The payload for the chosen action
     */
    const MapStoreReducer = (actionType, payload) => {
        switch (actionType) {
            case MapStoreReducerAction.SET_MAP_VIEW: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedMapView: payload, // Update the selectedMapView state
                }));
                return;
            }

            case MapStoreReducerAction.SET_SELECTED_STATE_CODE: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedStateCode: payload, // Update the selected state
                }));
                return;
            }

            case MapStoreReducerAction.SET_SELECTED_DISTRICT: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedDistrict: payload, // Update the selected district
                }));
                return;
            }

            case MapStoreReducerAction.SET_SELECTED_PRECINCT: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedPrecinct: payload, // Update the selected precinct
                }));
                return;
            }
            
            case MapStoreReducerAction.SET_HEATMAP: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedHeatmap: payload, // Update the selected heatmap
                }));
                return;
            }

            case MapStoreReducerAction.SET_DEMOGRAPHIC: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedDemographic: payload, // Update the selected demographic
                }));
                return;
            }

            case MapStoreReducerAction.SET_DATA_VISIBILITY: {
                setStore((prevStore) => ({
                    ...prevStore,
                    isDataVisible: payload, // Update the data visibility
                }));
                return;
            }

            default: {
                return store;
            }
        }
    };

    // Methods to update the store
    store.setMapView = function (selectedMapView) {
        MapStoreReducer(MapStoreReducerAction.SET_MAP_VIEW, selectedMapView);
    };

    store.setSelectedStateCode = function (state) {
        MapStoreReducer(MapStoreReducerAction.SET_SELECTED_STATE_CODE, state);
    };

    store.setSelectedDistrict = function (district) {
        MapStoreReducer(MapStoreReducerAction.SET_SELECTED_DISTRICT, district);
    };

    store.setSelectedPrecinct = function (precinct) {
        MapStoreReducer(MapStoreReducerAction.SET_SELECTED_PRECINCT, precinct);
    };

    store.setSelectedHeatmap = function (heatmap) {
        MapStoreReducer(MapStoreReducerAction.SET_HEATMAP, heatmap);
    };

    store.setSelectedDemographic = function (demographic) {
        MapStoreReducer(MapStoreReducerAction.SET_DEMOGRAPHIC, demographic);
    };

    store.setDataVisibility = function (isDataVisible) {
        MapStoreReducer(MapStoreReducerAction.SET_DATA_VISIBILITY, isDataVisible);
    };

    // Return context provider
    return (
        <MapStoreContext.Provider value={{ store }}>
            {props.children}
        </MapStoreContext.Provider>
    );
}

export default MapStoreContext;
export { MapStoreContextProvider };
