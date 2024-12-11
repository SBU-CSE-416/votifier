import RacialBarChart from "./graphs/RacialChart";
import HistogramChart from "./graphs/StateSummaryBarChart";
import StateSummaryBarChart from "./graphs/StateSummaryBarChart";
import EcologicalInferenceChart from "./graphs/EcologicalInferenceChart"
import VotingGraph from "./graphs/VotingGraph";
import MedianIncomeBoxPlot from "./graphs/MedianIncomeBoxPlot"
import IncomeRangeDensityChart from "./graphs/IncomeRangeDensityChart"
import SummaryTable from "./SummaryTable"
import GinglesGraph from "./graphs/GinglesGraph";
import "../../stylesheets/map and data/DataPg.css";
import { useState } from "react";


export default function DataPg({ stateSummaryData }) {
  const [activeTab, setActiveTab] = useState("summary");
  const incomeDistributionData = Object.entries(stateSummaryData.house_HOLD_INCOME_DISTRIBUTION).map(
    ([range, percentage]) => ({
      range: range.replace(/_/g, "-"), 
      percentage,                    
    })
  );

  return (
    <div id="info">
      <div className="datapg-container">
        <div className="data-genernal-info">
          <SummaryTable stateSummaryData={stateSummaryData} />
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
            Gingles Analysis
          </div>
          <div
            className={`tab ${activeTab === "ei-analysis" ? "active" : ""}`}
            onClick={() => setActiveTab("ei-analysis")}
          >
            EI Analysis
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
              <StateSummaryBarChart
                data={incomeDistributionData}
                w={500}
                h={250}
                title={"Household Income Distribution"}
              />
            </div>
          )}
          {activeTab === "ensemble-data" && (
            <div className="data-charts">
              {/* <MedianIncomeBoxPlot w={400} h={200} title={"Median Income Distribution"} /> */}
            </div>
          )}
          {activeTab === "gingles" && (
            <div className="data-charts">
              <GinglesGraph/>
            </div>
          )}
          {activeTab === "ei-analysis" && (
            <div className="data-charts">
              <h2>Coming Soon</h2>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
