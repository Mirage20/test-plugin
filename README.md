# Che Test Plugin (Prototype)

## Features
* Currently support JUnit *4.x* 
* Tested with che version *4.0.0-RC12-SNAPSHOT*
* Currently able to view failures and stack trace within the **Events** window. 
* Sample UI view
 

## Installation

1. Run `mvn clean install`
2. Add `che-plugin-java-ext-testing-client` extension as dependency to `/che/assembly-ide-war/pom.xml`
3. Add GWT inheritance to `/che/assembly-ide-war/src/main/resources/org/eclipse/che/ide/IDE.gwt.xml`
4. Add `che-plugin-java-ext-testing-server` extension as dependency to `/che/assembly-machine-war/pom.xml`
5. Create new che assembly with test extension: `mvn clean install -Denforcer.skip=true` in `/che/assembly` module
6. In `/che/assembly-main/target/eclipse-che-{version}/bin`, run `che run` to launch the new Che assembly with test extension

## Run JUnit Test's
 
1. Create a workspace in Che
2. Add **JUnit 4.xx** dependency to the `pom.xml`
2. Wait until the all dependencies get resolved
3. Navigate to your test class
4. In the IDE menu bar, goto **Test -> Run as JUnit Test...**
5. Wait for the test result.
