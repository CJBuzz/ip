package avon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class TaskListTest {
    @Test
    void find_keywordInDescriptions_returnsMatchingTasksInOrder() {
        TaskList taskList = new TaskList(List.of(
                new Todo("read book"),
                new Todo("write essay"),
                new Todo("return book")));

        List<Task> matchingTasks = taskList.find("book");

        assertEquals(2, matchingTasks.size());
        assertEquals("read book", matchingTasks.get(0).getDescription());
        assertEquals("return book", matchingTasks.get(1).getDescription());
    }

    @Test
    void find_keywordIsCaseSensitive_returnsNoMatch() {
        TaskList taskList = new TaskList(List.of(new Todo("read book")));

        assertEquals(0, taskList.find("Book").size());
    }
}
