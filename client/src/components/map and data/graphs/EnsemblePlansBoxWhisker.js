import React, {useEffect, useState, useContext} from "react";
import { MapStoreContext } from "../../../stores/MapStore";
import { formatVariable } from "../../../utilities/ReformatVariableNamesUtil";
import { stateCodeMapping } from "../../../utilities/FederalInfomationProcessingStandardEnumUtil";
import "../../../stylesheets/map and data/graphs/EnsemblePlansBoxWhisker.css";
import Plot from "react-plotly.js";


export default function EnsemblePlansBoxWhisker(){
    const { store } = useContext(MapStoreContext);
    const [selectedEnsemble, setSelectedEnsemble] = useState("1");
    const [selectedDataType, setSelectedDataType] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [incomeGroup, setIncomeGroup] = useState("LOW");
    const [regionType, setRegionType] = useState("RURAL");
    const [boxWhiskerData, setBoxWhiskerData] = useState(null);
    const [optionsData, setOptionsData] = useState(null);
    useEffect(() => {
        check_state();
    }, [racialGroup, regionType, incomeGroup, selectedEnsemble, selectedDataType]);

    const fetch_ensemble_data_race = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/boxplot/demographics/${racialGroup}`);
            const json = await response.json();
            console.log("boxplot race data:",json);
            return json;
        } catch (error) {
            console.error(error.message);
        }
    };

    const fetch_ensemble_data_income = async (stateAbbreviation, incomeGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/boxplot/economic/${incomeGroup}`);
            const json = await response.json();
            console.log("boxplot income data:",json);
            return json;
        } catch (error) {
            console.error(error.message);
        }
    };

    const fetch_ensemble_data_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/boxplot/region/${regionType}`);
            const json = await response.json();
            console.log("boxplot region data:",json);
            return json;
        } catch (error) {
            console.error(error.message);
        }
    };

    const check_state = async () => {
        var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        var ensemblePlan = `ensemble_${selectedEnsemble}`;
        let response;
        let ensembleData;
        if (selectedDataType === "race"){
            console.log("race ensemble for state,racialGroup:",stateAbbreviation,racialGroup);
            response = await fetch_ensemble_data_race(stateAbbreviation, racialGroup);
            ensembleData = response.data[ensemblePlan]?.[racialGroup];
        } else if (selectedDataType === "income"){
            console.log("income ensemble for state,incomeGroup:",stateAbbreviation,incomeGroup);
            response = await fetch_ensemble_data_income(stateAbbreviation, incomeGroup);
            ensembleData = response.data[ensemblePlan]?.[incomeGroup];
        } else if (selectedDataType === "region"){
            console.log("region ensemble for state,regionType:",stateAbbreviation,regionType);
            response = await fetch_ensemble_data_region(stateAbbreviation, regionType);
            ensembleData = response.data[ensemblePlan]?.[regionType];
        }
        console.log("RETRIEVED ENSEMBLE:",response);
        console.log("RETRIEVED ENSEMBLE DATA:",ensembleData);
        
        if (response && ensembleData){
            const formattedData = Object.keys(ensembleData.data).map((key,index) => ({
                name: `Bucket ${key}`,
                min: ensembleData.data[key].MIN,
                q1: ensembleData.data[key].LOWER_QUARTILE_Q1,
                median: ensembleData.data[key].MEDIAN,
                q3: ensembleData.data[key].UPPER_QUARTILE_Q3,
                max: ensembleData.data[key].MAX,
                dot: ensembleData.data[key]._2022_DOT_VALUE,
                index: index+1,
            }));
            setBoxWhiskerData(formattedData);
            setOptionsData(ensembleData.labels);
            console.log("BOX WHISKER DATA:",formattedData);
            console.log("BOX WHISKER OPTIONS:",optionsData);
        }

        
    };

    const plotData = boxWhiskerData?.map((entry) => ({
        type: "box",
        name: entry.name,
        x: Array(5).fill(entry.index),
        y: [
          entry.min,
          entry.q1,
          entry.median,
          entry.q3,
          entry.max,
        ],
        showlegend: false,
    }));

    const dotData = {
        type: "scatter",
        mode: "markers",
        x: boxWhiskerData?.map((entry) => entry.index),
        y: boxWhiskerData?.map((entry) => entry.dot),
        marker: { color: "red", size: 8 },
        name: "2022 Enacted Plan",
    };
    

    return (
        <div style={{width:"100%"}}>
            <div style={{display:"flex", justifyContent:"center", gap:"20px"}}>
                <div className="box-plot-select-container" style={{marginBottom:"10px"}}>
                    <label>Selected Ensemble Plan</label>
                    <select 
                        value={selectedEnsemble}
                        onChange={(event) => setSelectedEnsemble(event.target.value)}
                        style={{width:"auto"}}
                    >
                        <option value="1">Ensemble 1</option> 
                        <option value="2">Ensemble 2</option>
                    </select>
                </div>

                <div className="box-plot-select-container" style={{marginBottom:"10px"}}>
                    <label>Selected Data Option</label>
                    <select 
                        value={selectedDataType}
                        onChange={(event) => setSelectedDataType(event.target.value)}
                        style={{width:"auto"}}
                    >
                        <option value="race">Racial Data</option> 
                        <option value="income">Economical Data</option>
                        <option value="region">Regional Data</option>
                    </select>
                </div>
            </div>

            <div className="box-plot-container">
                {plotData ? (            
                    <Plot
                        data={[...plotData, dotData]}
                        layout={{
                        autosize: true,
                        title: optionsData?.title,
                        xaxis: {
                            title: optionsData?.axisX,
                            tickvals: boxWhiskerData?.map((entry) => entry.index),
                            ticktext: boxWhiskerData?.map((entry) => entry.index),
                        },
                        yaxis: {
                            title: optionsData?.axisY,
                            tickvals: optionsData?.axisYTicks,
                        },
                        legend: { 
                            orientation: "h" ,
                            y:1.05,
                        },
                        margin:{
                            t:50,
                        },
                        }}
                        config={{
                            displayModeBar: false,
                            useResizeHandler: true,
                            responsive: true,
                            staticPlot: true,
                        }}
                        style={{width:"100%", height:"100%"}}
                    />
                ) : null}
            </div>


            {selectedDataType==="race" ? 
            <>
                <div className="box-plot-select-container">
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

            {selectedDataType === "region" ? 
            <>
                <div className="box-plot-select-container">
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