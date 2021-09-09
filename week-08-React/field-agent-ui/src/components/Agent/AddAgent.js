import { useState } from "react"
import ErrorMessages from "../ErrorMessages";

function AddAgent({setDisplay, getList, URL}) {

    const emptyAgent = {
        "agentId": 0,
        "firstName": "",
        "middleName": "",
        "lastName": "",
        "dob": "",
        "heightInInches": ""
    }

    const [errorList, setErrorList] = useState([]);
    const [agent, setAgent] = useState(emptyAgent);

    const handleOnChange = (event) => {
        const newAgent = {...agent};
        newAgent[event.target.name] = event.target.value;
        setAgent(newAgent);
    };

    const addAgent = (event) => {

        event.preventDefault();

        const init = {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(agent)
        };

        fetch(URL, init)
            .then(response => {
                if(response.status !== 400 && response.status !== 201) {
                    return Promise.reject("Something went wrong. :(")
                }
                return response.json();
            })
            .then(data => {
                if(data.agentId) {
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
        <div className="container mt-5 w-50">
            <h3>Add Agent</h3>
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
                    <button className="btn btn-success" type="submit" onClick={addAgent}>Submit</button>
                    <button className="btn btn-warning ml-2" type="button" onClick={cancel}>Cancel</button>
                </div>  
            </form>
        </div>
    )
}

export default AddAgent;