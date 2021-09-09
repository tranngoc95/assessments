import { useState } from "react"
import ErrorMessages from "../ErrorMessages";

function UpdateAgent({agents, agentId, setDisplay, getList, URL}) {

    const agentToUpdate = () => {
        return agents.find(a => a.agentId === agentId);
    }

    const [errorList, setErrorList] = useState([]);
    const [agent, setAgent] = useState(agentToUpdate);

    const handleOnChange = (event) => {
        const newAgent = {...agent};
        newAgent[event.target.name] = event.target.value;
        setAgent(newAgent);
    };

    const updateAgent = (event) => {

        event.preventDefault();

        const init = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(agent)
        };

        fetch(URL + `/${agent.agentId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Agent with id ${agentToUpdate.agentId} does not exist.`];
                } else if (response.status === 400) {
                    return response.json();
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (!data) {
                    getList();
                    setDisplay("view");
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log('Error:', error));
    }

    const cancel = () => {
        setDisplay("view");
    };

    return (
        <div className="container w-50">
            <h3 className="mt-5">Update Agent</h3>
            <ErrorMessages errorList={errorList}/>
            <form>
                <div className="form-group">
                    <label htmlFor="firstName">First Name</label>
                    <input className="form-control" id="firstName" name="firstName" value={agent.firstName} onChange={handleOnChange} type="text" required></input>
                </div>
                <div className="form-group">
                    <label htmlFor="middleName">Middle Name</label>
                    <input className="form-control" id="middleName" name="middleName" value={agent.middleName} onChange={handleOnChange} type="text"></input>
                </div>     
                <div className="form-group">
                    <label htmlFor="lastName">Last Name</label>
                    <input className="form-control" id="lastName" name="lastName" value={agent.lastName} onChange={handleOnChange} type="text" required></input>
                </div>
                <div className="form-group">
                    <label htmlFor="dob">Date of Birth</label>
                    <input className="form-control" id="dob" name="dob" value={agent.dob} onChange={handleOnChange} type="date" required></input>
                </div>
                <div className="form-group">
                    <label htmlFor="heightInInches">Heigh in Inches</label>
                    <input className="form-control" id="heightInInches" name="heightInInches" value={agent.heightInInches} onChange={handleOnChange} type="number" required></input>
                </div>
                <div className="mt-3">
                    <button className="btn btn-success" type="submit" onClick={updateAgent}>Submit</button>
                    <button className="btn btn-warning" type="button" onClick={cancel}>Cancel</button>
                </div>  
            </form>
        </div>
    )
}

export default UpdateAgent;