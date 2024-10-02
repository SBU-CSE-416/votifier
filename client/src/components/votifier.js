import { useState } from "react";
import NavBar from "./NavBar";
import LeftSideMenu from "./LeftSideMenu";
import HomePg from "./HomePg";
import MapPg from "./MapPg";
import CreditPg from "./CreditPg";


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
        // else if (pg === 'data') {
        //         <>
        //            <DataPg></DataPg>
        //         </>
        // }
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
            <div style={{display:"flex"}}>
                {pg !== "home" && pg !== "credits" ? (<LeftSideMenu></LeftSideMenu>) : null}
                {checkState()}
            </div>
        </section>
    );
}