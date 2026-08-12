# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Sufficient programming experience (>10000 lines of code written).
* IDE and level of expertise: VSCode, familiar with it.

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.

## Project Instructions

Avon is a Shakespearen-themed chatbot. This theme is chosen by the user as part of customisation efforts. Instructions sent to the chatbot may contain the original, uncustomised messages. Please do not change the Shakespeare theme unless otherwise stated.

Additional instructions (if applicable)
- Strict Standard Adherence: Strictly follow the SE-EDU Java coding standard (basic and intermediate rules) and comply with SE-EDU Git conventions, particularly for commit message subjects.
- Effective OOP Structure: Demonstrate a reasonable use of OOP by utilizing inheritance and dividing code into specialized classes with clear responsibilities, such as Ui, Storage, Parser, Todo, and Deadline.
- Adherence to SLAP: Apply the Single Level of Abstraction Principle (SLAP) to ensure methods are not excessively long and to avoid deeply nested code.
- Code Neatness and Naming: Keep code neat by removing all commented-out code and ensuring variables and methods have clear, meaningful naming.
- Robust Error Handling: Use Exceptions to handle errors systematically rather than allowing the program to crash.
- Comprehensive Documentation: Provide Javadoc comments for at least half of the public classes and methods to aid developer understanding.
- Thoughtful Refactoring: Before refactoring existing code, investigate the original design rationale (applying Chesterton’s Fence Principle) through commit history and documentation.

## Environment workaround

If repository tools fail with:

`bubblewrap is unavailable: no system bwrap was found on PATH`

do not repeatedly retry `apply_patch`; the failure is in the sandbox runtime. Run repository commands with elevated execution:

```json
{
  "sandbox_permissions": "require_escalated"
}

If apply_patch still fails, apply edits using a standard unified diff piped to git apply. Inspect the diff with git diff --check afterward. This is an environment issue, not a repository or patch-content issue.
