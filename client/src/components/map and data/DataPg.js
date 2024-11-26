import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import HouseholdIncomeHistogram from "./HouseholdIncomeHistogram";
import EcologicalInferenceChart from "./EcologicalInferenceChart"
import VotingGraph from "./VotingGraph";
import MedianIncomeBoxPlot from "./MedianIncomeBoxPlot"
import IncomeRangeDensityChart from "./IncomeRangeDensityChart"
import GeneralInfoBox from "./GeneralInfoBox"
import "../../stylesheets/dataPg.css"
import { useState } from "react";
export default function DataPg({state}) {
    const [activeTab, setActiveTab] = useState("summary");
    console.log("DataPg state:", state);

    const houseIncomeData = Object.entries(state.HOUS_INCOME_DIS).map(([bin, value]) => {
      const formattedBin = bin.replace("_", "-").replace("K", "K+").replace("MORE", "+");
      return { bin: formattedBin, value };
    });
    return(
        
    <div id="info" >
        
        <div className="datapg-container">
            <div className="data-genernal-info"> 
            <GeneralInfoBox 
                state = {state}
            />
            </div>
              <div className="data-navbar">
                <div
                  className={`tab ${activeTab === "summary" ? "active" : ""}`}
                  onClick={() => setActiveTab("summary")}
                >
                  Summary
                </div>
                <div
                  className={`tab ${activeTab === "gingles" ? "active" : ""}`}
                  onClick={() => setActiveTab("gingles")}
                >
                  Gingles 2/3
                </div>
                <div
                  className={`tab ${activeTab === "ei-analysis" ? "active" : ""}`}
                  onClick={() => setActiveTab("ei-analysis")}
                >
                  Ecological Inference
                </div>
                <div
                  className={`tab ${activeTab === "ensemble-data" ? "active" : ""}`}
                  onClick={() => setActiveTab("ensemble-data")}
                >
                  Ensemble Data
                </div>
              </div>
            <div className="data-charts-container">
                {activeTab === "summary" && (
                  <div className="data-charts">
                    <HouseholdIncomeHistogram w={400} h={200} title={"Household Income Distribution"} data={houseIncomeData} />
                    <HistogramChart w = {400} h = {200} title={"Age Distribution"}></HistogramChart>
                    {/* <RacialBarChart w = {400} h = {200} title={"Population Distribution"}></RacialBarChart> */}
                  </div>
                )}
                {activeTab === "ensemble-data" && (
                  <div className="data-charts">
                    <MedianIncomeBoxPlot w = {400} h = {200} title={"Median Income Distribution"}></MedianIncomeBoxPlot>
                  </div>
                )}
                {activeTab === "gingles" && (
                  <div className="data-charts">
                    <h2> Comming Soon</h2>
                    {/* <IncomeRangeDensityChart w={400} h={200} title={"Support for Candidates by Racial Group"}/> */}
                  </div>
                )}
                {activeTab === "ei-analysis" && (
                  <div className="data-charts">
                    <EcologicalInferenceChart w={400} h={200} title={"Support for Candidates by Racial Group"} />
                    {/* <IncomeRangeDensityChart w={400} h={200} title={"Support for Candidates by Racial Group"}/> */}
                  </div>
        )}  
            </div>
            
        </div>
    </div>
        
    );
}