# Assessment M03: Solar Farm

### Requirements
- Display all solar panels
- Add, update, remove a solar panel
- Data is stored and doesn't change when stopping or starting application
- If data entered include delimiter => not break data
- Repositories throw custom exception
- Repositories and service class are fully tested with positive and negative cases
- Solar panel material is Enum of five values

### State
solar-panels.csv
solar-panels-seed.csv
solar-panels-test.csv

### models
SolarPanel(int ID, String section, int row, int col, int yearInstalled, MaterialType material, boolean isTracking)
enum MaterialType

#### Rules
- section: required && not blank
- row: 0<row<=250
- col: 0<col<=250
- yearInstalled: <=2021
- material: required && in MaterialType
- isTracking: required
- combined values of section, row, and col can't be duplicate

### Data
DataAccessException

SolarRepositories
- add
- update
- delete
- findAll
- findBySection
- findOne  
- optional:
  - findByYearB
  - findByMaterial

### Domain
SolarService
- add
- update
- delete
- findAll
- findBySection
- validation

SolarResult

### UI
ConsoleIO
- readInt
- readString
- readMaterialType

View
- printHeader
- printMenuOption
- printSolarPanels
- printResult
- createSolarPanel
- updateSolarPanel
- findSolarPanel

Controller(service, view)
- run
- methods for each menu option

### Steps
- Start Up/ Main Menu
  - Call view to print out Menu Option
  - Call view to get option input
- Find Panels by Section
  - Call view to get section input
  - Call service to fetch data from input section
  - Call view to print out data
- Add a Panel
  - Call view to get and create a panel with all input data
  - Call service to add new panel to database
  - Call view to print out result
- Update a Panel
  - Call view to get a panel input to update and get all update data
  - Call service to update panel in database
  - Call view to print out result
- Remove a Panel
  - Call view to get a panel input to delete 
  - Call service to delete panel in database
  - Call view to print out result 