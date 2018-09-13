
package lib ;

import com.google.gson.JsonObject;
import config.jsonParser;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import cucumber.api.java.After;
import gherkin.formatter.model.DataTableRow;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class steps {

    private WebDriver driver;
    private JsonObject pageObject;

    private String productName;
    private String firmName ;

    private static int scenariosCounter = 0;
    private static int failedScenariosCounter = 0;

    private static Logger LOGGER = Logger.getLogger(steps.class);


    public steps() {

    }

    @Before
    public void beforeScenario(Scenario scenario) {
        ChromeOptions options;
        System.setProperty("webdriver.chrome.driver", (new File("tools/drivers/chromedriver.exe")).getAbsolutePath());
        options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("start-maximized");
        options.addArguments("incognito");
        options.addArguments("no-sandbox");
        this.driver = new ChromeDriver(options);

        LOGGER.info(String.format("\n\n\t[%d] > Scenario [%s] started\n\t", ++scenariosCounter, scenario.getName()));

    }

    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            ++failedScenariosCounter;
        } else
            driver.quit();

        String result = scenario.isFailed() ? "with errors" : "succesfully";
        LOGGER.info(String.format("\n\t[%d] > Scenario [%s] finished %s\t", scenariosCounter, scenario.getName(), result));
        LOGGER.info(String.format("\n\t%d of %d scenarios failed so far\t", failedScenariosCounter, scenariosCounter));

    }

    @Given("^I open (\\w+(?: \\w+)*) page$")
    public void openPage(String flowKey) {

        JsonObject jsonObject = jsonParser.main();
        pageObject = jsonObject.get(flowKey).getAsJsonObject();
        String urlString = pageObject.get("url").getAsString();
        driver.get(urlString);

        LOGGER.info(String.format("\n\tNavigate to the website: %s\n\t", urlString));
    }

    @When("^I click (\\w+(?: \\w+)*) button$")
    public void iClick(String pageKey) {

        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        WebElement button = driver.findElement(By.xpath(pageElement));

        try {

            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(button));

            Actions actions = new Actions(driver);
            actions.moveToElement(element).moveToElement(button).build().perform();

            actions.moveToElement(element).click().perform();
            LOGGER.info(String.format("\n\tClicking link with label %s\n\t", pageKey));

        } catch (Exception e) {

            LOGGER.info(String.format("\n\tClicking failed %s\n\t", pageKey));
        }

    }


    @Then("^I see (\\w+(?: \\w+)*) equals to \"([^\"]*)\"$")
    public void iSeeElement(String pageKey, String valueKey) {

        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        String element = driver.findElement(By.xpath(pageElement)).getText();
        Assert.assertEquals(valueKey,element);
        LOGGER.info(String.format("\n\tCheck web element: %s\n\t", pageKey));
    }


    @When("^I fill the (\\w+(?: \\w+)*)$")
    public void iFill(String pageKey, DataTable table)  {

        for (DataTableRow row : table.getGherkinRows()){

            String key = row.getCells().get(0);
            String value = row.getCells().get(1);

            JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(pageKey).getAsString();
            WebElement element = driver.findElement(By.xpath(pageElement));
            element.sendKeys(value);

            LOGGER.info(String.format("\n\tFilling the key: [%s] \t with the value: [%s]\n\t", key, value));

        }
    }

    @Then("^I see (\\w+(?: \\w+)*) contains text$")
    public void iSeeText(String pageKey, DataTable table) {

        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        String element= driver.findElement(By.xpath(pageElement)).getText();


        for (DataTableRow row : table.getGherkinRows()) {

            String key = row.getCells().get(0);
            boolean isThere = element.contains(key);

            if (isThere) {
                LOGGER.info(String.format("\n\tCheck web element: %s\n\t", pageKey));
            }
            else {
                throw new java.lang.AssertionError("No such element "+pageKey+" on the webpage!");
            }
        }

    }

    @Then("^I see webpage title is \"([^\"]*)\"$")
    public void iSeeTitle(String expectedTitle) {

        String actualTitle = driver.getTitle();

        if(actualTitle.contains(expectedTitle)){
            LOGGER.info(String.format("\n\tThe webpage title is [%s] as expected [%s]\n\t", actualTitle, expectedTitle));
        }
        else{
            throw new java.lang.AssertionError(String.format("\n\tThe webpage title is NOT [%s] as expected [%s]\n\t", actualTitle, expectedTitle));
        }
    }

    @And("^I save the (\\w+(?: \\w+)*) and (\\w+(?: \\w+)*) in the background$")
    public void iSaveTheProductInTheBackground(String pageKey, String pageKey2) {

        JsonObject pageElementObject1 = pageObject.get("elements").getAsJsonObject();
        String pageElement1 = pageElementObject1.get(pageKey).getAsString();
        productName = driver.findElement(By.xpath(pageElement1)).getText();

        JsonObject pageElementObject2 = pageObject.get("elements").getAsJsonObject();
        String pageElement2 = pageElementObject2.get(pageKey2).getAsString();
        firmName = driver.findElement(By.xpath(pageElement2)).getText();


    }


    @Then("^I compare the (\\w+(?: \\w+)*) and (\\w+(?: \\w+)*) is same$")
    public void iCompareTheProductIsSame(String pageKey1, String pageKey2) {

        JsonObject pageElementObject1 = pageObject.get("elements").getAsJsonObject();
        String pageElement1 = pageElementObject1.get(pageKey1).getAsString();
        String element1 = driver.findElement(By.xpath(pageElement1)).getText();

        JsonObject pageElementObject2 = pageObject.get("elements").getAsJsonObject();
        String pageElement2 = pageElementObject2.get(pageKey2).getAsString();
        String element2= driver.findElement(By.xpath(pageElement2)).getText();

        try{
            Assert.assertEquals(element1,productName);
            Assert.assertEquals(element2,firmName);
            LOGGER.info(String.format("\n\tThe product title is [%s] as expected [%s]\n\t", productName, pageElement1));
        }
        catch (Exception e){
            LOGGER.info(String.format("\n\tThe firm title is [%s] as expected [%s]\n\t", firmName, pageElement2));
        }

    }

    @Then("^I see the url is \"([^\"]*)\"$")
    public void iSeeTheUrlIs(String urlExpected) {

        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, urlExpected);

    }

    @When("^I wait for page$")
    public void iWaitForPage() {

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        LOGGER.info("\n\tWait for page\n\t");

    }

    @When("^I refresh the page$")
    public void iRefreshThePage() {

        try {
            String currentUrl = driver.getCurrentUrl();
            driver.navigate().refresh();
            LOGGER.info(String.format("\n\tThe page [ %s ] has been refreshed\n\t", currentUrl));
        } catch ( AssertionError e ) {
            e.printStackTrace();
        }

    }
}

