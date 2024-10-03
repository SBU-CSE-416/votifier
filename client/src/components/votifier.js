import { useState } from "react";
import NavBar from "./NavBar";
import LeftSideMenu from "./leftSideMenu";
import HomePg from "./homePg";
import MapPg from "./mapPg";
import DataPg from "./dataPg";
import CreditPg from "./creditPg";
import '../stylesheets/App.css';


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
        } else if (pg === 'data') {
            return(
                <>
                   <DataPg></DataPg>
                </>
            )
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
        <section style={{marginTop: '50px' }}>
            <NavBar setPage={setPage}></NavBar>
            <div className="page-container" style={{display:"flex"}}>
                {pg !== "home" && pg !== "credits" ? (<LeftSideMenu></LeftSideMenu>) : null}
                {checkState()}
            </div>
        </section>
    );
}