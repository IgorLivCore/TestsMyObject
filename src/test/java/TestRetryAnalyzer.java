/**
 * Created by natalia on 14.04.15.
 */
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class TestRetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private int maxRetryCount = 1;

    public boolean retry(ITestResult result) {

        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}