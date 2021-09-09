# Assessment M04: Sustainable Foraging

### Requirements
#### Items
- Require Name, Category, Dollars/Kg
- No duplicate name
- 0 <= Dollars/Kg <= 7500
- ID is system-generated unique sequential int

#### Foragers
- Require First name, Last name, State    x
- No duplicate combination of First name, Last name, State    x
- ID is system-generated GUID   x

#### Forages
- Require and must exist: Item, Forager
- Date: required and not in future
- 0 < Kilograms <= 250
- No duplicate combination of Date, Item, Forager
- ID is system-generated GUID

#### Technical Requirements
- Spring DI
- Kilogram/item report => use Stream
- Total value/category report => use loops & intermediate variables
- Financial math: BigDecimal
- Dates: LocalDate

### Steps
- Change to Spring Annotation DI  x
- Add validation for required Category in ItemService and add tests for that function   x
- Add validation for duplication in ForageService and add tests for that function   x
- Implement View Foragers By State    x
  - Add View Foragers in MainMenuOption   x
  - Add methods in Controller and View    x
- Implement Add Forager   x
  - Add "add" method in ForagerFileRepositories   x
  - Add "add" and "validation" method in ForagerService   x
  - Create tests for methods above    x
  - Add "makeForager" method in View    x
  - Add "addForager" method in Controller   x
- Implement Report Kilogram/Item    x
  - Add "reportKgPerItem" in ItemService which will retrieve, 
    calculate and return a Map<String, Double> which pair Item with its kg/item   x
  - Add "displayKgPerItem" method in Controller & View    x
- Implement Report Category Value in Controller & View    x
  - Add "reportCategoryValue" in ItemService which will retrieve, 
    calculate and return a Map<Category, BigDecimal> which pair Category with its value   x
  - Add "displayCategoryValue" method in Controller & View    x
  
### Methods
#### Controller
- void viewForagers()
- void addForager()
- void reportKgPerItem()
- void reportCategoryValue()

#### View
- void displayForagers(List<Foragers> foragers)
- Forager makeForager()
- void displayKgPerItem(Map<String, Double> report)
- void displayCategoryValue(Map<Category, BigDecimal> report)

#### ForagerFileRepositories
- Forager add(Forager forager)

#### ForagerService
- Result<Forager> validate(Forager forager)
- Result<Forager> add(Forager forager)

#### ItemService
- Map<String, Double> reportKgPerItem()
- Map<Category, BigDecimal> reportCategory()

#### ForageService
- Result<Forage> validateDuplication(Forage forage, Result<Forage> result)