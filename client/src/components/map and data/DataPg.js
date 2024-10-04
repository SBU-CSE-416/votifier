import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import EcologicalInferenceChart from "./EcologicalInferenceChart"
import EcologicalInferenceChartD3 from "./EcologicalInferenceChartD3"
import MedianIncomeBoxPlot from "./MedianIncomeBoxPlot"
import IncomeRangeDensityChart from "./IncomeRangeDensityChart"
import GeneralInfoBox from "./GeneralInfoBox"
import "../../stylesheets/dataPg.css"
import { useState } from "react";
export default function DataPg({state}) {
    const [activeTab, setActiveTab] = useState("demographics");
    console.log("State Data: ", state);
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
                  className={`tab ${activeTab === "demographics" ? "active" : ""}`}
                  onClick={() => setActiveTab("demographics")}
                >
                  Demographics
                </div>
                <div
                  className={`tab ${activeTab === "racial" ? "active" : ""}`}
                  onClick={() => setActiveTab("racial")}
                >
                  Racial Distribution
                </div>
                <div
                  className={`tab ${activeTab === "economic" ? "active" : ""}`}
                  onClick={() => setActiveTab("economic")}
                >
                  Ecological Inference
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
                    <RacialBarChart w={300} h={150} />
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