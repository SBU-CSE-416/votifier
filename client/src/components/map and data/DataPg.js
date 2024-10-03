import RacialBarChart from "./RacialChart";
import HistogramChart from "./HistogramChart";
import "../../stylesheets/dataPg.css"
export default function DataPg({resetMapViewToDefault}) {
    return(
        
    <div id="info" style={{ height: '600px', width: '35%', padding: '10px', fontSize: '18px' }}>
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
            <div className="charts">
                <HistogramChart w = {300} h = {150}></HistogramChart>
                <RacialBarChart w = {300} h = {150}></RacialBarChart>
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