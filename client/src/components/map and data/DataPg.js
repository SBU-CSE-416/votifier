import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import EcologicalInferenceChart from "./EcologicalInferenceChart"
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
            <div className="charts">
                {activeTab === "demographics" && (
                  <div className="charts">
                    <HistogramChart w = {350} h = {175}></HistogramChart>
                    <RacialBarChart w = {350} h = {175}></RacialBarChart>
                  </div>
                )}
                {activeTab === "racial" && (
                  <div className="charts">
                    <RacialBarChart w={300} h={150} />
                  </div>
                )}
                {activeTab === "economic" && (
                  <div className="charts">
                    <EcologicalInferenceChart w={300} h={150} />
                  </div>
        )}
                    
                
            </div>
            <div className="data-genernal-info"> 
            <p> General data here</p>
            
            </div>
            
        </div>
        {/* <button onClick={resetMapViewToDefault} style={{ marginTop: '10px', padding: '10px', backgroundColor: '#3388ff', color: 'white', border: 'none', cursor: 'pointer' }}>
          Reset
        </button> */}
    </div>
        
    );
}