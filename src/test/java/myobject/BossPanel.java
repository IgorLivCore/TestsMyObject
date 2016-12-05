package myobject;

import org.junit.BeforeClass;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.*;

import static Base.MOWebdriver.*;
import java.util.Arrays;
import static org.testng.Assert.*;

/**
 * Created by igor on 02.12.16.
 */
public class BossPanel extends MOTestBase{

    int t1 = 1;
    @BeforeMethod
    private void isDataPresent() throws Exception
    {
        if (isListEmpty(new String[] {"Монитор руководителя"})) {
            fail("Не хватает данных для проведения теста!");
            t1 = 0;
            System.out.print(t1);
        }
    }


    @Test
    private void BPTechnicalDeviceStatusNow(){

    System.out.print(t1);

    }


}
