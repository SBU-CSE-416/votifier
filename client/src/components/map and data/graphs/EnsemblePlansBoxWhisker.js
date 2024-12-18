import React, {useEffect, useState, useContext} from "react";
import { MapStoreContext } from "../../../stores/MapStore";
import { formatVariable } from "../../../utilities/ReformatVariableNamesUtil";
import { stateCodeMapping } from "../../../utilities/FederalInfomationProcessingStandardEnumUtil";
import "../../../stylesheets/map and data/graphs/EnsemblePlansBoxWhisker.css";
import Plot from "react-plotly.js";
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from 'chart.js';
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);


export default function EnsemblePlansBoxWhisker(){
    const { store } = useContext(MapStoreContext);
    const [selectedEnsemble, setSelectedEnsemble] = useState("1");
    const [selectedDataType, setSelectedDataType] = useState("race");
    const [selectedView, setSelectedView] = useState("box-whisker");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [incomeGroup, setIncomeGroup] = useState("_0_35K");
    const [regionType, setRegionType] = useState("RURAL");
    const [boxWhiskerData, setBoxWhiskerData] = useState(null);
    const [optionsData, setOptionsData] = useState(null);
    const [barChartData, setBarChartData] = useState(null);
    const [barOptionsData, setBarOptionsData] = useState(null);
    useEffect(() => {
        if (selectedView === "box-whisker"){
            check_box_plot_state();
        }
        else if (selectedView === "summary"){
            handle_ensemble_summary();
        }
    }, [racialGroup, regionType, incomeGroup, selectedEnsemble, selectedDataType, selectedView]);

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
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/boxplot/economics/${incomeGroup}`);
            const json = await response.json();
            console.log("boxplot income data:",json);
            return json;
        } catch (error) {
            console.error(error.message);
        }
    };

    const fetch_ensemble_data_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/boxplot/region_type/${regionType}`);
            const json = await response.json();
            console.log("boxplot region data:",json);
            return json;
        } catch (error) {
            console.error(error.message);
        }
    };

    const fetch_ensemble_summary = async (stateAbbreviation) => {
        try{
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/plansplits`);
            const json = await response.json();
            console.log("ensemble summary data:",json);
            return json;
        } catch (error) {
            console.error(error.message);
        }
    };

    const check_box_plot_state = async () => {
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

    const handle_ensemble_summary = async () => {
        var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        let response = await fetch_ensemble_summary(stateAbbreviation);
        var ensemblePlan = `ensemble_${selectedEnsemble}`;
        let ensembleData = response.data[ensemblePlan];
        console.log("ensemble summary data fetched: ", response);
        console.log("RETRIEVED ensemble data:",ensembleData);
        
        const barData = {
            labels: Object.keys(ensembleData.data),
            datasets: [
              {
                label: 'Number of Plans',
                data: Object.values(ensembleData.data),
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
              },
            ],
          };
          setBarChartData(barData);
          setBarOptionsData(ensembleData.labels);
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
                    <label>Selected View</label>
                    <select 
                        value={selectedView}
                        onChange={(event) => setSelectedView(event.target.value)}
                        style={{width:"auto"}}
                    >
                        <option value="box-whisker">Box & Whisker Plot</option> 
                        <option value="summary">Summary</option>
                    </select>
                </div>

                {selectedView === "box-whisker" && (
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
                )}
            </div>

            {selectedView === "box-whisker" && (            
                <div className="box-plot-container">
                {plotData && (            
                    <Plot
                        data={[
                            ...plotData.map(trace => ({
                            ...trace,
                            marker: {
                                color: '#3388ff',
                            },
                            line: {
                                color: '#3388ff',
                            },
                            })),
                            dotData
                        ]}
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
                )}
                </div>
            )}

            {(selectedView === "summary" && barChartData) && (
                <div style={{display:"flex", flexDirection:"column", alignItems:"center"}}>
                    <p style={{}}>{barOptionsData?.title}</p>
                    <Bar
                    data={{
                        ...barChartData,
                        datasets: barChartData.datasets.map(dataset => ({
                        ...dataset,
                        backgroundColor: '#94aae4', // Set the color for the box
                        borderColor: '#283d71', // Set the color for the whiskers
                        }))
                    }}                        
                    options={{
                        responsive: true,
                        plugins: {
                            legend: {
                            position: 'top',
                            },
                            title: {
                            display: true,
                            text: barOptionsData?.subtitle,
                            },
                        },
                        scales: {
                            x: {
                            title: {
                                display: true,
                                text: barOptionsData?.axisX,
                            },
                            ticks: {
                                callback: function(value, index, values) {
                                return barOptionsData?.axisXTicks[index];
                                }
                            }
                            },
                            y: {
                            title: {
                                display: true,
                                text: barOptionsData?.axisY,
                            },
                            ticks: {
                                callback: function(value, index, values) {
                                return barOptionsData?.axisYTicks[index];
                                }
                            }
                            }
                        }
                        }}
                    />
                </div>
            )}


            {( selectedView==="box-whisker" && selectedDataType==="race") &&
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
            </>}

            {( selectedView==="box-whisker" && selectedDataType === "region") &&
            <>
                <div className="box-plot-select-container">
                    <label style={{fontWeight:"bold"}}>Region Type</label>
                    <div>
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
            </>}

            {( selectedView==="box-whisker" && selectedDataType==="income") && (
                <div className="select-container">
                    <label style={{fontWeight:"bold"}}>Economic Group</label>
                    <div>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="_0_35K"
                                checked={incomeGroup === "_0_35K"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Below Poverty
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="_35K_60K"
                                checked={incomeGroup === "_35K_60K"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Low Income
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="_60K_100K"
                                checked={incomeGroup === "_60K_100K"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Lower Middle Income
                        </label>
                    </div>
                    <div>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="_100K_125K"
                                checked={incomeGroup === "_100K_125K"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Middle Income
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="_125K_150K"
                                checked={incomeGroup === "_125K_150K"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Upper Middle Income
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="_150K_MORE"
                                checked={incomeGroup === "_150K_MORE"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            High Income
                        </label>
                    </div>
                </div>
            )}
        </div>
    );
}