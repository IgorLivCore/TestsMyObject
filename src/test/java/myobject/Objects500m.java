package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static Base.MOWebdriver.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.fail;

/**
 * Created by natalia on 12.05.15.
 */
public class Objects500m extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Объекты"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Состав");
        goTo("Объекты в радиусе 500м");
    }

    @Test(priority=10, groups = { "include" })
    public void testInclude() throws Exception
    {
        operation="включение объектов в радиусе 500 м";
        add();

        selectRow(0);
        legend="Общая информация";
        data=revisionValue("Наименование");
        back();
        include(0);

        messageResultOfOperation("объект в радиусе 500м");

        goToList();
        input("Поиск по списку", data);
        selectRow(0);
        assertEquals(revisionValue("Наименование"), data,"Объект в радиусе 500м не добавлен");
    }

    @Test(priority=30, groups = { "exclude" })
    public void testExclude() throws Exception
    {
        operation="исключение объекта в радиусе 500м";

        selectRow(0);
        data = revisionValue("Наименование");
        back();
        exclude(0);

        messageResultOfOperation("объект в радиусе 500м");

        goToList();
        if(!MOWebdriver.isListEmpty())
        {
            selectRow(0);
            edit();
            assertNotEquals(revisionValue("Наименование"), data,"Объект в радиусе 500 м не исключен");
        }
    }
}
