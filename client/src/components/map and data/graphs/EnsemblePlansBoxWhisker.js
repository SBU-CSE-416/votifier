import React, {useEffect, useState, useContext} from "react";
import { MapStoreContext } from "../../../stores/MapStore";
import { formatVariable } from "../../../utilities/ReformatVariableNamesUtil";
import { stateCodeMapping } from "../../../utilities/FederalInfomationProcessingStandardEnumUtil";

export default function EnsemblePlansBoxWhisker() {
    const { store } = useContext(MapStoreContext);
    const [selectedEnsemble, setSelectedEnsemble] = useState("1");
    const [selectedDataType, setSelectedDataType] = useState("race");
    const [racialGroup, setRacialGroup] = useState("WHITE");
    const [regionType, setRegionType] = useState("RURAL");
    const [boxWhiskerData, setBoxWhiskerData] = useState(null);
    useEffect(() => {
        check_state();
    }, [racialGroup, regionType]);

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
            //todo
        } else if (selectedDataType === "region"){
            //todo
        }
        console.log("RETRIEVED ENSEMBLE:",response);
        console.log("RETRIEVED ENSEMBLE DATA:",ensembleData);
        
        const formattedData = Object.keys(ensembleData).map((key) => ({
            min: ensembleData[key].MIN,
            q1: ensembleData[key].LOWER_QUARTILE_Q1,
            median: ensembleData[key].MEDIAN,
            q3: ensembleData[key].UPPER_QUARTILE_Q3,
            max: ensembleData[key].MAX,
        }));
        setBoxWhiskerData(formattedData);
    };

    return (
        <div style={{width:"100%"}}>
            <div className="select-container" style={{marginBottom:"10px"}}>
                <label>Selected Data Option</label>
                <select 
                    value={selectedDataType}
                    onChange={(event) => setSelectedDataType(event.target.value)}
                    style={{width:"30%"}}
                >
                    <option value="race">Racial Data</option> 
                    <option value="income">Economical Data</option>
                    <option value="region">Regional Data</option>
                </select>
            </div>

            {selectedDataType==="race" ? 
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
        </div>
    );
}