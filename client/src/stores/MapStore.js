import { createContext, useState, useEffect } from 'react';

// Map Store Context
export const MapStoreContext = createContext({});

// Reducer action types
export const MapStoreReducerAction = {
    SET_MAP_VIEW: "SET_MAP_VIEW",
    SET_SELECTED_STATE: "SET_SELECTED_STATE",
    SET_SELECTED_DISTRICT: "SET_SELECTED_DISTRICT",
    SET_SELECTED_PRECINCT: "SET_SELECTED_PRECINCT",
};

// Context Provider
function MapStoreContextProvider(props) {
    const [store, setStore] = useState({
        selectedMapView: "state", // Default view: "state", can also be "district" or "precinct"
        selectedState: null,
        selectedDistrict: null,
        selectedPrecinct: null,
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

            case MapStoreReducerAction.SET_SELECTED_STATE: {
                setStore((prevStore) => ({
                    ...prevStore,
                    selectedState: payload, // Update the selected state
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

            default: {
                return store;
            }
        }
    };

    // Methods to update the store
    store.setMapView = function (selectedMapView) {
        MapStoreReducer(MapStoreReducerAction.SET_MAP_VIEW, selectedMapView);
    };

    store.setSelectedState = function (state) {
        MapStoreReducer(MapStoreReducerAction.SET_SELECTED_STATE, state);
    };

    store.setSelectedDistrict = function (district) {
        MapStoreReducer(MapStoreReducerAction.SET_SELECTED_DISTRICT, district);
    };

    store.setSelectedPrecinct = function (precinct) {
        MapStoreReducer(MapStoreReducerAction.SET_SELECTED_PRECINCT, precinct);
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
