import org.junit.runners.Suite;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.xml.*;

import java.io.File;
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by natalia on 14.04.15.
 */

public class TestListener extends TestListenerAdapter {
    @Override
    public void onTestFailure(ITestResult result){
        //TestNG testng = new TestNG();
        run(result.getTestClass(),result.getMethod().getMethodName());

        //testng.run();
        /*result.getMethod();
        TestNG testng = new TestNG();
        XmlSuite xmlSuite = new XmlSuite();
        suite.setName("Failed test " + result.getMethod().getMethodName());



        XmlInclude xmlInclude = new XmlInclude();
        xmlInclude.toXml(result.getMethod().getMethodName());
        List<XmlInclude> xmlIncludes
        XmlClass xmlClass = new XmlClass(result.getClass().getName());
        xmlClass.setIncludedMethods(xmlInclude);

        xs.add(xmlSuite);
        testng.setTestSuites();
        testng.run();*/

    }



    public java.lang.Object run(java.lang.Object someClass,String methodName) {
        try{
            Method classMethod = someClass.getClass().getMethod(methodName);
            java.lang.Object obj = someClass;
            return classMethod.invoke(someClass);
        }
        catch (Exception e)
        { return null;}
    }


    @Override
    public void onFinish(ITestContext context) {
       // System.out.println("onFinish");
        /*Iterator<ITestResult> listOfFailedTests = context.getFailedTests().getAllResults().iterator();
        while (listOfFailedTests.hasNext()) {
            ITestResult failedTest = listOfFailedTests.next();
            ITestNGMethod method = failedTest.getMethod();
            if (context.getFailedTests().getResults(method).size() > 1) {
                listOfFailedTests.remove();
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    listOfFailedTests.remove();
                }
            }
        }*/
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
    }
    @Override
    public void onStart(ITestContext testContext){
    }
}
