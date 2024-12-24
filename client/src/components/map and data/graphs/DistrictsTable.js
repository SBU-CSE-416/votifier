import { useContext, useState, useEffect } from "react";
import { MapStoreContext } from "../../../stores/MapStore";
import { stateCodeMapping } from "../../../utilities/FederalInfomationProcessingStandardEnumUtil";
import "../../../stylesheets/map and data/graphs/DistrictsTable.css";
export default function DistrictsTable(){
    const { store } = useContext(MapStoreContext);
    const [districtsData, setDistrictsData] = useState([]);
    const [selectedDistrict2, setSelectedDistrict2] = useState(null);
    const API_URL = 'votifier-server-production.up.railway.app'
    useEffect(() => {
        const fetchData = async () => {
            var stateAbbreviation = stateCodeMapping[store.selectedStateCode];
            let response = await fetch_districts_summary(stateAbbreviation);
            //console.log("Districts Table data fetched: ", response);
            setDistrictsData(response);
        };
        fetchData();
    }, [store.selectedStateCode]);

    useEffect(() => {
        setSelectedDistrict2(store.selectedDistrict);
    }, [store.selectedDistrict]);

    const fetch_districts_summary = async (stateAbbreviation) => {
        try{
            const response = await fetch(`${API_URL}/api/data/${stateAbbreviation}/districts/summary`);
            const json = await response.json();
            //console.log("Districts Summary Data: ", json);
            return json;
        }catch(error){
            console.error(error.message);
        }
    }

    const handleRowClick = (district) => {
        store.setSelectedDistrict(district);
        setSelectedDistrict2(district);
    }

    return (
        <div className="districts-table-container">
            <table className="districts-table">
                <thead>
                    <tr className="districts-table-title">
                        <th>District</th>
                        <th>Representative</th>
                        <th>Party</th>
                        <th>Average Household Income</th>
                        <th>Democratic %</th>
                        <th>Republican %</th>
                        <th>% Below Poverty</th>
                        <th>Rural %</th>
                        <th>Suburban %</th>
                        <th>Urban %</th>
                    </tr>
                </thead>
                <tbody>
                    {districtsData.data?.map((district, index) => (
                        <tr 
                            key={index}
                            className={selectedDistrict2 === district.CONG_DIST ? "highlighted" : ""}
                            onClick={() => handleRowClick(district.CONG_DIST)}
                        >
                            <td>{district.CONG_DIST}</td>
                            <td>{district.REPRESENTATIVE}</td>
                            <td>{district.PARTY}</td>
                            <td>{district.AVERAGE_HOUSEHOLD_INCOME.toLocaleString('en-US', { style: 'currency', currency: 'USD', minimumFractionDigits: 0, maximumFractionDigits: 0 })}</td>
                            <td>{district.DEM_PERCENT}%</td>
                            <td>{district.REP_PERCENT}%</td>
                            <td>{district.PERCENT_BELOW_POVERTY}%</td>
                            <td>{district.RURAL_PERCENT}%</td>
                            <td>{district.SUBURBAN_PERCENT}%</td>
                            <td>{district.URBAN_PERCENT}%</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}