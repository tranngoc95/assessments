import { useEffect, useState } from "react";
import AgenciesTable from "./AgenciesTable";


function DisplayAgencies() {

    const [agencies, setAgencies] = useState([]);
    const [display, setDisplay] = useState("view");

    const URL = "http://localhost:8080/api/agency";

    const getList = () => {
        return fetch(URL)
                .then(response => {
                    if(response.status !== 200) {
                        return Promise.reject("Agencies fetch failed.")
                    }
                    return response.json();
                    })
                .then(data => setAgencies(data))
                .catch(error => console.log("Error", error));
    }
    
    useEffect(getList, []);

    let result;
    switch (display) {
        case "view":
        default:
            result = <AgenciesTable agencies={agencies} setDisplay={setDisplay}/>;   
    }

    return (
        <>
        {result}
        </>
    );

}

export default DisplayAgencies;