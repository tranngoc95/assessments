import { useState } from "react";
import Agent from "./Agent";
import ErrorMessages from "../ErrorMessages";

function DeleteAgent({agentId, setDisplay, getList, URL}){

    const [errorList, setErrorList] = useState([]);

    const deleteAgent = () => {
        const init = {
            method: "Delete"
        }

        fetch(URL + `/${agentId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Agent with id ${agentId} does not exist.`];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if(!data) {
                    getList();
                    setDisplay("view");
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));

    }

    const cancel = () => {
        setDisplay("view");
    };

    return (
        <div className="container mt-5">
            <h3>Delete Agent</h3>
            <ErrorMessages errorList={errorList}/>
            <Agent agentId={agentId} setDisplay={setDisplay} URL={URL}/>
            <hr/>
            <div>Are you sure you want to delete this agent?</div>
            <div className="mt-3">
                <button type="button" className="btn btn-success" onClick={deleteAgent}>Yes</button>
                <button type="button" className="btn btn-warning" onClick={cancel}>Cancel</button>
            </div>
        </div>
    )

}

export default DeleteAgent;