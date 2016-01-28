/**
 * Created by mariuskueng on 26.01.16.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import java.util.List;


public class WebDriverTest {

    public static void main(String[] args) {
        WebDriverTest w = new WebDriverTest();
        w.checkTitle("http://newtours.demoaut.com", "Welcome: Mercury Tours");
        w.checkSearch("https://www.designernews.co", "Meteor");

        // exit the program explicitly
        System.exit(0);
    }

    public void checkTitle(String url, String title) {
        // declaration and instantiation of objects/variables
        WebDriver driver = new FirefoxDriver();
        String baseUrl = url;
        String expectedTitle = title;
        String actualTitle = "";

        // launch Firefox and direct it to the Base URL
        driver.get(baseUrl);

        // get the actual value of the title
        actualTitle = driver.getTitle();

    /*
     * compare the actual title of the page witht the expected one and print
     * the result as "Passed" or "Failed"
     */
        if (actualTitle.contentEquals(expectedTitle)){
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed");
        }

        //close Firefox
        driver.close();
    }

    public void checkSearch(String url, String searchTerm) {
        WebDriver driver = new FirefoxDriver();
        driver.get(url);

        WebElement navSearchElement = driver.findElement(By.className("search-button"));
        WebElement searchFormInput = driver.findElement(By.className("live-search-field"));

        Actions builder = new Actions(driver);
        Action searchIconClick = builder
                .moveToElement(navSearchElement)
                .click()
                .build();

        Action search = builder
                .moveToElement(searchFormInput)
                .sendKeys(searchFormInput, searchTerm)
                .sendKeys(searchFormInput, Keys.ENTER)
                .build();

        searchIconClick.perform();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        search.perform();

        WebElement searchResults = driver.findElement(By.className("st-results"));

        List<WebElement> titles = searchResults.findElements(By.tagName("h3"));

        WebElement firstTitle = titles.get(0);

        System.out.println(firstTitle.getText());

        String titleExpected = "Meteor Pad: The JSfiddle for Meteor! :O";

        if (firstTitle.getText().contentEquals(titleExpected)) {
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed");
        }

        //close Firefox
        driver.close();
    }

}
