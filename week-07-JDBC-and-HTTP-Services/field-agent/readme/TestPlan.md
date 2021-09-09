
# Module 7 Code Reviews

## Process

_See [this document](../misc/code-reviews.md) for a complete breakdown of the code review process. The below information is specific to this assessment._

### Testing Locally

* Clone the trainee's repo to your local machine
* Drop any existing assessment databases from previous code reviews
* Using Workbench...
    * Run the script to create the production database
    * Run the script to create the test database
* Per the test plan, use the VS Code REST Client extension to send HTTP requests to the trainee's API endpoints
* Note the bugs that you find so you can summarize them in a top level pull request comment

## Requirements Checklist

* [ ] Find all security clearances
* [ ] Find a security clearance by id
* [ ] Add a security clearance
* [ ] Update a security clearance
* [ ] Delete a security clearance (challenge)
* [ ] Find agent with aliases
* [ ] Add an alias
* [ ] Update an alias
* [ ] Delete an alias
* [ ] Global Error Handling (correctly handles data and general errors differently)
* [ ] Test data components (all data components are tested with valuable tests)
* [ ] Test domain components (all domain components are tested with valuable tests)
* [ ] Java Idioms (excellent layering, class design, method responsibilities, and naming)

## Test Plan

_If the trainee followed instructions during kickoff, they should have an HTTP file with a good sequence of events for demonstrating CRUD capabilities._

### Security Clearance

* [ ] GET all security clearances
* [ ] GET a security clearance by ID
* [ ] For GET return a 404 if security clearance is not found
* [ ] POST a security clearance
* [ ] For POST return a 400 if the security clearance fails one of the domain rules
    * [ ] Security clearance name is required
    * [ ] Name cannot be duplicated
* [ ] PUT an existing security clearance
* [ ] For PUT return a 400 if the security clearance fails one of the domain rules
* [ ] DELETE a security clearance that is not in use by ID
* [ ] For DELETE return a 404 if the security clearance is not found
* [ ] For DELETE return a 400 if the security clearance is in use

### Alias

* [ ] GET an agent record with aliases attached
* [ ] POST an alias
* [ ] For POST return a 400 if the alias fails one of the domain rules
    * [ ] Name is required
    * [ ] Persona is not required unless a name is duplicated. The persona differentiates between duplicate names.
* [ ] PUT an alias
* [ ] For PUT return a 400 if the alias fails one of the domain rules
* [ ] DELETE an alias by ID
* [ ] For DELETE Return a 404 if the alias is not found

### Global Error Handling

* [ ] Return a specific data integrity error message for data integrity issues
* [ ] Return a general error message for issues other than data integrity