import '../stylesheets/navBar.css';

export default function NavBar() {
    return (
        <div className='nav-container'>
            <header className='navbar'>
                <div style={{display:"flex", alignItems:"center"}}>
                    <img id="sbu-logo-img" src="/sbu_round_logo.png" alt="sbu logo"></img>
                    <p id='home-btn' className='text-btn' >Votifier</p>
                </div>
            </header>
        </div>
    );
}