# Test framework for JavaEE applications  
This framework has two modes:


AGAINST_SERVER - packaging tests in WAR with javaee-testing dependency
and deployed alongside with application. If run tests from local machine
framework find test on server, run it and return result. 


OFFLINE - JavaEE container mocked by Spring. Tests run immediately without 
deployment.


Test mode chooses by test-mode property in javaee-testing.properties or 
by ping of deployed application (OFFLINE if fail).


## Start guide
 1. Create WAR artifact, add dependency for javaee-testing.
 2. Create test in main directory, add annotations:
 `@RequestScoped`
 `@Stateful`
 `@RunWith(JavaeeTestRunner.class)`
 3. Add this WAR to application EAR and enjoy.


## Capabilities in AGAINST_SERVER:
* Tests are standard JavaEE beans with injections, interceptions etc.
* New test instance creates for evert @Test (it's JUnit standard).
* Session lifecycle bounds to test class lifecycle. @Stateful beans will 
save state among test methods of one test class.
* @BeforeClass, @Before, @After, @AfterClass work as expected.
 

## Capabilities in OFFLINE:
1. CDI:
    * @Inject
    * @Stateful with @SessionScope and @RequestScope
    * @Interceptors (without changes of setParameters(), getContextData()
     and getTimer()
     
## Customisation
Add javaee-testing.properties to you resources in WAR

1. host - where to find deployed application default "localhost". 
2. port - default "8080".
3. context-root - context root of WAR with tests default "javaee-testing".
4. test-mode - AGAINST_SERVER or OFFLINE. Will choose by ping if not specified.