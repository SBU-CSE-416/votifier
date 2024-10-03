import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import "../stylesheets/dataPg.css"
export default function DataPg() {
    return(
        <div className="datapg-container">
            <div className="charts">
                <HistogramChart w = {300} h = {150}></HistogramChart>
                <RacialBarChart w = {300} h = {150}></RacialBarChart>
            </div>
            <div className="data-genernal-info"> 
                <p> General data here</p>
            
            </div>
            
        </div>
    );
}