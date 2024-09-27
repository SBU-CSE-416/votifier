import '../stylesheets/navBar.css';
//top nav bar here
export default function NavBar() {
    return (
        <div className='nav-container'>
            <header className='navbar'>
                <img id="sbu-logo-img" src="/sbu_round_logo.png" alt="sbu logo"></img>
                <p>Votifier</p>
                <p>Map</p>
                <p>Data</p>
                <p>Credits</p>
            </header>
        </div>
    );
}