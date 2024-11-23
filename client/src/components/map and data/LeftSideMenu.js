import { useState } from "react";
import "../../stylesheets/map and data/leftSideMenu.css";

export default function LeftSideMenu(props){
    let dataVisible = props.dataVisible;
    const [ isStatesOpen, setStatesOpen ] = useState(false);
    const [ selectedStates, setSelectedStates] = useState({
        maryland: false,
        southCarolina: false,
    });

    const toggleStateDropdown = () => {
        setStatesOpen(!isStatesOpen);
    };

    const handleStatesCheckChange = (event) => {
        setSelectedStates({
            ...selectedStates, 
            [event.target.name]: event.target.checked
        });
    };

    return (
        <div className="side-nav-container">
            {/* States Dropdown Menu*/}
            { dataVisible ? 
            (
            <div>
                <p onClick={toggleStateDropdown} className="dropdown-title">{ isStatesOpen ? "v States" : ">States"}</p>
                {isStatesOpen && (
                    <div className="dropdown-content">
                        <label>
                            <input
                                type="checkbox"
                                name="maryland"
                                checked={selectedStates.maryland}
                                onChange={handleStatesCheckChange}
                            />
                            Maryland
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                name="southCarolina"
                                checked={selectedStates.southCarolina}
                                onChange={handleStatesCheckChange}
                            />
                            South Carolina
                        </label>
                    </div>
                )}
            </div>
            ) : (
                <div>
                    <p>Select a state on the map.</p>
                </div>
            )
            }
        </div>
    );
}