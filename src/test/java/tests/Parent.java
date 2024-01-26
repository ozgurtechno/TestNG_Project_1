package tests;

import org.openqa.selenium.By;
import utility.BaseDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Parent {
    public static WebDriverWait wait;

    public Parent() {
        wait = new WebDriverWait(BaseDriver.driver, Duration.ofSeconds(15));
    }

    public void sendKeysFunction(WebElement element, String value) {
        waitUntilVisible(element);
        element.clear();
        element.sendKeys(value);
    }

    public void clickFunction(WebElement element) {
        waitUntilClickable(element);
        element.click();
    }

    public static void waitUntilVisible(WebElement element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.visibilityOf(element));

    }

    public void waitUntilClickable(WebElement element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitUntilClickableAndVisible(WebElement element) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        wait.until(ExpectedConditions.and(
                ExpectedConditions.elementToBeClickable(element),
                ExpectedConditions.elementToBeClickable(element)));
    }

    public static void waiting(int second) {
        try {
            Thread.sleep(second * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void randomClick(List<WebElement> element) {
        Random rnd = new Random();
        int index = rnd.nextInt(element.size());
        element.get(index).click();
    }

    public void verifyContainsText(WebElement element, String msg) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
        waitUntilVisible(element);
        Assert.assertTrue(element.getText().contains(msg));
    }

    public void hoverFunction(WebElement element) {
        Actions actions = new Actions(BaseDriver.driver);
        Action action = actions.moveToElement(element).build();
        action.perform();
    }
}