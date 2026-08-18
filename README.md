# Avon

_Avon_ is personalised variant of the personal assistance chatbot Duke for the Software Engineering Course' individual project. 
It is built from the project template for a greenfield Java project. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Avon.java` file, right-click it, and choose `Run Avon.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
       ___                    
      /   |_   ______  ____  
     / /| | | / / __ \/ __ \ 
    / ___ | |/ / /_/ / / / / 
   /_/  |_|___/\____/_/ /_/
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Building the executable JAR

Run `./gradlew shadowJar` from the project root. The executable JAR is created at
`build/libs/avon.jar` and can be launched using:

```shell
java -jar build/libs/avon.jar
```

## Acknowledgement of AI Use

In line with course expectations, AI tools were used throughout the code base. Usage was around AI-5 level in general. 
The requirements of each increment were read, understood and broken down into small steps for the AI to implement. 
After each step, the software was tested, to ensure its behaviour conforms to requirements.
The generated code was also reviewed, and the AI tool would be questioned regarding certain implementation choices. 
If the justifications provided by the AI tool was not accepted, it would be asked to modify its implementation. 
It is hoped that this method of usage does not compromise learning, thus aligning with the course's goals.
