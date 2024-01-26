package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

public class BaseDriver {
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static Actions actions;

    @BeforeClass(alwaysRun = true) // Before Class doesn't work with groups. Because we are not running the class.

    @Parameters("browserName")
    public void createDriver(@Optional("chrome") String browser){
        switch (browser.toLowerCase()) {

            case "chrome":
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            case "edge":
                driver = new EdgeDriver();
                break;
        }

        driver.manage().window().maximize();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        actions = new Actions(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    }

    @AfterClass(alwaysRun = true)
    public void quitDriver(){
        driver.quit();
        closePreviousDrivers();
    }

    public void closePreviousDrivers(){
        try {
            if (Platform.getCurrent().is(Platform.MAC)) {
                Runtime.getRuntime().exec("pkill -f chromedriver");
            } else if (Platform.getCurrent().is(Platform.WINDOWS)) {
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Logger logger = LogManager.getLogger();
    @BeforeMethod(alwaysRun = true)
    public void logsBeforeTest(ITestResult testResult){
        logger.info(testResult.getMethod().getMethodName()+ " test has started");
    }

    @AfterMethod(alwaysRun = true)
    public void logsAfterTest(ITestResult testResult){
        logger.info(testResult.getMethod().getMethodName()+" has ended with status of "+testResult.getStatus());
    }
}
