import "../stylesheets/credits.css"

export default function CreditPg() {
    return(
        <div style={{display:"flex", width:"100vw"}}>
            <div className="side-panel"></div>
            <div className="content-area">
                <p>Hello from CreditsPg</p>
            </div>
            <div className="side-panel"></div>
        </div>
    );
}