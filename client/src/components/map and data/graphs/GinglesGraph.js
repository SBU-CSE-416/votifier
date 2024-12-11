import React from "react";
import { useState, useContext } from "react";
import "../../../stylesheets/map and data/graphs/GinglesGraph.css";
import { MapStoreContext } from "../../../stores/MapStore";

export default function GinglesGraph() {
    const { store } = useContext(MapStoreContext);
    const [selectedGingles, setSelectedGingles] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("NONE");
    const [json, setJson] = useState(null);

    const fetch_gingles_racial = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/demographics/${racialGroup}`);
            const json = await response.json();
            console.log("Gingles racial data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    };

    const fetch_gingles_economic = async (stateAbbreviation) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/economic`);
            const json = await response.json();
            console.log("Gingles income data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    const fetch_gingles_economic_by_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/economic/${regionType}`);
            const json = await response.json();
            console.log("Gingles income by region data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    var Test = fetch_gingles_economic("SC");

    const check_state = async () => {
        const stateCodeMapping = {
        45: "SC", // South Carolina
        24: "MD", // Maryland
        };
        stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        if (selectedGingles==="race"){
            setJson(fetch_gingles_racial(stateAbbreviation, racialGroup));
        }
        else if (selectedGingles==="income"){
            setJson(fetch_gingles_economic(stateAbbreviation));
        }
        else if (selectedGingles==="income-race"){
            setJson(fetch_gingles_economic_by_region(stateAbbreviation, regionType));
        }
    }

    return (
        <div>
            {/* Drop down options for Gingles Analysis */}
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

            {/* Radial buttons for precinct-race GUI-12 OR precinct-income-race GUI-14*/}
            {(selectedGingles === "race" || selectedGingles === "income-race") ? 
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

            {/* Radial Buttons for precinct-income GUI-13 */}
            {selectedGingles === "income" ? 
            <>
                <div className="select-container">
                    <label>Region Type</label>
                    <div>
                        <label>
                            <input 
                                type="radio" 
                                name="regionType" 
                                value="NONE" 
                                checked={regionType === "NONE"} 
                                onChange={(event) => setRegionType(event.target.value)} 
                            />
                            None
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="regionType" 
                                value="RURAL" 
                                checked={regionType === "RURAL"} 
                                onChange={(event) => setRegionType(event.target.value)} 
                            />
                            Rural
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="regionType" 
                                value="SUBURBAN" 
                                checked={regionType === "SUBURBAN"} 
                                onChange={(event) => setRegionType(event.target.value)} 
                            />
                            Suburban
                        </label>
                        <label>
                            <input 
                                type="radio" 
                                name="regionType" 
                                value="URBAN" 
                                checked={regionType === "URBAN"} 
                                onChange={(event) => setRegionType(event.target.value)} 
                            />
                            Urban
                        </label>
                    </div>

                </div>
            </>
            : null}
            <h2>Coming sooon</h2>
        </div>
    );
}