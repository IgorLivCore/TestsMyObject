package adminpanel;

import Base.AdminTestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.data;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by natalia on 06.10.15.
 */
public class ActivationCompany extends AdminTestBase {
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Компании");
        selectRow(0);
        goTo("Активации");
    }

    @Test(priority=30, groups = { "add" })
    public void testAdd() throws Exception
    {
        operation="добавление";
        add();

        select("Тариф",3);
        input("Уплаченная сумма","50000");
        input("Заказано дополнительных мест пользователя","5");
        input("Заказано дополнительно памяти, ГБ","2");
        input("Комментарий", data = "ТестСозданиеАктивацииКомментарий" + curDate());

        save();
        messageResultOfOperation("Активация");

        goTo("Компании");
        selectRow(0);
        goTo("Активации");
        assertEquals(revisionValueFromList("Комментарий"), data,"Активация не добавлена");
    }

    @Test(priority=30, groups = { "add","empty" })
    public void testAddEmpty() throws Exception
    {
        expected_result=false;
        add();

        unselect("Тариф");
        input("Уплаченная сумма", "");
        input("Заказано дополнительных мест пользователя","");
        input("Заказано дополнительно памяти, ГБ", "");

        save();
        messageResultOfOperation("Активация");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }
}
