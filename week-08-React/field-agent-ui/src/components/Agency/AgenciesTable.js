function AgenciesTable({agencies}) {

    return (
        <div className="container mt-5">
            <h2>Agencies List</h2>   
            <table className="table table-hover">
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Short Name</th>
                        <th>Long Name</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {agencies.map(agency => (
                        <tr key={agency.agencyId}>
                            <td>{agency.agencyId}</td>
                            <td>{agency.shortName}</td>
                            <td>{agency.longName}</td>
                        </tr>
                    ))                   
                    }
                </tbody>
            </table>
        </div>
    );
}

export default AgenciesTable;