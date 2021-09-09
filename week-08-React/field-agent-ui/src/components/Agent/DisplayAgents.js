import { useEffect, useState } from "react";
import AddAgent from './AddAgent';
import Agent from "./Agent";
import AgentsTable from "./AgentsTable";
import DeleteAgent from "./DeleteAgent";
import UpdateAgent from "./UpdateAgent";


function DisplayAgents() {

    const [agents, setAgents] = useState([]);
    const [display, setDisplay] = useState("view");
    const [agentId, setAgentId] = useState({});

    const URL = "http://localhost:8080/api/agent";

    const getList = () => {
        return fetch(URL)
                .then(response => {
                    if(response.status !== 200) {
                        return Promise.reject("Agents fetch failed.")
                    }
                    return response.json();
                    })
                .then(data => setAgents(data))
                .catch(error => console.log("Error", error));
    }
    
    useEffect(getList, []);

    const redirect = (agentId, action) => {
        setAgentId(agentId);
        setDisplay(action);
    }

    let result;
    switch (display) {
        case "add":
            result = <AddAgent setDisplay={setDisplay} getList={getList} URL={URL}/>;
            break;
        case "delete":
            result = <DeleteAgent agentId={agentId} setDisplay={setDisplay} getList={getList} URL={URL}/>;
            break;
        case "update":
            result = <UpdateAgent agents={agents} agentId={agentId} setDisplay={setDisplay} getList={getList} URL={URL}/>
            break;
        case "view-each":
            result = <Agent agentId={agentId} URL={URL} setDisplay={setDisplay}/>
            break;
        case "view":
        default:
            result = <AgentsTable redirect={redirect} agents={agents} setDisplay={setDisplay}/>;   
    }

    return (
        <>
        {result}
        </>
    );

}

export default DisplayAgents;