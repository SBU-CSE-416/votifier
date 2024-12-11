import React from "react";
import { useState, useContext } from "react";
import "../../../stylesheets/map and data/graphs/GinglesGraph.css";
import { MapStoreContext } from "../../../stores/MapStore";

export default function GinglesGraph() {
    const { store } = useContext(MapStoreContext);
    const [selectedGingles, setSelectedGingles] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("URBAN");

    const fetch_gingles_racial = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/demographics/${racialGroup}`);
            const json = await response.json();
            console.log("Gingles racial data:", json);
        } catch (error){
            console.error(error.message);
        }
    };

    const fetch_gingles_economic = async (stateAbbreviation) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/economic`);
            const json = await response.json();
            console.log("Gingles income data:", json);
        } catch (error){
            console.error(error.message);
        }
    }

    const fetch_gingles_economic_by_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/economic/${regionType}`);
            const json = await response.json();
            console.log("Gingles income by region data:", json);
        } catch (error){
            console.error(error.message);
        }
    }

    var Test = fetch_gingles_economic("SC");

    return (
        <div>
            <div className="select-container">
                <label>Selected Gingles Option</label>
                <select 
                    value={selectedGingles} 
                    onChange={(event) => setSelectedGingles(event.target.value)}
                >
                    <option value="race">precinct race</option> 
                    <option value="income">precinct income</option>
                    <option value="income-race">precinct income/race</option>
                </select>
            </div>
            {selectedGingles === "race" ? 
            <>
                <div className="select-container">
                    <label>Racial Group</label>
                    <div>
                        <label>
                            <input 
                                type="radio" 
                                name="racialGroup" 
                                value="WHITE" 
                                checked={racialGroup === "WHITE"} 
                                onChange={(event) => setRacialGroup(event.target.value)} 
                            />
                            White
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="racialGroup" 
                                value="BLACK" 
                                checked={racialGroup === "BLACK"} 
                                onChange={(event) => setRacialGroup(event.target.value)} 
                            />
                            Black
                        </label>
                        {store.selectedStateCode === 24? <>
                            <label>
                            <input 
                                type="radio" 
                                name="racialGroup" 
                                value="ASIAN" 
                                checked={racialGroup === "ASIAN"} 
                                onChange={(event) => setRacialGroup(event.target.value)} 
                            />
                            Asian
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="racialGroup" 
                                value="HISPANIC" 
                                checked={racialGroup === "HISPANIC"} 
                                onChange={(event) => setRacialGroup(event.target.value)} 
                            />
                            Hispanic
                        </label>
                        </> : null}
                    </div>
                </div>
            </>
            : null}
            <h2>Coming sooon</h2>
        </div>
    );
}