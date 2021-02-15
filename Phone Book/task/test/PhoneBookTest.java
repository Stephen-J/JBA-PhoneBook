import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.Arrays;
import java.util.List;

public class PhoneBookTest extends StageTest {

    @Override
    public List<TestCase> generate() {
        return Arrays.asList(
            new TestCase().setTimeLimit(30 * 60 * 1000)
        );
    }

    @Override
    public CheckResult check(String reply, Object clue) {
        reply = reply.toLowerCase();
        return new CheckResult(
            reply.contains("start searching")
                && reply.contains("found")
                && reply.contains("min.")
                && reply.contains("sec.")
                && reply.contains("ms.")
                && reply.contains("sorting time")
                && reply.contains("searching time")
                && reply.contains("linear search")
                && reply.contains("bubble sort")
                && reply.contains("jump search"));
    }
}
