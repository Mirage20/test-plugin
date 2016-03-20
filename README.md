# Che Test Plugin (Prototype)

## Features
* Currently support JUnit *4.x* 
* Tested with che version *4.0.0-RC12-SNAPSHOT*
* Currently able to view failures and stack trace within the **Events** window. 
* Sample UI view
* Run tests asynchronously
 
## Screen Shots
 
#### Sample view of UI with notification informing that test has been launched.
 ![](https://db.tt/iLb61yLx)
 
#### Event window of failed test case with stack trace.
 ![](https://db.tt/uLFot82r)


## Installation

1. Clone the repository using `git clone https://github.com/Mirage20/test-plugin`
2. Run `mvn clean install -Dcheckstyle.skip=true -Dfindbugs.skip=true -DskipTests=true -Dlicense.skip=true -Denforcer.skip=true -T 1C`
3. Add `che-plugin-java-ext-testing-client` and `che-plugin-java-ext-testing-shared` as dependencies to `/che/assembly-ide-war/pom.xml`
4. Add GWT inheritance to `/che/assembly-ide-war/src/main/resources/org/eclipse/che/ide/IDE.gwt.xml`
5. Add `che-plugin-java-ext-testing-server` extension as dependency to `/che/assembly-machine-war/pom.xml`
6. Create new che assembly with test extension: `mvn clean install -Denforcer.skip=true` in `/che/assembly` module
7. In `/che/assembly-main/target/eclipse-che-{version}/bin`, run `che run` to launch the new Che assembly with test extension

## Run JUnit Test's
 
1. Create a workspace in Che
2. Add **JUnit 4.xx** dependency to the `pom.xml`
2. Wait until the all dependencies get resolved
3. Navigate to your test class
4. In the IDE menu bar, goto **Test -> Run as JUnit Test...**
5. Wait for the test result.
