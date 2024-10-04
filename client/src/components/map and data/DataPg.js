import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import EcologicalInferenceChart from "./EcologicalInferenceChart"
import EcologicalInferenceChartD3 from "./EcologicalInferenceChartD3"
import MedianIncomeBoxPlot from "./MedianIncomeBoxPlot"
import IncomeRangeDensityChart from "./IncomeRangeDensityChart"
import GeneralInfoBox from "./GeneralInfoBox"
import "../../stylesheets/dataPg.css"
import { useState } from "react";
export default function DataPg({resetMapViewToDefault}) {
    const [activeTab, setActiveTab] = useState("demographics");
    return(
        
    <div id="info" >
        {/* <h3>Selected Location</h3>
        <p>{districtName}</p>
        <p>{population}</p>
        <p>{income}</p>
        <p>{political_lean}</p>
        <p>{total_precinct}</p>

        <div id="barGraphContainer">
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar1}%`, backgroundColor: '#3388ff' }}>{barData.bar1}%</div></div>
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar2}%`, backgroundColor: '#3388ff' }}>{barData.bar2}%</div></div>
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar3}%`, backgroundColor: '#3388ff' }}>{barData.bar3}%</div></div>
          <div className="bar"><div className="bar-fill" style={{ width: `${barData.bar4}%`, backgroundColor: '#3388ff' }}>{barData.bar4}%</div></div>
        </div> */}
        
        <div className="datapg-container">
            <div className="data-genernal-info"> 
            <GeneralInfoBox 
                title="Total Population"
                value="6.165 million "
                subtitle="from 2022"
            />
            
            </div>
            {/* Navigation Bar */}
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
        {/* <button onClick={resetMapViewToDefault} style={{ marginTop: '10px', padding: '10px', backgroundColor: '#3388ff', color: 'white', border: 'none', cursor: 'pointer' }}>
          Reset
        </button> */}
    </div>
        
    );
}