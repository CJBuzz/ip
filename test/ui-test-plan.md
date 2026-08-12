# UI Test Plan

Run these cases with the project-specific `test-ui` skill. Each case starts Avon in a fresh JVM and compares the complete console output exactly, apart from line-ending and final-newline normalization.

## TC-001: Add and list a Todo

Aim: Verify that `todo` creates a Todo task and `list` displays its type and completion status.

### Input

```text
todo borrow book
list
bye
```

### Expected output

```text
____________________________________________________________
    ___
   /   |_   ______  ____
  / /| | | / / __ \/ __ \
 / ___ | |/ / /_/ / / / /
/_/  |_|___/\____/_/ /_/
Avon:	Hark! I am Avon who stands before thee.
Avon:	How may my hand or wit now serve thy need?
____________________________________________________________
____________________________________________________________
Avon:	By thy command, I've added this task:
        [T][ ] borrow book
Avon:	Now thou hast 1 tasks in thy list.
____________________________________________________________
____________________________________________________________
Avon:	Here are the tasks in thy list:
Avon:	1.[T][ ] borrow book
____________________________________________________________
____________________________________________________________
Avon:	Fare thee well! Pray heavens our paths cross anon.
____________________________________________________________
```

## TC-002: Add timed tasks and reverse completion

Aim: Verify Deadline and Event parsing, typed display, marking, unmarking, and list ordering.

### Input

```text
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
mark 1
unmark 1
list
bye
```

### Expected output

```text
____________________________________________________________
    ___
   /   |_   ______  ____
  / /| | | / / __ \/ __ \
 / ___ | |/ / /_/ / / / /
/_/  |_|___/\____/_/ /_/
Avon:	Hark! I am Avon who stands before thee.
Avon:	How may my hand or wit now serve thy need?
____________________________________________________________
____________________________________________________________
Avon:	By thy command, I've added this task:
        [D][ ] return book (by: Sunday)
Avon:	Now thou hast 1 tasks in thy list.
____________________________________________________________
____________________________________________________________
Avon:	By thy command, I've added this task:
        [E][ ] project meeting (from: Mon 2pm to: 4pm)
Avon:	Now thou hast 2 tasks in thy list.
____________________________________________________________
____________________________________________________________
Avon:	Tis well! Thy noble task is now fulfilled:
        [D][X] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Avon:	Thy noble task is undone once more:
        [D][ ] return book (by: Sunday)
____________________________________________________________
____________________________________________________________
Avon:	Here are the tasks in thy list:
Avon:	1.[D][ ] return book (by: Sunday)
        2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
____________________________________________________________
____________________________________________________________
Avon:	Fare thee well! Pray heavens our paths cross anon.
____________________________________________________________
```

## TC-003: Reject unknown and incomplete Todo commands

Aim: Verify that Avon rejects an unknown first word and a Todo without a description, then explains valid input.

### Input

```text
blah
todo
todo read Macbeth
bye
```

### Expected output

```text
____________________________________________________________
    ___
   /   |_   ______  ____
  / /| | | / / __ \/ __ \
 / ___ | |/ / /_/ / / / /
/_/  |_|___/\____/_/ /_/
Avon:	Hark! I am Avon who stands before thee.
Avon:	How may my hand or wit now serve thy need?
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I know not that command.
        Start with one of: todo, deadline, event, list, mark, unmark, bye.
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! The description of a todo cannot be empty.
        Enter it in this format: todo DESCRIPTION
____________________________________________________________
____________________________________________________________
Avon:	By thy command, I've added this task:
        [T][ ] read Macbeth
Avon:	Now thou hast 1 tasks in thy list.
____________________________________________________________
____________________________________________________________
Avon:	Fare thee well! Pray heavens our paths cross anon.
____________________________________________________________
```

## TC-004: Explain incomplete Deadline and Event commands

Aim: Verify error-specific guidance for missing descriptions, separators, and date or time details.

### Input

```text
deadline
deadline return book
deadline /by Sunday
deadline return book /by
event
event project meeting /to 4pm
event project meeting /from 2pm
event /from 2pm /to 4pm
event project meeting /from /to 4pm
event project meeting /from 2pm /to
bye
```

### Expected output

```text
____________________________________________________________
    ___
   /   |_   ______  ____
  / /| | | / / __ \/ __ \
 / ___ | |/ / /_/ / / / /
/_/  |_|___/\____/_/ /_/
Avon:	Hark! I am Avon who stands before thee.
Avon:	How may my hand or wit now serve thy need?
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! The description of a deadline cannot be empty.
        Enter it in this format: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! This deadline command is ill-formed.
        Include '/by' before the deadline date or time.
        Enter it in this format: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! The description of a deadline cannot be empty.
        Enter it in this format: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! This deadline command is ill-formed.
        Add a date or time after '/by'.
        Enter it in this format: deadline DESCRIPTION /by DATE_OR_TIME
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! The description of an event cannot be empty.
        Enter it in this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! This event command is ill-formed.
        Include '/from' before the start date or time.
        Enter it in this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! This event command is ill-formed.
        Include '/to' before the end date or time.
        Enter it in this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! The description of an event cannot be empty.
        Enter it in this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! This event command is ill-formed.
        Add a start date or time after '/from'.
        Enter it in this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! This event command is ill-formed.
        Add an end date or time after '/to'.
        Enter it in this format: event DESCRIPTION /from START /to END
____________________________________________________________
____________________________________________________________
Avon:	Fare thee well! Pray heavens our paths cross anon.
____________________________________________________________
```

## TC-005: Reject invalid Mark and Unmark task numbers

Aim: Verify that mark and unmark commands explain missing, malformed, and out-of-range task numbers.

### Input

```text
mark
mark one
mark 1
todo rehearse scene
mark 0
mark 2
mark 1
unmark
unmark 1
bye
```

### Expected output

```text
____________________________________________________________
    ___
   /   |_   ______  ____
  / /| | | / / __ \/ __ \
 / ___ | |/ / /_/ / / / /
/_/  |_|___/\____/_/ /_/
Avon:	Hark! I am Avon who stands before thee.
Avon:	How may my hand or wit now serve thy need?
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I cannot mark that task.
        Add a task number after 'mark'.
        Example: mark 1
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I cannot mark that task.
        Use one whole task number greater than zero.
        Example: mark 1
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I cannot mark that task.
        Your task list is empty; add a task first.
        Example: mark 1
____________________________________________________________
____________________________________________________________
Avon:	By thy command, I've added this task:
        [T][ ] rehearse scene
Avon:	Now thou hast 1 tasks in thy list.
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I cannot mark that task.
        Choose a task number from 1 to 1.
        Example: mark 1
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I cannot mark that task.
        Choose a task number from 1 to 1.
        Example: mark 1
____________________________________________________________
____________________________________________________________
Avon:	Tis well! Thy noble task is now fulfilled:
        [T][X] rehearse scene
____________________________________________________________
____________________________________________________________
Avon:	OOPS!!! I cannot unmark that task.
        Add a task number after 'unmark'.
        Example: unmark 1
____________________________________________________________
____________________________________________________________
Avon:	Thy noble task is undone once more:
        [T][ ] rehearse scene
____________________________________________________________
____________________________________________________________
Avon:	Fare thee well! Pray heavens our paths cross anon.
____________________________________________________________
```
