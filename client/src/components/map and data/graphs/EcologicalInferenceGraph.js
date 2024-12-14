import { useContext, useState, useEffect } from 'react';
import { MapStoreContext } from '../../../stores/MapStore';
import { stateCodeMapping } from '../../../utilities/FederalInfomationProcessingStandardEnumUtil';
import '../../../stylesheets/map and data/graphs/EcologicalInferenceGraph.css';
import { Chart as ChartJS } from 'react-chartjs-2';
import { calculateKDE, kernelDensityEstimator, epanechnikovKernel } from '../../../utilities/KernelDensityEstimationUtils';
import { group, index } from 'd3';
import { point } from 'leaflet';

export default function EcologicalInferenceGraph() {
    const { store } = useContext(MapStoreContext);
    const [selectedEI, setSelectedEI] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("ALL");
    const [incomeGroup, setIncomeGroup] = useState("LOW");

    const [republicanData, setRepublicanData] = useState([]);
    const [democraticData, setDemocraticData] = useState([]);

    useEffect(() => {
        check_state();
    },[selectedEI, racialGroup, regionType, incomeGroup]);

    useEffect(() => {
        console.log("race ei republican data:", republicanData);
        console.log("race ei democratic data:", democraticData);
    }, [republicanData, democraticData]);

    //GUI-17
    const fetch_ei_race = async (stateAbbreviation, racialGroup, regionType) => {
        try {
            const response = await fetch(`http://localhost:8000/api/data/${stateAbbreviation}/ei-analysis/demographics/${racialGroup}/${regionType}`);
            const json = await response.json();
            console.log("Ecological Inference Racial Data: ", json);
            return json;
        } catch (error) {
                console.error(error.message);
        }
    }

    const check_state = async () => {
        var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
        let response;
        if(selectedEI==="race"){
            //TODO
            console.log("race ei for state,racialGroup,regionType", stateAbbreviation, racialGroup, regionType);
            response = await fetch_ei_race(stateAbbreviation, racialGroup, regionType);
        }
        else if(selectedEI==="income"){
            //TODO
            console.log("income ei for state,incomeGroup,regionType", stateAbbreviation, incomeGroup, regionType);
        }

        console.log("RETRIEVE EI:", response);

        if(selectedEI==="race"){
            setRepublicanData(response?.data?.[racialGroup]?.REPUBLICAN?.[regionType]);
            setDemocraticData(response?.data?.[racialGroup]?.DEMOCRATIC?.[regionType]);

        }
    }

    const plotKDE = (json) => {
        if (!json || !json.data || !json.data.sampled_voting_prefs) {
            console.error("json is null or data structure is not as expected:", json);
            return null;
        }

        const data = json.data.sampled_voting_prefs.map((values, index) => {
            const kde = calculateKDE(values);
            return {
                label: json.group_names[index],
                data: kde.map(d => ({ x: d[0], y: d[1] })),
                fill: true,
                borderColor: index === 0 ? 'green' : 'orange',
                backgroundColor: index === 0 ? 'rgba(0, 255, 0, 0.2)' : 'rgba(255, 165, 0, 0.2)',
                pointRadius: 0,
   
            };
        });

        const plotData = {
            datasets: data,
        };

        const options = {
            scales: {
                x: {
                    type: 'linear',
                    position: 'bottom',
                    title: {
                        display: true,
                        text: 'Voting Preference',
                    },
                },
                y: {
                    title: {
                        display: true,
                        text: 'Density',
                    },
                },
            },
        };

        return <ChartJS type="line" data={plotData} options={options} />;
    }

    return (
        <div>
            <div className="select-container">
                <label>Selected Ecological Inference Option</label>
                <select
                    value={selectedEI}
                    onChange={(event) => setSelectedEI(event.target.value)}
                >
                    <option value="race">Racial Group</option>
                    <option value="income">Economic Group</option>
                </select>
            </div>

            <div>
                <p>Republican Data</p>
                {republicanData && plotKDE(republicanData)}
                <p>Democratic Data</p>
                {democraticData && plotKDE(democraticData)}
            </div>

            {selectedEI==="race" && (
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
                    </div>
                </div>
            )}
            {selectedEI==="income" && (
                <div className="select-container">
                    <label>Economic Group</label>
                    <div>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="LOW"
                                checked={incomeGroup === "LOW"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Low
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="MIDDLE"
                                checked={incomeGroup === "MIDDLE"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            Middle
                        </label>
                        <label>
                            <input
                                type="radio"
                                name="incomeGroup"
                                value="HIGH"
                                checked={incomeGroup === "HIGH"}
                                onChange={(event) => setIncomeGroup(event.target.value)}
                            />
                            High
                        </label>
                    </div>
                </div>
            )}

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
        </div>
    );
}