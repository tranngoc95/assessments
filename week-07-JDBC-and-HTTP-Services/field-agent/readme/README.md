# Assessment: Field Agent HTTP Service


## Requirements
- Create full HTTP CRUD for security clearance
- Create full HTTP CRUD for agent aliases
- Implement global error handling


## Features
### Security Clearance
- create and implement repository, service, and @RestController
- Should:
    - findAll
    - findByID
    - add
    - update
    - delete (only allow deletion if a security clearance key isn't referenced)
- Domain Rules:
    - Require: name
    - Not duplicate name
    
### Aliases
- Aliases aren't required. Some agents have many.
- Create implement classes and methods required to support aliases in the REST API
- Should
    - Fetch an individual agent with aliases attached
    - Add
    - Update
    - Delete
- Domain Rules:
    - Require: name
    - Not required Persona unless a name is duplicated
    
### Global Error Handling
- Use @ControllerAdvice
- Catch and handle exceptions at two levels
    - Precise exception for data integrity failures with specific message
    - Other exceptions: general response

    
## Technical Requirements
- Don't change database schema
- Use Spring
- Test both data and domain; controller test at least one HTTP endpoint
- Never run tests against production database


## Model
### Alias(int aliasID, String name, String persona, int agentID)  xx

### Agent
  - private List<Alias> aliases;  xx


## Data
### SecurityClearanceJdbcTemplateRepository   xx
- List<SecurityClearance> findAll()
- SecurityClearance findById(String clearanceId)
- boolean add(SecurityClearance clearance)
- boolean update(SecurityClearance clearance)
- boolean deleteById(int clearanceId)
  (only allow deletion if a security clearance key isn't referenced)
- boolean checkReferenceKey(int clearanceId)

### AliasMapper implements RowMapper<Alias>   xx
- Alias mapRow(ResultSet resultSet, int i)

### AliasJdbcTemplateRepository   xx
  - List<Alias> findAll()
  - Alias findById(int agentId)
  - boolean add(Alias alias)
  - boolean update(Alias alias)
  - boolean delete(Alias alias)

### AgentJdbcTemplateRepository   xx
  - void addAliases(Agent agent)
  - add calling above method in findById()

  
## Domain
### SecurityClearanceService    xx
- List<SecurityClearance> findAll()
- SecurityClearance findById(String clearanceId)
- Result<SecurityClearance> add(SecurityClearance clearance)
- Result<SecurityClearance> update(SecurityClearance clearance)
- Result<SecurityClearance> deleteById(int clearanceId)
  - call repository.checkReferenceKey(key) to validate
- Result<SecurityClearance> validate(SecurityClearance clearance)
  - Name not null or empty
  - No duplicate name

### AliasService  xx
- List<Alias> findAll()
- Alias findById(int agentId)
- Result<Alias> add(Alias alias)
- Result<Alias> update(Alias alias)
- boolean delete(Alias alias)
- Result<Alias> validate(Alias alias)
  - Name not null or empty
  - If name is duplicated -> Require Persona


## Controller
### SecurityClearanceController   xx
@RestController
@RequestMapping("/api/security-clearance")
- @GetMapping 
  - List<SecurityClearance> findAll()
- @GetMapping("/{clearanceId}") 
  - ResponseEntity<SecurityClearance> findById(@PathVariable String clearanceId)
- @PostMapping
  - ResponseEntity<SecurityClearance> add(@RequestBody SecurityClearance clearance)
  - return:
    - ResponseEntity<>(result.getPayload(), HttpStatus.CREATED)
    - ErrorResponse.build(result)
- @PutMapping("/{clearanceId}")
  - ResponseEntity<SecurityClearance> update(@RequestBody SecurityClearance clearance)
  - return:
    - ResponseEntity<>(HttpStatus.CONFLICT)
    - ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT)
    - ErrorResponse.build(result)
- @DeleteMapping("/{clearanceId}")
  - ResponseEntity<Void> deleteById(@PathVariable int clearanceId)
  - return: 
    - ResponseEntity<>(HttpStatus.NO_CONTENT)
    - ErrorResponse.build(result)

### AliasController   xx
@RestController
@RequestMapping("/api/alias")
  - @GetMapping
    - List<Alias> findAll()
  - @GetMapping("/{aliasId}")
    - ResponseEntity<Alias> findById(@PathVariable String aliasId)
  - @PostMapping
    - ResponseEntity<Alias> add(@RequestBody Alias alias)
    - return:
      - ResponseEntity<>(result.getPayload(), HttpStatus.CREATED)
      - ErrorResponse.build(result)
  - @PutMapping("/{aliasId}")
    - ResponseEntity<Alias> update(@PathVariable int aliasId, @RequestBody Alias alias)
    - return:
      - ResponseEntity<>(HttpStatus.CONFLICT)
      - ResponseEntity<>(result.getPayload(), HttpStatus.NO_CONTENT)
      - ErrorResponse.build(result)
  - @DeleteMapping("/{aliasId}")
    - ResponseEntity<Alias> delete(@PathVariable int aliasId)
    - return:
      - ResponseEntity<>(HttpStatus.NO_CONTENT)
      - ResponseEntity<>(HttpStatus.NOT_FOUND)
 
### GlobalExceptionHandler    xx
@ControllerAdvice
- @ExceptionHandler(DataAccessException.class)
  - ResponseEntity<ErrorResponse> handleException(DataAccessException ex)
- @ExceptionHandler(IllegalArgumentException.class)
  - ResponseEntity<ErrorResponse> handleException(IllegalArgumentException ex)
- @ExceptionHandler(Exception.class)
  - ResponseEntity<ErrorResponse> handleException(Exception ex)  


## Steps
- [x] Create full HTTP CRUD for Security Clearance
  - [x] Update Repository
  - [x] Update Repository Tests
  - [x] Create Service
  - [x] Test Service  
  - [x] Create Controller
  - [x] Test Controller
  - [x] Create security-clearance.http
- [x] Update Agent
  - [x] Update Model
  - [x] Update Repository
  - [x] Update Repository Tests
  - [x] Check http for findById with full aliases attached
- [x] Create full HTTP CRUD for agent aliases
  - [x] Create Model
  - [x] Create Mapper
  - [x] Create Repository
  - [x] Test Repository (+ create known_good_state for alias tests)
  - [x] Create Service
  - [x] Test Service
  - [x] Create Controller
  - [x] Test Controller
  - [x] Create alias.http
- [x] Implement global error handling
