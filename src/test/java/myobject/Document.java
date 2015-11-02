package myobject;

import Base.MOTestBase;
import Base.MOWebdriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.*;
import static Base.MOWebdriver.*;

/**
 * Created by natalia on 16.02.15.
 *
 * Тип документа - где создаётся
 *
 * ПЛА - Объект
 * Универсальный - Предприятие
 * Сертификаты тех. устройств - ТУ
 * Разрешительный - Предприятие
 * Заключение ЭПБ - ТУ
 * Документация СУПБ - Предприятие
 * Полис страхования - Объект
 * Подтверающий док. о выполнении предисания РТН - Предписание РТН
 * Предписания РТН - Предписание РТН
 * Акт поверки / ТО - ТУ
 * Свидетельство о регистрации ОПО - Предприятие
 * Заключение по обследованию тех. сотсояния ЗиС - ЗиС
 *
 */
public class Document extends MOTestBase {
    @BeforeClass
    private void isDataPresent() throws Exception{
        if (isListEmpty(new String[] { "Персонал" }))
            fail("Не хватает данных для проведения теста!");
    }

    private void addDefaultFields() throws Exception
    {
        legend = "Форма документа";
        input("Название документа", "ТестСозданиеДокументаНазваниеДокумента" + curDate());
        input("Код документа", data = curDate());
        date("Дата документа","20.04.2015");
        select("Статус документа", "Актуальный");
        input("Кем выдан","ТестСозданиеДокументаКемВыдан");
        select("Сотрудник",0);
    }

    private void addEmptyDefaultFields() throws Exception
    {
        legend = "Форма документа";
        input("Название документа", "");
        input("Дата документа", "");
    }

    @Test(priority=6, groups = { "add" })
    public void testAddPLA() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "План ликвидации аварии");
        addDefaultFields();
        select("Источник","Внутренний");

        save();
        messageResultOfOperation("Документ ПЛА");

        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ ПЛА не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyPLA() throws Exception{
        expected_result=false;
        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "План ликвидации аварии");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Документ ПЛА");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddDefault() throws Exception
    {
        operation="добавление";
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Универсальный");
        addDefaultFields();
        select("Источник","Внутренний");

        save();
        messageResultOfOperation("Универсальный документ");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Универсальный документ не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyDefault() throws Exception{
        expected_result=false;
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Универсальный");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Универсальный документ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddTDCertificate() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Сертификаты тех. устройств");
        addDefaultFields();
        select("Источник","Внешний");
        select("Вывод о состоянии","Работоспособное");
        input("Тип сертификата", "ТестСозданиеДокументаТипСертификата");
        input("Номер сертификата", "ТестСозданиеДокументаНомерСертификата");
        input("Кем выдан сертификат", "ТестСозданиеДокументаКемВыданСертификат");

        save();
        messageResultOfOperation("Документ сертификат ТУ");

        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ сертификат ТУ не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyTDCertificate() throws Exception{
        expected_result=false;
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Сертификаты тех. устройств");
        addEmptyDefaultFields();
        input("Тип сертификата", "");
        input("Номер сертификата", "");
        input("Кем выдан сертификат", "");

        save();
        messageResultOfOperation("Документ сертификат ТУ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddPermissive() throws Exception
    {
        operation="добавление";
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Разрешительный");
        addDefaultFields();
        select("Источник","Внешний");
        input("Вид разрешительного документа", "ТестСозданиеДокументаВидРазрешительногоДокумента");
        date("Срок действия (до какой даты)","25.07.2020");
        input("Кем выдан","ТестСозданиеДокументаКемВыдан");

        save();
        messageResultOfOperation("Разрешительный документ");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Разрешительный документ не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyTDPermissive() throws Exception{
        expected_result=false;
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Разрешительный");
        addEmptyDefaultFields();
        input("Вид разрешительного документа", "");
        input("Срок действия (до какой даты)", "");
        input("Кем выдан","");

        save();
        messageResultOfOperation("Разрешительный документ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddInspectionFinally() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Заключение ЭПБ");
        addDefaultFields();
        select("Источник","Внешний");
        date("Дата внесения заключения в реестр заключений ЭПБ", "09.10.2015");
        date("Дата проведения экспертизы промышленной безопасности", "03.10.2015");
        date("Дата следующей экспертизы промышленной безопасности", "03.10.2017");

        save();
        messageResultOfOperation("Документ заключение ЭПБ");

        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Заключение ЭПБ не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyInspectionFinally() throws Exception{
        expected_result=false;
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Заключение ЭПБ");
        addEmptyDefaultFields();
        input("Дата внесения заключения в реестр заключений ЭПБ", "");
        input("Дата проведения экспертизы промышленной безопасности", "");
        input("Дата следующей экспертизы промышленной безопасности", "");

        save();
        messageResultOfOperation("Документ заключение ЭПБ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddDocumentationSUPB() throws Exception
    {
        operation="добавление";
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Документация СУПБ");
        addDefaultFields();
        select("Источник","Внутренний");

        save();
        messageResultOfOperation("Документация СУПБ");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Заключение ЭПБ не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyDocumentationSUPB() throws Exception{
        expected_result=false;
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Документация СУПБ");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Документация СУПБ");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddCivilLiabilityCoverage() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Полис обязательного страхования гражданской ответственности");
        addDefaultFields();
        select("Источник","Внешний");

        save();
        messageResultOfOperation("Документ полис обязательного страхования гражданской ответственности");

        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Полис обязательного страхования гражданской ответственности не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyCivilLiabilityCoverage() throws Exception{
        expected_result=false;
        goTo("Эксплуатация", "Объекты");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Полис обязательного страхования гражданской ответственности");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Документ полис обязательного страхования гражданской ответственности");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddConfirmingPrescriptions() throws Exception
    {
        operation="добавление";
        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Документы");
        add();

        select("Тип документа", "Подтверждающий документ о выполнении предписания РТН");
        addDefaultFields();
        select("Источник","Внутренний");

        save();
        messageResultOfOperation("Подтверждающий документ о выполнении предписания РТН");

        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Подтверждающий документ о выполнении предписания РТН не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyConfirmingPrescriptions() throws Exception{
        expected_result=false;
        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Документы");
        add();

        select("Тип документа", "Подтверждающий документ о выполнении предписания РТН");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Подтверждающий документ о выполнении предписания РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddPrescription() throws Exception
    {
        operation="добавление";
        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Документы");
        add();

        select("Тип документа", "Предписания РТН");
        addDefaultFields();
        select("Источник","Внутренний");

        save();
        messageResultOfOperation("Документ предписание РТН");

        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ предписание РТН не добавлено");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyPrescription() throws Exception{
        expected_result=false;
        goTo("Производственный контроль", "Предписания РТН");
        selectRow(0);
        goTo("Документы");
        add();

        select("Тип документа", "Предписания РТН");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Документ предписание РТН");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddTechnicalInspectionAct() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Акт поверки / технического освидетельствования");
        addDefaultFields();
        select("Источник","Внутренний");
        date("Дата очередной поверки (технического освидетельствования)", "14.06.2015");
        date("Дата следующей поверки (технического освидетельствования)", "14.06.2018");

        save();
        messageResultOfOperation("Документ акт поверки/технического освидетельствования");

        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ акт поверки/технического освидетельствования не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyTechnicalInspectionAct() throws Exception{
        expected_result=false;
        goTo("Эксплуатация", "ТУ");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Акт поверки / технического освидетельствования");
        addEmptyDefaultFields();
        input("Дата очередной поверки (технического освидетельствования)", "");
        input("Дата следующей поверки (технического освидетельствования)", "");

        save();
        messageResultOfOperation("Документ акт поверки/технического освидетельствования");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddCertificateRegistrationObject() throws Exception
    {
        operation="добавление";
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Свидетельство о регистрации ОПО");
        addDefaultFields();
        select("Источник","Внешний");

        save();
        messageResultOfOperation("Документ свидетельство о регистрации ОПО");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ свидетельство о регистрации ОПО не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyCertificateRegistrationObject() throws Exception{
        expected_result=false;
        goTo("Общее", "Предприятие");
        goTo("Документы");
        add();

        select("Тип документа", "Свидетельство о регистрации ОПО");
        addEmptyDefaultFields();

        save();
        messageResultOfOperation("Документ акт поверки/технического освидетельствования");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority=6, groups = { "add" })
    public void testAddBacTechnicalInspectionAct() throws Exception
    {
        operation="добавление";
        goTo("Эксплуатация", "ЗиС");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Заключение по обследованию технического состояния ЗиС");
        addDefaultFields();
        select("Источник","Внешний");
        date("Дата проведения обследования технического состояния", "16.11.2014");
        date("Дата следующего обследования технического состояния", "06.11.2019");

        save();
        messageResultOfOperation("Документ заключение по обследованию технического состояния ЗиС");

        goTo("Эксплуатация", "ЗиС");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ заключение по обследованию технического состояния ЗиС не добавлен");
    }

    @Test(priority = 20, groups = { "empty","add" })
    public void testAddEmptyBacTechnicalInspectionAct() throws Exception{
        expected_result=false;
        goTo("Эксплуатация", "ЗиС");
        selectRow(0);
        goTo("Документация");
        goTo("Документы");
        add();

        select("Тип документа", "Заключение по обследованию технического состояния ЗиС");
        addEmptyDefaultFields();
        input("Дата проведения обследования технического состояния", "");

        save();
        messageResultOfOperation("Документ заключение по обследованию технического состояния ЗиС");

        String[] s=checkEmptyFields();
        assertTrue(s.length == 0, "Есть обязательные поля, не подсвеченные как обязательные:\n" + Arrays.asList(s) + "\n" + (s.length == 0 ? "" : getScreenshot("fail")));
    }

    @Test(priority = 210, groups = { "edit" })
    public void testEdit() throws Exception{
        operation="изменение";
        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        edit();

        legend = "Форма документа";
        input("Название документа", "ТестИзменениеДокументаНазваниеДокумента" + curDate());
        input("Код документа", data = curDate());
        date("Дата документа", "20.08.2015");
        select("Статус документа", "Не актуальный");
        select("Источник","Внешний");
        input("Кем выдан","ТестИзменениеДокументаКемВыдан");
        select("Сотрудник",1);

        save();
        messageResultOfOperation("План");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        legend = "";
        assertEquals(revisionValue("Код"), data, "Документ не изменен");
    }

    @Test(priority=310, groups = { "delete" })
    public void testDelete() throws Exception
    {
        operation="удаление";
        goTo("Общее", "Предприятие");
        goTo("Документы");
        selectRow(0);
        data = revisionValue("Код");
        delete();

        //messageResultOfOperation("Документ");

        goTo("Общее", "Предприятие");
        goTo("Документы");
        if (!MOWebdriver.isListEmpty()) {
            selectRow(0);
            assertNotEquals(revisionValue("Код"), data, "Документ не удален");
        }
    }
}
