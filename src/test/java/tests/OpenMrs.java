package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utility.BaseDriver;

import static tests.Parent.waiting;

public class OpenMrs extends BaseDriver {

    @Test(priority = 1, groups = {"Smoke Test"}, dataProviderClass = OpenMrs.class, dataProvider = "userData")
    public void US_401(String username, String password) { //Check Login Errors

        ElementsPage ep = new ElementsPage();
        driver.get("https://openmrs.org/");
        waiting(1);
        ep.clickFunction(ep.getDemo());
        ep.clickFunction(ep.getDemo2());
        waiting(1);
        ep.clickFunction(ep.getEnterMRS2());
        ep.sendKeysFunction(ep.getUserName(), username);
        ep.sendKeysFunction(ep.getPassword(), password);
        ep.randomClick(ep.getLocationSession());
        ep.clickFunction(ep.getLoginBtn());

        if (username.equals("Admin") && password.equals("Admin123")) {
            ep.verifyContainsText(ep.getLoginSuccess(), "Logged in as");
            ep.clickFunction(ep.getLogout());
        } else {
            ep.verifyContainsText(ep.getErrorMsg(), "Invalid");
        }
    }

    @DataProvider
    public Object[][] userData() {
        Object[][] data =
                {
                        {"admin", "Admin12"},
                        {"Admin", ""},
                        {"", ""},
                        {"", "Admin123"},
                        {"admin", "admin123"},
                        {"xyz", "1234"}
                };
        return data;
    }


    @Test(priority = 2, groups = {"Smoke Test"})
    public void US_402() { //Login to the System

        ElementsPage ep = new ElementsPage();
        driver.get("https://openmrs.org/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        //waiting(1);
        ep.clickFunction(ep.getDemo());
        ep.clickFunction(ep.getDemo2());
        waiting(1);
        ep.clickFunction(ep.getEnterMRS2());
        ep.sendKeysFunction(ep.getUserName(), "admin");
        ep.sendKeysFunction(ep.getPassword(), "Admin123");
        ep.randomClick(ep.getLocationSession());
        ep.clickFunction(ep.getLoginBtn());
        //waiting(1);
        ep.verifyContainsText(ep.getLoginSuccess(), "Logged in as");
    }

    @Test(priority = 10, groups = {"Smoke Test"})
    public void US_403() { //Logout from the System

        ElementsPage ep = new ElementsPage();

        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getLogout());
        ep.verifyContainsText(ep.getLogoutSuccess(), "LOGIN");
    }

    @Test(priority = 4, groups = {"Smoke Test"}, dataProviderClass = OpenMrs.class, dataProvider = "patientData")
    public void US_404(String name, String familyName, String day, String year, String adres, String city, String country, String phoneNumber) { //Patient Registration

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        }
        ep.clickFunction(ep.getRegister());
        ep.sendKeysFunction(ep.getName(), name);
        ep.sendKeysFunction(ep.getSurname(), familyName);
        ep.clickFunction(ep.getNextButton1());
        ep.clickFunction(ep.getGender());
        ep.clickFunction(ep.getNextButton2());
        ep.sendKeysFunction(ep.getDay(), day);
        ep.clickFunction(ep.getMonth());
        ep.sendKeysFunction(ep.getYear(), year);
        ep.clickFunction(ep.getNextButton3());
        ep.sendKeysFunction(ep.getAddress(), adres);
        ep.sendKeysFunction(ep.getCity(), city);
        ep.sendKeysFunction(ep.getCountry(), country);
        ep.clickFunction(ep.getNextButton4());
        ep.sendKeysFunction(ep.getPhone(), phoneNumber);
        ep.clickFunction(ep.getNextButton5());
        ep.clickFunction(ep.getNextButton6());
        waiting(1);
        ep.clickFunction(ep.getConfirm());
        ep.verifyContainsText(ep.getAccessMessage(), "Created Patient Record:");
        ep.clickFunction(ep.getHomeButton());
    }

    @DataProvider(name = "patientData")
    public static Object[][] patientData() {
        return new Object[][]{
                {"John1", "Marunchak", "20", "1991", "66628 Thompson Crossing", "Rochester", "New York", "055544433245"},
                {"Westley1", "Wilce", "07", "1990", "16471 Melvin Street", "North Carolina", "Georgia", "05444212451"},
                {"Brenden1", "Woltering", "27", "1992", "437 High Crossing Drive", "Stockton", "California", "05444064506"},
                {"Jack1", "Woltering", "26", "1980", "437 High Crossing Drive", "Stockton", "California", "0544545455"}
        };
    }

    @Test(priority = 3, groups = {"Smoke Test"})
    public void US_405() { //My Account

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getUserIcon());
        waiting(1);
        ep.clickFunction(ep.getMyAccount());
        waiting(1);
        ep.hoverFunction(ep.getChangePassword());
        ep.verifyContainsText(ep.getChangePassword(), "Change Password");
        waiting(1);
        ep.hoverFunction(ep.getMyLanguages());
        ep.verifyContainsText(ep.getMyLanguages(), "My Languages");
        waiting(1);
        ep.clickFunction(ep.getHomeButton());
    }

    @Test(priority = 5, groups = {"Smoke Test"})
    public void US_406() { //Patient List Search

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getSearchPatient());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "John1 Marunchak");
        waiting(1);
        ep.clickFunction(ep.getPatientRow());
        Assert.assertTrue(ep.getNameSuccess().getText().toUpperCase().contains("JOHN1"));
        Assert.assertTrue(ep.getSurnameSuccess().getText().toUpperCase().contains("MARUNCHAK"));
        waiting(1);
        ep.clickFunction(ep.getHomeButton());
        ep.clickFunction(ep.getSearchPatient());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "No existing name");
        Assert.assertTrue(ep.getSearchPatientNegativeSuccess().getText().contains("No matching records found"));
        waiting(2);
        ep.clickFunction(ep.getHomeButton());
    }

    @Test(priority = 9, groups = {"Smoke Test"})
    public void US_407() { //Patient Deletion

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getSearchPatient());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "Brenden1 Woltering");
        waiting(1);
        ep.clickFunction(ep.getPatientRow());
        String id1 = ep.getPatientId().getText();
        waiting(1);
        ep.clickFunction(ep.getDeletePatient());
        ep.sendKeysFunction(ep.getDeleteReason(), "Incorrect Information");
        ep.clickFunction(ep.getConfirmButton());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "John1 Marunchak");
        waiting(1);
        ep.clickFunction(ep.getPatientRow());
        waiting(1);
        ep.clickFunction(ep.getDeletePatient());
        ep.sendKeysFunction(ep.getDeleteReason(), "Patient Request");
        ep.clickFunction(ep.getConfirmButton());

        wait.until(ExpectedConditions.visibilityOf(ep.getSearchPatientBox()));
        waiting(1);
        ep.sendKeysFunction(ep.getSearchPatientBox(), id1);
        Assert.assertTrue(ep.getSearchPatientNegativeSuccess().getText().contains("No matching records found"));
        ep.clickFunction(ep.getHomeButton());
    }

    @Test(priority = 6, groups = {"Smoke Test"})
    public void US_408() { //Patient Listing

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getSearchPatient());
        String fullText = ep.getShowingEntries().getText();
        String[] words = fullText.split(" ");
        int listTotal = Integer.parseInt(words[words.length - 2]);

        int pageNumber = ep.getPageNumber().size();
        int rowNumber = 0;

        for (int i = 0; i < pageNumber; i++) {
            ep.getPageNumber().get(i).click();
            rowNumber += ep.getPageRows().size();
        }
        ep.clickFunction(ep.getHomeButton());

        Assert.assertEquals(listTotal, rowNumber);
    }

    @Test(priority = 7, groups = {"Smoke Test"})
    public void US_409() { //Merge Patient Records

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getSearchPatient());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "Westley1 Wilce");
        waiting(1);
        ep.clickFunction(ep.getPatientRow());
        String id1 = ep.getPatientId().getText();
        ep.clickFunction(ep.getHomeButton());
        waiting(1);
        ep.clickFunction(ep.getSearchPatient());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "Jack1 Woltering");
        waiting(1);
        ep.clickFunction(ep.getPatientRow());
        String id2 = ep.getPatientId().getText();
        ep.clickFunction(ep.getHomeButton());
        waiting(1);
        ep.clickFunction(ep.getDataManagement());
        ep.clickFunction(ep.getMergePatient());
        ep.sendKeysFunction(ep.getPatient1(), id1);
        ep.sendKeysFunction(ep.getPatient2(), id2);
        ep.clickFunction(ep.getPatientSearchClick());
        waiting(5);
        ep.clickFunction(ep.getContinueButton());
        ep.verifyContainsText(ep.getMergindSuccess(), "Merging cannot be undone");
        ep.clickFunction(ep.getClickPatient());
        ep.clickFunction(ep.getContinueButton());
        ep.verifyContainsText(ep.getMergePatientId1(), id2);
        ep.verifyContainsText(ep.getMergePatientId2(), id1);
        ep.clickFunction(ep.getHomeButton());
    }

    @Test(priority = 8, groups = {"Smoke Test"})
    public void US_410() { //Patient Appointment Scheduling with Incorrect Time Zone

        ElementsPage ep = new ElementsPage();
        if (driver.getCurrentUrl().equals("data:,")) {
            US_402();
        }
        ep.clickFunction(ep.getAppointmentScheduling());
        ep.clickFunction(ep.getManageAppointments());
        ep.sendKeysFunction(ep.getSearchPatientBox(), "Westley1 Wilce");
        waiting(2);
        ep.clickFunction(ep.getPatientRow());
        ep.verifyContainsText(ep.getAlertText(), "Your computer is not set to the right time zone.");
        ep.clickFunction(ep.getHomeButton());
    }
}