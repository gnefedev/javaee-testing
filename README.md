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
 3. Create javaee-testing.properties file and add packageToScan property 
 4. Add this WAR to application EAR and enjoy.


## Capabilities in AGAINST_SERVER:
* Tests are standard JavaEE beans with injections, interceptions etc.
* New test instance creates for evert @Test (it's JUnit standard).
* Session lifecycle bounds to test class lifecycle. @Stateful beans will 
save state among test methods of one test class.
* @BeforeClass, @Before, @After, @AfterClass work as expected.
 

## Capabilities in OFFLINE:
1. CDI:
    * @Inject, @EJB
    * @Alternative, @Qualifier
    * @Stateful with @SessionScope and @RequestScope
    * @Interceptors (without changes of setParameters(), getContextData()
     and getTimer(), not controlled order of interceptors)
2. Persistence:
    * Supported: hsqldb
    * @PersistenceContext injection
    * @Transactional annotations
    * bean transaction managements (without setting transaction timeout)
     
## Customisation
Add javaee-testing.properties to you resources in WAR

1. packageToScan - package for scan you JEE beans
2. host - where to find deployed application default "localhost". 
3. port - default "8080".
4. context-root - context root of WAR with tests default "javaee-testing".
5. test-mode - AGAINST_SERVER or OFFLINE. Will choose by ping if not specified.

## Crazy things that not implemented in OFFLINE:
1. Two different instances of same @Stateful bean in one object
2. In JavaEE every module looks on own beans.xml. OFFLINE looks only on 
one in classpath.
