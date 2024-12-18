import "../../stylesheets/map and data/DataPg.css";
import { useState, useContext, useEffect } from "react";
import { MapStoreContext } from "../../stores/MapStore";
import StateSummaryBarChart from "./graphs/StateSummaryBarChart";
import EcologicalInferenceGraph from "./graphs/EcologicalInferenceGraph";
import SummaryTable from "./SummaryTable"
import GinglesGraph from "./graphs/GinglesGraph";
import DistrictsTable from "./graphs/DistrictsTable";
import EnsemblePlansBoxWhisker from "./graphs/EnsemblePlansBoxWhisker";
import { use } from "react";

export default function DataPg({ stateSummaryData }) {
  const { store } = useContext(MapStoreContext);
  const [activeTab, setActiveTab] = useState("summary");
  useEffect(() => {
    if(activeTab!=="summary" || store.firstTabView !== "districtsTable"){
      store.setSelectedDistrict(null);
    }
  }, [store.firstTabView, activeTab]);
  useEffect(() => {
    if(store.selectedDistrict!==null){
      setActiveTab("summary");
      store.setFirstTabView("districtsTable");
    }
  }, [store.selectedDistrict]);

  let incomeDistributionData;
  if(stateSummaryData!==null){
    incomeDistributionData = Object.entries(stateSummaryData.house_HOLD_INCOME_DISTRIBUTION).map(
      ([range, percentage]) => ({
        range: range.replace(/_/g, "-"), 
        percentage,                    
      })
    );
    console.log("Income Distribution Data: ", incomeDistributionData);
  }

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
            {store.firstTabView === "summary" ? "Summary" : "Districts Table"}
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
              <label>
                <span>Selected View</span>
              </label>
              <select
                value={store.firstTabView}
                onChange={(event) => store.setFirstTabView(event.target.value)}
                style={{width:"30%"}}
              >
                <option value="summary">Summary</option>
                <option value="districtsTable">Districts Table</option>
              </select>
              {store.firstTabView === "summary" ? 
                <StateSummaryBarChart
                  data={incomeDistributionData}
                  title={"Household Income Distribution"}
                /> : <DistrictsTable/>
              }
            </div>
          )}
          {activeTab === "ensemble-data" && (
            <div className="data-charts">
              <EnsemblePlansBoxWhisker/>
            </div>
          )}
          {activeTab === "gingles" && (
            <div className="data-charts">
              <GinglesGraph/>
            </div>
          )}
          {activeTab === "ei-analysis" && (
            <div className="data-charts">
              <EcologicalInferenceGraph/>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
