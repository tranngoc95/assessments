# Assessment 08: React Field Agent

## Requirements
### High-Level
- Implement a full CRUD UI for agents
    - Display all agents
    - Add an agent
    - Update an agent
    - Delete an agent

### Technical
- Use Create React App
- Use fetch for async HTTP
- Not allowed to change Field Agent HTTP Service or database
- Use a CSS framework

## Features
### Navigation
- What the user see when the UI first render

### Display All Agents
- Short, easy to read format
- Don't include all agent properties
- Each "record" should include a UI element (button, link, etc) to trigger an edit or delete action
- Optional: include a way to expand all agent data in read-only format

### Add An Agent
- Available as a menu option, button, or link in navigation or Display All view
- Form submission creates a new agent and navigates to Display All Agents view
- User may cancel and return to the Display All Agents view

### Update An Agent
- HTML form should be prepopulated with the existing agent
- Form submission creates a new agent and navigates to Display All Agents view
- User may cancel and return to the Display All Agents view
- If time allows, show related data: aliases, missions, and agencies, but don't start with them.

### Delete An Agent
- Display an agent summary
- Allow user delete or cancel then return to Display All Agents view

## Components
### Navigation
- Navbar and return switch case to DisplayAgents, DisplayAgencies, DisplayMissions, DisplayLocations, DisplayAliases.

### DisplayAgents
- Fetch all agents from back-end service
- Display table of agents using Agent component with Add button which leads to AddAgent component
- Functions: handleAddClick(), addAgent(), updateAgent(), deleteAgent() which will fetch async HTTP and display success messages or errors

### Agent
- Display a single agent data
- Edit and Delete buttons which lead to corresponding components (UpdateAgent, DeleteAgent)
- Able to toggle to display buttons
- Function: handleUpdateClick(), handleDeleteClick()

### AddAgent
- HTML form with Submit and Cancel button
- Function: handleOnChange(), handleSubmit(), handleCancel()

### UpdateAgent
- HTML form with Submit and Cancel button
- Function: handleOnChange(), handleSubmit(), handleCancel()

### DeleteAgent
- Display a single agent by calling component Agent
- Ask user to confirm delete
- Submit and Cancel button
- Function: handleSubmit(), handleCancel()

## Steps
- [x] Create a new React project with CRA
- [x] Create Agent
- [x] Create DisplayAgents
- [x] Create AddAgent
- [x] Create DeleteAgent
- [x] Create UpdateAgent
- [x] Create Navigation
- [x] Import and use Bootstrap for styling components
