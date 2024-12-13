import React, { useEffect, useState, useContext } from "react";
import "../../../stylesheets/map and data/graphs/GinglesGraph.css";
import { MapStoreContext } from "../../../stores/MapStore";
import { Chart, registerables } from 'chart.js';
import { Chart as ChartJS } from 'react-chartjs-2';
import { point } from "leaflet";
Chart.register(...registerables);

export default function GinglesGraph() {
    const { store } = useContext(MapStoreContext);
    const [selectedGingles, setSelectedGingles] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("ALL");
    const [JSON, setJson] = useState(null);

    const [republicanData, setRepublicanData] = useState([]);
    const [democraticData, setDemocraticData] = useState([]);
    const [republicanLine, setRepublicanLine] = useState([]);
    const [democraticLine, setDemocraticLine] = useState([]);
    const [xAxisName, setXAxisName] = useState("");
    const yAxisNameDem = "DEMOCRATIC_VOTE_SHARE";
    const yAxisNameRep = "REPUBLICAN_VOTE_SHARE";    
    const [xAxisGraphTitle, setXAxisGraphTitle] = useState("");
    const yAxisGraphTitle = "Vote Share (%)";
    const [graphTitle, setGraphTitle] = useState("");
    const [republicanCandidate, setRepublicanCandidate] = useState("");
    const [democraticCandidate, setDemocraticCandidate] = useState("");
    const [election, setElection] = useState("");
    
    useEffect(() => {
        check_state();
    }, [selectedGingles, racialGroup, regionType]);

    useEffect(() => {
        console.log("Updated JSON:", JSON);
    }, [JSON]);

    //GUI-12
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

    //GUI-13 (Specific regions)
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

    //GUI-14
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
            setXAxisGraphTitle(`Percent ${racialGroup}`);
            setGraphTitle(`Gingles 2/3 Analysis for ${racialGroup} in ${stateAbbreviation} Precincts`);
        }
        else if (selectedGingles==="income"){
            setXAxisName("AVG_HOUSEHOLD_INCOME");
            console.log(" income gingles for state,regionType:", stateAbbreviation, regionType);
            response = await fetch_gingles_income_by_region(stateAbbreviation, regionType);
            setXAxisGraphTitle(`${regionType} Average Household Income`);
            setGraphTitle(`Gingles 2/3 Analysis for ${regionType} Avg Household Income ${stateAbbreviation} Precincts`);
            console.log("RETRIEVED INCOME GINGLES:",response);
        }
        else if (selectedGingles==="income-race"){
            setXAxisName("RACE_INCOME_PERCENT");
            setXAxisGraphTitle(`Percent ${racialGroup} Income`);
            setGraphTitle(`Gingles 2/3 Analysis for ${racialGroup} Income in ${stateAbbreviation} Precincts`);
            console.log("income-race gingles for state,racialGroup:", stateAbbreviation, racialGroup);
            response = await fetch_gingles_income_race(stateAbbreviation, racialGroup);
            console.log("RETRIEVED INCOME-RACE GINGLES:",response);

        }

        console.log("RETRIEVED GINGLES:",response);
        setJson(response);

        setRepublicanCandidate(response?.candidates?.Republican);
        setDemocraticCandidate(response?.candidates?.Democratic);
        setElection(response?.election);

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
            setRepublicanLine(response?.lines?.[racialGroup]?.republican?.x.map((x, i) => ({
                x,
                y: response.lines[racialGroup].republican.y[i],
            })) || []);
            setDemocraticLine(response?.lines?.[racialGroup]?.democratic?.x.map((x, i) => ({
                x,
                y: response.lines[racialGroup].democratic.y[i],
            })) || []);
        }
        else if(selectedGingles==="income"){
            setRepublicanData(response?.data?.[regionType]?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.[regionType]?.map((data) => ({
                x: data[xAxisName],
                y: data[yAxisNameDem],
            })) || []);
            setRepublicanLine(response?.lines?.[regionType]?.republican?.x.map((x, i) => ({
                x,
                y: response.lines[regionType].republican.y[i],
            })) || []);
            setDemocraticLine(response?.lines?.[regionType]?.democratic?.x.map((x, i) => ({
                x,
                y: response.lines[regionType].democratic.y[i],
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
            setRepublicanLine(response?.lines?.[racialGroup]?.republican?.x.map((x, i) => ({
                x,
                y: response.lines[racialGroup].republican.y[i],
            })) || []);
            setDemocraticLine(response?.lines?.[racialGroup]?.democratic?.x.map((x, i) => ({
                x,
                y: response.lines[racialGroup].democratic.y[i],
            })) || []);
        }
 

        console.log("Republican Data:", republicanData);
        console.log("Democratic Data:", democraticData);
    }

    const combinedData = {
        labels: republicanLine.map(d => d.x),
        datasets: [
            {
                label: 'Republican Vote Share',
                data: republicanData,
                backgroundColor: 'rgba(255, 0, 0, 0.2)',
                type: 'scatter'
            },
            {
                label: 'Democratic Vote Share',
                data: democraticData,
                backgroundColor: 'rgba(0, 0, 255, 0.2)',
                type: 'scatter'
            },
            {
                label: 'Republican Trend Line',
                data: republicanLine.map(d => ({ x: d.x, y: d.y })),
                borderColor: 'red',
                fill: false,
                type: 'line',
                pointRadius: 0
            },
            {
                label: 'Democratic Trend Line',
                data: democraticLine.map(d => ({ x: d.x, y: d.y })),
                borderColor: 'blue',
                fill: false,
                type: 'line',
                pointRadius: 0
            }
        ]
    };

    const options = {
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                title: {
                    display: true,
                    text: xAxisGraphTitle,
                },
            },
            y: {
                title: {
                    display: true,
                    text: yAxisGraphTitle,
                },
            },
        },
        plugins: {
            legend: {
                display: true,
                position: 'top',
            },
        },
    };

    return (
        <div>
            {/* Drop down options for Gingles Analysis */}
            <div className="select-container">
                <label>Selected Gingles Option</label>
                <select 
                    value={selectedGingles} 
                    onChange={(event) => setSelectedGingles(event.target.value)}
                >
                    <option value="race">Precinct Race</option> 
                    <option value="income">Precinct Income</option>
                    <option value="income-race">Precinct Income/Race</option>
                </select>
            </div>

            {republicanData.length > 0 && (
                <div className="graph-container">
                    <h2>{graphTitle}</h2>
                    <p>{election}</p>
                    <p>{republicanCandidate} vs {democraticCandidate}</p>
                    <ChartJS type='scatter' data={combinedData} options={options} />
                </div>
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
                                value="ALL" 
                                checked={regionType === "ALL"} 
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