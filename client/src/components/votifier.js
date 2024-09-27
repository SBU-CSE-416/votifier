import { useState } from "react";
import NavBar from "./navBar";
import HomePg from "./homePg";
import MapPg from "./mapPg";
import DataPg from "./dataPg";
import CreditPg from "./creditPg";

export default function Votifier() {
    const [pg, setPage] = useState('home');

    const checkState = () => {
        if (pg === 'home'){
            return (
                <section>
                    <NavBar setPage={setPage}></NavBar>
                    <HomePg></HomePg>
                </section>
            );
        }
        else if (pg === 'map'){
            return(
                <section>
                    <NavBar setPage={setPage}></NavBar>
                    <MapPg></MapPg>
                </section>
            );
        }
        else if (pg === 'data'){
            return(
                <section>
                    <NavBar setPage={setPage}></NavBar>
                    <DataPg></DataPg>
                </section>
            );
        }
        else if (pg === 'credits'){
            return(
                <section>
                    <NavBar setPage={setPage}></NavBar>
                    <CreditPg></CreditPg>
                </section>
            );
        }
    };

    return <>{checkState()}</>;
}