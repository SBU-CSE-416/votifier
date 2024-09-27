import { useState } from "react";
import NavBar from "./navBar.js";
import HomePg from "./homePg.js";

export default function Votifier() {
    const [pg, setPg] = useState('home');

    const checkState = () => {
        if (pg === 'home'){
            return (
                <section>
                    <NavBar></NavBar>
                    <HomePg></HomePg>
                </section>
            );
        }
    };

    return <>{checkState()}</>;
}