import React, { useEffect, useState, useContext } from "react";
import "../../../stylesheets/map and data/graphs/GinglesGraph.css";
import { MapStoreContext } from "../../../stores/MapStore";
import { formatVariable } from "../../../utilities/ReformatVariableNamesUtil";
import { stateCodeMapping } from "../../../utilities/FederalInfomationProcessingStandardEnumUtil";
import { Chart, registerables } from 'chart.js';
import { Chart as ChartJS } from 'react-chartjs-2';
Chart.register(...registerables);

export default function GinglesGraph() {
    const { store } = useContext(MapStoreContext);
    const [selectedGingles, setSelectedGingles] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("ALL");

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
    const API_URL = process.env.NODE_ENV === 'production' ? 'votifier-server-production.up.railway.app' : 'http://localhost:8000'
    useEffect(() => {
        check_state();
    }, [selectedGingles, racialGroup, regionType]);

    //GUI-12
    const fetch_gingles_race = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`${API_URL}/api/data/${stateAbbreviation}/gingles/demographics/${racialGroup}`);
            const json = await response.json();
            return json;
        } catch (error){
            console.error(error.message);
        }
    };

    //GUI-13 (Specific regions)
    const fetch_gingles_income_by_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`${API_URL}/api/data/${stateAbbreviation}/gingles/economic/${regionType}`);
            const json = await response.json();
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    //GUI-14
    const fetch_gingles_income_race = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`${API_URL}/api/data/${stateAbbreviation}/gingles/demographics-and-economic/${racialGroup}`);
            const json = await response.json();
            return json;
        } catch (error){
            console.error(error.message);
        }
    }

    const check_state = async () => {
        var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        let response;
        if (selectedGingles==="race"){
            response = await fetch_gingles_race(stateAbbreviation, racialGroup);
            setXAxisName("RACE_PERCENT");
            let formattedRacialGroup = formatVariable(racialGroup);
            setXAxisGraphTitle(`Percent ${formattedRacialGroup}`);
            setGraphTitle(`Gingles 2/3 Analysis for ${formattedRacialGroup} in ${stateAbbreviation} Precincts`);
        }
        else if (selectedGingles==="income"){
            setXAxisName("AVG_HOUSEHOLD_INCOME");
            response = await fetch_gingles_income_by_region(stateAbbreviation, regionType);
            let formattedRegionType = formatVariable(regionType);
            setXAxisGraphTitle(`${formattedRegionType} Average Household Income`);
            setGraphTitle(`Gingles 2/3 Analysis for ${formattedRegionType} Avg Household Income ${stateAbbreviation} Precincts`);
        }
        else if (selectedGingles==="income-race"){
            setXAxisName("RACE_INCOME_PERCENT");
            let formattedRacialGroup = formatVariable(racialGroup);
            setXAxisGraphTitle(`Percent ${formattedRacialGroup} Income`);
            setGraphTitle(`Gingles 2/3 Analysis for ${formattedRacialGroup} Income in ${stateAbbreviation} Precincts`);
            response = await fetch_gingles_income_race(stateAbbreviation, racialGroup);

        }

        setRepublicanCandidate(response?.candidates?.Republican);
        setDemocraticCandidate(response?.candidates?.Democratic);
        setElection(response?.election);

        if(selectedGingles==="race"){
            const xName = "RACE_PERCENT";
            setXAxisName(xName);
            setRepublicanData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xName],
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
            const xName = "AVG_HOUSEHOLD_INCOME";
            setXAxisName(xName);
            setRepublicanData(response?.data?.[regionType]?.map((data) => ({
                
                x: data[xName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.[regionType]?.map((data) => ({
                x: data[xName],
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
            const xName = "RACE_INCOME_PERCENT"
            setXAxisName(xName);
            setRepublicanData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xName],
                y: data[yAxisNameRep],
            })) || []);
            setDemocraticData(response?.data?.[racialGroup]?.map((data) => ({
                x: data[xName],
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
    }

    const ginglesData = {
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
        <div style={{width:"100%"}}>
            {/* Drop down options for Gingles Analysis */}
            <div className="select-container" style={{marginBottom:"10px"}}>
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
                    <ChartJS type='scatter' data={ginglesData} options={options} />
                </div>
            )}

            {/* Radial buttons for precinct-race GUI-12 OR precinct-income-race GUI-14*/}
            {(selectedGingles === "race" || selectedGingles === "income-race") ? 
            <>
                <div className="select-container">
                    <label style={{fontWeight:"bold"}}>Racial Group</label>
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
                    <label style={{fontWeight:"bold"}}>Region Type</label>
                    <div>
                        <label>
                            <input 
                                type="radio" 
                                name="regionType" 
                                value="ALL" 
                                checked={regionType === "ALL"} 
                                onChange={(event) => setRegionType(event.target.value)} 
                            />
                            All
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