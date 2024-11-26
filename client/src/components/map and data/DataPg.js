import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import EcologicalInferenceChart from "./EcologicalInferenceChart"
import VotingGraph from "./VotingGraph";
import MedianIncomeBoxPlot from "./MedianIncomeBoxPlot"
import IncomeRangeDensityChart from "./IncomeRangeDensityChart"
import GeneralInfoBox from "./GeneralInfoBox"
import "../../stylesheets/dataPg.css"
import { useState } from "react";
export default function DataPg({state}) {
    const [activeTab, setActiveTab] = useState("cd-table");
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
                  className={`tab ${activeTab === "cd-table" ? "active" : ""}`}
                  onClick={() => setActiveTab("cd-table")}
                >
                  Congressional Districts
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
                {activeTab === "demographics" && (
                  <div className="data-charts">
                    <HistogramChart w = {400} h = {200} title={"Age Distribution"}></HistogramChart>
                    <RacialBarChart w = {400} h = {200} title={"Population Distribution"}></RacialBarChart>
                    <MedianIncomeBoxPlot w = {400} h = {200} title={"Median Income Distribution"}></MedianIncomeBoxPlot>
                  </div>
                )}
                {activeTab === "racial" && (
                  <div className="data-charts">
                    <VotingGraph w={300} h={150} />
                  </div>
                )}
                {activeTab === "economic" && (
                  <div className="data-charts">
                    <EcologicalInferenceChart w={400} h={200} title={"Support for Candidates by Racial Group"} />
                    <IncomeRangeDensityChart w={400} h={200} title={"Support for Candidates by Racial Group"}/>
                  </div>
        )}  
            </div>
            
        </div>
    </div>
        
    );
}