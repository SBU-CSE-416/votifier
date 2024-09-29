import { useState } from "react";
import "../stylesheets/leftSideMenu.css";

export default function LeftSideMenu(){
    const [ isStatesOpen, setStatesOpen ] = useState(false);
    const [ selectedStates, setSelectedStates] = useState({
        maryland: false,
        southCarolina: false,
    });

    const [ isGraphsOpen, setGraphsOpen ] = useState(false);
    const [ selectedGraphs, setSelectedGraphs ] = useState({
        bar: false,
        histogram: false,
    });

    const [ isDemographicsOpen, setDemographicsOpen ] = useState(false);
    const [ selectedDemographics, setSelectedDemographics ] = useState({
        income: false,
        race: false,
        age: false,
    });

    const toggleStateDropdown = () => {
        setStatesOpen(!isStatesOpen);
    };

    const toggleGraphsDropdown = () => {
        setGraphsOpen(!isGraphsOpen);
    };

    const toggleDemographicsDropdown = () => {
        setDemographicsOpen(!isDemographicsOpen);
    };

    const handleStatesCheckChange = (event) => {
        setSelectedStates({
            ...selectedStates, 
            [event.target.name]: event.target.checked
        });
    };

    const handleGraphsCheckChange = (event) => {
        setSelectedGraphs({
            ...selectedGraphs, 
            [event.target.name]: event.target.checked
        });
    };

    const handleDemographicsCheckChange = (event) => {
        setSelectedDemographics({
            ...selectedDemographics, 
            [event.target.name]: event.target.checked
        });
    };

    return (
        <div className="side-nav-container">
            {/* States Dropdown */}
            <div className="dropdown-category">
                <p onClick={toggleStateDropdown} className="dropdown-title">{ isStatesOpen ? "V States" : "> States"}</p>
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

            {/* Graphs Dropdown */}
            <div className="dropdown-category">
                <p onClick={toggleGraphsDropdown} className="dropdown-title">{ isGraphsOpen ? "V Graphs" : ">Graphs"}</p>
                {isGraphsOpen && (
                    <div className="dropdown-content">
                        <label>
                            <input
                                type="checkbox"
                                name="bar"
                                checked={selectedGraphs.bar}
                                onChange={handleGraphsCheckChange}
                            />
                            Bar
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                name="histogram"
                                checked={selectedGraphs.histogram}
                                onChange={handleGraphsCheckChange}
                            />
                            Histogram
                        </label>
                    </div>
                )}
            </div>

            {/* Demographics Dropdown */}
            <div className="dropdown-category">
                <p onClick={toggleDemographicsDropdown} className="dropdown-title">{isDemographicsOpen ? "V Demographics" : ">Demographics"}</p>
                {isDemographicsOpen && (
                    <div className="dropdown-content">
                        <label>
                            <input
                                type="checkbox"
                                name="income"
                                checked={selectedDemographics.income}
                                onChange={handleDemographicsCheckChange}
                            />
                            Income
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                name="race"
                                checked={selectedDemographics.race}
                                onChange={handleDemographicsCheckChange}
                            />
                            Race
                        </label>
                        <label>
                            <input
                                type="checkbox"
                                name="age"
                                checked={selectedDemographics.age}
                                onChange={handleDemographicsCheckChange}
                            />
                            Age
                        </label>
                    </div>
                )}
            </div>
        </div>
    );
}