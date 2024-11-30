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


export default function DataPg({ state }) {
  const [activeTab, setActiveTab] = useState("summary");
  // console.log("DataPg state:", state);

  // Ensure hous_INCOME_DIS exists
  // if (!state || !state.hous_INCOME_DIS) {
  //   return <div>Error: Missing or invalid data for state</div>;
  // }
  //Objects are not valid as a React child (found: object with keys {0_35K, 35K_60K, 60K-100K, 100K_125K, 125K_150K, 150K_MORE}). If you meant to render a collection of children, use an array instead.
  const incomeDistributionData = Object.entries(state.hous_INCOME_DIS).map(
    ([range, percentage]) => ({
      range: range.replace(/_/g, "-"), // Format the range
      percentage,                     // Use the percentage value
    })
  );
  console.log("Data: ", incomeDistributionData)

  return (
    <div id="info">
      <div className="datapg-container">
        <div className="data-genernal-info">
          <GeneralInfoBox state={state} />
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
              <HistogramChart
                data={incomeDistributionData}
                w={500}
                h={250}
                title={"Household Income Distribution"}
              />
            </div>
          )}
          {activeTab === "ensemble-data" && (
            <div className="data-charts">
              <MedianIncomeBoxPlot w={400} h={200} title={"Median Income Distribution"} />
            </div>
          )}
          {activeTab === "gingles" && (
            <div className="data-charts">
              <h2>Coming Soon</h2>
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
