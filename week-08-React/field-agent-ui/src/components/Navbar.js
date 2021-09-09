import './Navbar.css';
import { useState } from "react";
import DisplayAgencies from "./Agency/DisplayAgencies";
import DisplayAgents from "./Agent/DisplayAgents"
import Home from "./Home";

function Navbar() {

    const [display, setDisplay] = useState("home");

    const navigate = (event) => {
        setDisplay(event.target.id);
    }

    let result;

    switch (display) {
        case "agent":
            result = <DisplayAgents/>;
            break;
        case "agency":
            result = <DisplayAgencies />
            break;
        // case "mission":
        //     break;
        // case "location":
        //     break;
        // case "alias":
        //     break;
        case "home":
        default:
            result = <Home />
            break;
            
    }

    return (
        <>
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <a id="home" class="navbar-brand" onClick={navigate}>AgentField</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
                    <div class="navbar-nav">
                    <a id="agent" class="nav-link" onClick={navigate}>Agent</a>
                    <a id="agency" class="nav-link" onClick={navigate}>Agency</a>
                    <a id="mission" class="nav-link" onClick={navigate}>Mission</a>
                    <a id="location" class="nav-link" onClick={navigate}>Location</a>
                    <a id="alias" class="nav-link" onClick={navigate}>Alias</a>
                    </div>
                </div>
            </nav>
            {result}
        </>
    )
}

export default Navbar;