import React, { useEffect } from "react";
import { useState, useContext } from "react";
import "../../../stylesheets/map and data/graphs/GinglesGraph.css";
import { MapStoreContext } from "../../../stores/MapStore";
import { VictoryChart, VictoryScatter, VictoryLine, VictoryTheme } from "victory";

export default function GinglesGraph() {
    const { store } = useContext(MapStoreContext);
    const [selectedGingles, setSelectedGingles] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("NONE");
    const [JSON, setJson] = useState(null);

    const [republicanData, setRepublicanData] = useState([]);
    const [democraticData, setDemocraticData] = useState([]);
    const [ginglesLines, setGinglesLines] = useState([]);
    const [xAxisName, setXAxisName] = useState("");
    const yAxisNameDem = "DEMOCRATIC_VOTE_SHARE";
    const yAxisNameRep = "REPUBLICAN_VOTE_SHARE";    

    useEffect(() => {
        check_state();
    }, [selectedGingles, racialGroup, regionType]);

    useEffect(() => {
        console.log("Updated JSON:", JSON);
    }, [JSON]);

    const fetch_gingles_race = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/demographics/${racialGroup}`);
            const json = await response.json();
            console.log("Gingles racial data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    };

    const fetch_gingles_income = async (stateAbbreviation) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/economic`);
            const json = await response.json();
            console.log("Gingles income data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    const fetch_gingles_income_by_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/economic/${regionType}`);
            const json = await response.json();
            console.log("Gingles income by region data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    const fetch_gingles_income_race = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/gingles/demographics-and-economic/${racialGroup}`);
            const json = await response.json();
            console.log("Gingles income-race data:", json);
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    const check_state = async () => {
        const stateCodeMapping = {
        45: "SC", // South Carolina
        24: "MD", // Maryland
        };
        var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        let response;
        if (selectedGingles==="race"){
            console.log(" race gingles for state,racialGroup:", stateAbbreviation, racialGroup);
            response = await fetch_gingles_race(stateAbbreviation, racialGroup);
            setXAxisName("RACE_PERCENT");
        }
        else if (selectedGingles==="income"){
            setXAxisName("AVG_HOUSEHOLD_INCOME");
            if (regionType==="NONE"){
                console.log(" income gingles for state:", stateAbbreviation);
                response = await fetch_gingles_income(stateAbbreviation);
            }
            else{
                console.log(" income gingles for state,regionType:", stateAbbreviation, regionType);
                response = await fetch_gingles_income_by_region(stateAbbreviation, regionType);
            }
        }
        else if (selectedGingles==="income-race"){
            //TODO
            setXAxisName("RACE_INCOME_PERCENT");
            console.log("income-race gingles for state,racialGroup:", stateAbbreviation, racialGroup);
            response = await fetch_gingles_income_race(stateAbbreviation, racialGroup);
        }

        console.log("RETRIEVED GINGLES:",response);
        setJson(response);

        console.log("X Axis Name:", xAxisName);
        if(selectedGingles==="race"){
            setRepublicanData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameDem],
            })) || []);
        }
        else if(selectedGingles==="income"){
            setRepublicanData(response?.data?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameDem],
            })) || []);
        }
        else if(selectedGingles==="income-race"){
            setRepublicanData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameDem],
            })) || []);
        }
 

        console.log("Republican Data:", republicanData);
        console.log("Democratic Data:", democraticData);
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

            {republicanData.length>0 && (
                <VictoryChart theme={VictoryTheme.material} domainPadding={20}>
                    <VictoryScatter
                        data={republicanData}
                        style={{ data: { fill: "red" } }}
                    />
                    <VictoryScatter
                        data={democraticData}
                        style={{ data: { fill: "blue" } }}
                    />
                    {/* <VictoryLine
                        data={JSON.lines?.republican}
                        x={xAxisName}
                        y={yAxisNameRep}
                        style={{ data: { stroke: "red" } }}
                    />
                    <VictoryLine
                        data={JSON.lines?.democratic}
                        x={xAxisName}
                        y={yAxisNameDem}
                        style={{ data: { stroke: "blue" } }}
                    /> */}
                </VictoryChart>
            )}

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
        </div>
    );
}