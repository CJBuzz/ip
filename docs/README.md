# Avon User Guide

Avon is a Shakespearean task manager that keeps todos, deadlines, and events in one place. Speak commands in
the text field, press <kbd>Enter</kbd> or **Send**, and Avon will answer in the conversation window.

![The Avon task manager showing a short conversation](Ui.png)

## Quick start

1. Install Java 25.
2. Download `avon.jar` from the latest GitHub release.
3. Place the JAR in an empty folder so its task data is easy to find.
4. Open a terminal in that folder and run `java -jar avon.jar`.

Avon creates `data/avon.txt` beside the JAR when the task list first changes. Keep that file if you want your
tasks to remain available the next time Avon starts.

## Commands

Words in uppercase represent values that you supply. Dates use `yyyy-MM-dd`; an optional time uses 24-hour
`HHmm` format.

### Add a todo

Use `todo DESCRIPTION` for a task without a date.

```text
todo read Hamlet
```

### Add a deadline

Use `deadline DESCRIPTION /by DATE [TIME]` for work due by a specific date or time.

```text
deadline submit iP /by 2026-09-18 2359
```

### Add an event

Use `event DESCRIPTION /from DATE [TIME] /to DATE [TIME]` for an activity with a start and end. The end must
be later than the start.

```text
event project meeting /from 2026-09-18 1400 /to 2026-09-18 1600
```

### View tasks

Use `list` to show every task and its current number.

```text
list
```

Task markers have the following meanings:

- `[T]` — todo
- `[D]` — deadline
- `[E]` — event
- `[ ]` — not completed
- `[X]` — completed

### Find tasks

Use `find KEYWORD` to show tasks whose descriptions contain the case-sensitive keyword.

```text
find project
```

### Mark or unmark a task

Use the number shown by `list` or `find`.

```text
mark 2
unmark 2
```

### Delete a task

Use `delete TASK_NUMBER` to permanently remove one task.

```text
delete 2
```

### Get help or exit

Use `help` to see command formats inside Avon. Use `bye` to close the application.

```text
help
bye
```

## Error recovery

Avon highlights invalid GUI responses in red and explains both the problem and the expected command format.
Common corrections include:

- supply a non-empty description;
- include each required `/by`, `/from`, or `/to` separator exactly once;
- use real dates and valid 24-hour times;
- choose a task number currently shown by `list`;
- avoid adding a task with the same type, description, and date or time as an existing task.

If `data/avon.txt` is missing, Avon starts with an empty task list and creates it when needed. If the file is
unreadable or corrupted, Avon reports the problem without overwriting the existing data.

## Data and privacy

All task data stays in the local `data/avon.txt` file. Avon does not require an account or send task data over
the network.
