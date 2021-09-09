# Assessment: Field Agent HTTP Service

* [x] Walk through the instructions and requirements
* [x] Review the test plan: https://github.com/dev10-program/cohort-9/blob/main/resources/test-plans/M07-code-reviews.md
* [x] Create a repo for your assessment
    * [x] After creating your repo, add the Field Agent project in a standalone commit before you make any changes to it
    * [x] If you're up for it, you can also make a code-review branch at this point

* Gotchas...
    * Updating known good state
    * Using the expected column names between database column names and row mapper names
    * If you do this correctly, you don't need to rename a bunch of stuff
    * Correctly handling the agent with aliases requirement
    * Correctly handling the validation to prevent in-use security clearances from being deleted
* Submit your plan to your code reviewer this afternoon

* Planning
  * Plan before you write any code
  * Questions your plan should answer...
    * What are the requirements for the project?
      * A: Create full HTTP CRUD for security clearance, create full HTTP CRUD for agent aliases, Implement global error handling
    * Are there any requirements that I need to get clarification on?
      * A: No
    * Do I have to do any research?
      * A: Maybe if it comes up during coding?
    * Are there any unknowns? What do I need to do to get clarity?
      * A: No
    * What are my primary tasks?
      * A: crete full http crud for security clearance and agent aliases, implement global exception
    * How long do I estimate each of those tasks will take?
      * A: Several hours for each full crud and maybe one hour for global exception?
    * Are there any dependencies between tasks? What order do I need to complete the tasks in?
      * A: Order to complete task is security clearance, alias, and global exception
    * How will I review the provided database?
      * A: in sql files
    * How will I review the provided code?
      * A: in field-agent project provided in guide link
    * What classes and methods do I need to add to the project?
      * A: SecurityClearance and Aliases with specific methods listed in README
    * What REST API endpoints do I need to define?
      * A: listed in README
    * What are the HTTP methods and paths for each endpoint?
      * A: listed in README
    * What HTTP response status codes and content will each endpoint return to clients?
      * A: listed in README
    * What HTTP requests do I need to write to facilitate manual testing of the application?
      * A: get, post, put, delete
    * What will my strategy be for deleting security clearances?
      * A: will not delete if there is reference
    * How will I fetch an individual agent with their aliases attached?
      * A: Add a list of Aliases in Agent model
    * How will I implement global error handling?
      * A: Add a new class GlobalExceptionHandler
    * How will I determine the most precise exception to handle when data integrity failures occur?
      * A: listed in README
    * What unit tests do I need to write to fully test my new classes?
      * A: tests for all repositories, services, and controller
    * What will I do to ensure that my unit tests never run against the production database?
      * A: have test database and add that to application.properties in test directory
    * Do I need to do anything to establish a known good state for my repository unit tests?
      * A: yes have to add known good state data for aliases
    * Will I use test doubles or mocking when testing my service classes?
      * A: Mocking
    * What are my plans for controller testing?
      * A: mockingmvc with autowired service and mocking repository
    * If I have time, what stretch goals will I implement?
      * A: Implement full HTTP CRUD for mission
  * Write out a list of tasks
    * Estimate each task
    * Track how long each task actually takes 
    * Optionally use Trello or GitHub issues to manage your project
  * Acceptable planning document formats
    * Markdown file in your project
    * Google Doc
    * Word Doc