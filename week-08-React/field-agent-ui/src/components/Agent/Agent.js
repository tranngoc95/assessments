import { useState, useEffect } from "react";
import ErrorMessages from "../ErrorMessages";

function Agent({agentId, URL, setDisplay}) {

    const emptyAgent = {
        "agentId": 0,
        "firstName": "",
        "middleName": "",
        "lastName": "",
        "dob": "",
        "heightInInches": "",
        "agencies": [],
        "aliases": []
    }

    const [agent, setAgent] = useState(emptyAgent);
    const [errorList, setErrorList] = useState([]);

    useEffect(() => {
        fetch(URL + `/${agentId}`)
                .then(response => {
                    if (response.status === 200) {
                        return response.json();
                    } else if (response.status === 404) {
                        return [`Agent with id ${agentId.agentId} does not exist.`];
                    }
                    return Promise.reject("Something went wrong, sorry :(");
                })
                .then(data => {
                    console.log(data);
                    if(data.agentId){
                        setAgent(data);
                    } else {
                        setErrorList(data);
                    }
                })
                .catch(error => console.log("Error", error));
    },[]);

    const back = () => {
        setDisplay("view");
    }

	return (
        <div className="container mt-3">
            <button type="button" className="btn btn-dark mb-3" onClick={back}>Back</button>
            <ErrorMessages errorList={errorList}/>
            <h5>Agent Information</h5>
            <div>Id: {agent.agentId}</div>
            <div>First name: {agent.firstName}</div>
            <div>Middle name: {agent.firstName}</div>
            <div>Last name: {agent.lastName}</div>
            <div>Date of Birth: {agent.dob}</div>
            <div>Height in Inches: {agent.heightInInches}</div>
            <br/>
            {agent.agencies && agent.agencies.length !== 0 &&
            <div>
                <div>List of Agencies</div>
                <ul>
                {agent.agencies.map(a => (
                <li key={a.agency.agencyId}>
                    {a.agency.longName};
                </li>
                ))
                }
                </ul>             
            </div>
            }
            {agent.aliases && agent.aliases.length !== 0 &&
            <div>
                <div>List of Aliases</div>
                <ul>
                {agent.aliases.map(a => (
                <li key={a.aliasId}>
                    {a.name};
                </li>
                ))
                }
                </ul>             
            </div>
            }
        </div>
    )
}

export default Agent;

