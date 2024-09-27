import '../stylesheets/navBar.css';
//top nav bar here
export default function NavBar() {
    return (
        <div className='nav-container'>
            <header className='navbar'>
                <div style={{display:"flex", alignItems:"center"}}>
                    <img id="sbu-logo-img" src="/sbu_round_logo.png" alt="sbu logo"></img>
                    <p id='home-btn' className='text-btn'>Votifier</p>
                </div>
                <div style={{display:"flex", justifyContent:"flex-end"}}>
                    <p id="map-btn" className='text-btn'>Map</p>
                    <p id="data-btn" className='text-btn'>Data</p>
                    <p id="credits-btn" className='text-btn'>Credits</p>
                </div>
            </header>
        </div>
    );
}