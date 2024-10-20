import "../stylesheets/home.css";

export default function HomePg() {
    return (
        <div>
            <div className="container">
                <div className="banner-background">
                    <img src="/voting-booth-feat.jpg" width="1300" alt="Banner Picture"></img>
                </div>
            </div>
            <h3 className="centered-title-text">Votifier</h3>
            <h4 className="centered-para-text">Fair redistricting with mathematical optimization modeling and data analysis</h4>
        </div>
    );
}