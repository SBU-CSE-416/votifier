import React from "react";

export default function GinglesGraph() {

    const fetch_gingles_racial = async (stateAbbreviation, racialGroup) => {
        try{
            const response = await fetch(`http://localhost:8000/${stateAbbreviation}/gingles/demographics/${racialGroup}`);
            const data = await response.data;
            console.log("Gingles racial data:",data);
        } catch (error){
            console.error(error.message);
        }
    };

    const fetch_gingles_economic = async (stateAbbreviation) => {
        try{
            const response = await fetch(`http://localhost:8000/${stateAbbreviation}/gingles/economic`);
            const data = await response.data;
            console.log("Gingles income data:",data);
        } catch (error){
            console.error(error.message);
        }
    }

    const fetch_gingles_economic_by_region = async (stateAbbreviation, regionType) => {
        try{
            const response = await fetch(`http://localhost:8000/${stateAbbreviation}/gingles/economic/${regionType}`);
            const data = await response.data;
            console.log("Gingles income by region data:",data);
        } catch (error){
            console.error(error.message);
        }
    }

    return(
        <div>
            <h2>Coming sooon</h2>
        </div>
    );
}