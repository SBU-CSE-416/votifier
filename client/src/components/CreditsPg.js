import "../stylesheets/credits.css"
import ProfileRound from "./ProfileRound"

export default function CreditPg() {
    return(
        <div style={{display:"flex", width:"100vw"}}>
            <div className="side-panel"></div>
            <div className="content-area">
                <div class="center">
                    <img id="huskies-logo-img" src="/huskies_logo.png" width="150px" height="180px" alt="huskies logo"></img>
                </div>
                <h3 class="center">Votifier - Developed by Team Huskies</h3>
                <ProfileRound></ProfileRound>
            </div>
            <div className="side-panel"></div>
        </div>
    );
}