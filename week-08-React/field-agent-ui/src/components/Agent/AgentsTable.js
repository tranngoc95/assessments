
function AgentsTable({agents, setDisplay, redirect}) {

    const handleAddClick = () => {
        setDisplay("add");
    };

    const handleClick = (action, agentId) => {
        redirect(agentId, action);
	};

    return (
        <div className="container mt-5">
            <h2>Agents List</h2>
            <button className="btn btn-primary mt-2 mb-2" onClick={handleAddClick}>Add New Agent</button>
                
            <table className="table table-hover">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Date of Birth</th>
                        <th>Height in Inches</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {agents.map(agent => (
                        <tr key={agent.agentId}>
                            <td>{agent.agentId}</td>
                            <td>{agent.firstName}</td>
                            <td>{agent.lastName}</td>
                            <td>{agent.dob}</td>
                            <td>{agent.heightInInches}</td>
                            <td className="btn-group">
                                <button className="btn btn-outline-info" onClick={() => {handleClick("view-each", agent.agentId)}}>View</button>
                                <button className="btn btn-outline-secondary" onClick={() => {handleClick("update", agent.agentId)}}>Update</button>
                                <button className="btn btn-outline-danger" onClick={() => {handleClick("delete", agent.agentId)}} >Delete</button>
                            </td>
                        </tr>
                    ))                   
                    }
                </tbody>
            </table>
        </div>
    );
}

export default AgentsTable;