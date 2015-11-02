package adminpanel;

import Base.AdminTestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static Base.MOWebdriver.*;
import static Base.MOWebdriver.data;
import static Base.MOWebdriver.revisionValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by natalia on 09.10.15.
 */
public class WebinarPayment extends AdminTestBase{
    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Вебинары");
        goTo("Заявки");
    }

    @Test(priority=230, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="изменение";
        selectRow(0);
        edit();

        input("Фамилия", "ТестИзменениеЗаявкиФамилия");
        input("Имя", "ТестИзменениеЗаявкиИмя");
        input("Отчество", "ТестИзменениеЗаявкиОтчество");
        input("Организация", data = "ТестИзменениеЗаявкиОрганизация" + curDate());
        input("Email", "test" + curDate() + "@test.test");
        input("Телефон", "7865984");
        date("Дата заявки", "01.10.2015");
        select("Способ оплаты", "Онлайн");
        input("Сумма оплаты", "550");
        switcher("Оплата проведена","Да");
        date("Дата подтверждения оплаты","10.10.2015");

        save();
        messageResultOfOperation("Заявка");

        goTo("Вебинары");
        goTo("Заявки");
        if (!isListEmpty()) {
            selectRow(0);
            assertEquals(revisionValue("Организация"), data, "Заявка не изменена");
        }
    }

    @Test(priority=231, groups = { "edit","empty" })
    public void testEditEmpty() throws Exception
    {
        expected_result=false;
        selectRow(0);
        edit();

        input("Фамилия", "");
        input("Имя", "");
        input("Email", "");
        input("Дата заявки", "");

        save();
        //messageResultOfOperation("Заявка");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=330, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        selectRow(0);
        data = revisionValue("Организация");
        delete();

        //messageResultOfOperation("Заявка");

        goTo("Вебинары");
        goTo("Заявки");
        if (!isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("Организация"), data, "Заявка не изменена");
        }
    }
}