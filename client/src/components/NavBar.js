import '../stylesheets/navBar.css';
//top nav bar here
export default function NavBar(props) {

    const goHome = () => {
        props.setPage("home");
    };

    const toMap = () => {
        props.setPage("map");
    };

    // const toData = () => {
    //     props.setPage("data");
    // };

    const toCredits = () => {
        props.setPage("credits");
    };

    return (
        <div className='nav-container'>
            <header className='navbar'>
                <div style={{display:"flex", alignItems:"center"}}>
                    <img id="sbu-logo-img" src="/sbu_round_logo.png" onClick={goHome} alt="sbu logo"></img>
                    <p id='home-btn' className='text-btn' onClick={goHome}>Votifier</p>
                </div>
                <div style={{display:"flex", justifyContent:"flex-end"}}>
                    <p id="map-btn" className='text-btn' onClick={toMap}>Map</p>
                    {/* <p id="data-btn" className='text-btn' onClick={toData}>Data</p> */}
                    <p id="credits-btn" className='text-btn' onClick={toCredits}>Credits</p>
                </div>
            </header>
        </div>
    );
}