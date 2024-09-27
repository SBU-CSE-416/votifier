import { useState } from "react";
import NavBar from "./navBar";
import LeftSideMenu from "./leftSideMenu";
import HomePg from "./homePg";
import MapPg from "./mapPg";
import DataPg from "./dataPg";
import CreditPg from "./creditPg";

export default function Votifier() {
    const [pg, setPage] = useState('home');

    const checkState = () => {
        if (pg === 'home'){
            return (
                <>
                    <HomePg></HomePg>
                </>
            );
        }
        else if (pg === 'map'){
            return(
                <>
                    <MapPg></MapPg>
                </>
            );
        }
        else if (pg === 'data'){
            return(
                <>
                    <DataPg></DataPg>
                </>
            );
        }
        else if (pg === 'credits'){
            return(
                <>
                    <CreditPg></CreditPg>
                </>
            );
        }
    };

    return (
        <section>
            <NavBar setPage={setPage}></NavBar>
            <div style={{display:"flex"}}>
                <LeftSideMenu></LeftSideMenu>
                {checkState()}
            </div>
        </section>
    );
}