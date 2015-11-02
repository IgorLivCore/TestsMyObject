package myobject;

import Base.MOTestBase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static Base.MOWebdriver.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Created by natalia on 12.05.15.
 */
public class ObjectCrashTraining extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] {"Объекты"}))
            fail("Не хватает данных для проведения теста!");
    }

    @BeforeMethod
    private void goToList() throws Exception{
        goTo("Эксплуатация","Объекты");
        selectRow(0);
        goTo("Персонал");
        goTo("Подготовка");
    }

    @Test(priority=10, groups = { "edit" })
    public void testEdit() throws Exception
    {
        operation="добавление";
        legend="";
        select("Выберите год","2014");
        button("Показать данные по подготовке");
        add();

        input("Численность сотрудников, работающих на ОПО, успешно прошедших обучение действиям в случае возникновения аварии на ОПО", "20");
        input("Оценка готовности работников к действиям во время аварии", data = "Готовы " + curDate());
        input("Проведено учебно-тренировочных занятий по готовности персонала к действиям в случае возникновения аварии на ОПО согласно графику","13");
        input("Проведено учебных тревог по готовности персонала к действиям в случае возникновения аварии на ОПО согласно графику","7");
        input("Запланировано в отчетном периоде учебно-тренировочных занятий по действиям персонала в случае аварий и инцидентов","15");
        input("Запланировано на следующий отчетный период учебно-тренировочных занятий по действиям персонала в случае аварий и инцидентов","12");
        input("Запланировано в отчетном периоде учебных тревог по действиям персонала в случае возникновения аварий","10");
        input("Запланировано на следующий отчетный период учебных тревог по действиям персонала в случае аварий","11");
        switcher("Наличие специальных стендов, тренажеров и тому подобное для тренировок по планам ликвидации аварий", "Да");
        switcher("Наличие положения о расследовании причин инцидентов, согласованного с надзорными органами", "Да");
        input("Регистрационный номер положения о расследовании причин инцидентов, согласованного с надзорными органами", "36159");

        save();
        messageResultOfOperation("подготовка персонала");

        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Персонал");
        goTo("Подготовка");
        legend="";
        select("Выберите год", "2014");
        button("Показать данные по подготовке");
        assertEquals(revisionValue("Оценка готовности работников к действиям во время аварии"), data,"Подготовка пресонала не изменена");
    }
}
