import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void searchInFileTest() {
        try {
            long[] input1Search = Main.searchInFile("sample/input1.txt", "пирог");
            assertArrayEquals(input1Search, new long[]{ 7 });

            long[] input2Search = Main.searchInFile("sample/input2.txt", "пирог");
            assertArrayEquals(input2Search, new long[0]);

            long[] skyrimSearch = Main.searchInFile("sample/skyrim.txt", "thief");
            assertArrayEquals(skyrimSearch, new long[]{ 145, 497, 989 });

            long[] tasksSearch = Main.searchInFile("sample/tasks.txt", "задания");
            assertArrayEquals(tasksSearch, new long[]{ 372, 408, 534 });
        } catch (Exception e) {
            fail(e);
        }
    }
}
