package avon.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
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

    @Test
    void constructor_nullList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new TaskList(null));
    }

    @Test
    void constructor_listWithNullTask_throwsIllegalArgumentException() {
        List<Task> tasks = Arrays.asList(new Todo("read book"), null);

        assertThrows(IllegalArgumentException.class, () -> new TaskList(tasks));
    }
}
