
package lib ;

import com.google.gson.JsonObject;
import config.jsonParser;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class steps {


    private WebDriver driver;
    private JsonObject pageObject;

    private String productName;
    private String firmName ;


    public steps() {

        ChromeOptions options;
        System.setProperty("webdriver.chrome.driver", (new File("tools/drivers/chromedriver.exe")).getAbsolutePath());
        options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("start-maximized");
        options.addArguments("incognito");
        options.addArguments("no-sandbox");
        this.driver = new ChromeDriver(options);

    }

    @Given("^I open (\\w+(?: \\w+)*) page$")
    public void openPage(String flowKey) {

        JsonObject jsonObject = jsonParser.main();
        pageObject = jsonObject.get(flowKey).getAsJsonObject();
        String urlString = pageObject.get("url").getAsString();

        driver.get(urlString);
    }

    @When("^I click (\\w+(?: \\w+)*) button$")
    public void clickSignIn(String pageKey) {

        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        WebElement button = driver.findElement(By.xpath(pageElement));

        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            System.out.println("--Button is clicked--\n");
        } catch (Exception e) {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(button));
            button.click();
            System.out.println("--Button is clicked--\n");
        }

    }



    @Then("^I see (\\w+(?: \\w+)*) equals to \"([^\"]*)\"$")
    public void iSeeElement(String pageKey, String valueKey) {

        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        String element = driver.findElement(By.xpath(pageElement)).getText();
        Assert.assertEquals(valueKey,element);
        System.out.println("--"+ valueKey +" is checked--\n");

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
                System.out.println("--Text is in the page--\n");
            }
            else {
                System.out.println("--Text is not in the page--\n");
            }
        }

    }


    @Then("^I see title is \"([^\"]*)\"$")
    public void iSeeTitle(String pageTitle) {

        String title = driver.getTitle();

        if(title.contains(pageTitle)){
            System.out.println("--" + pageTitle + " is tested--\n");
        }
        else{
            System.out.println("--Title failed--");
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
            System.out.println("--Product is same with added product to the favorites--\n");
        }
        catch (Exception e){
            System.out.println("--Product is different with added product to the favorites--\n");
            System.out.println("--Test failed--");
        }

    }

    @When("^I wait for page$")
    public void iWaitForPage() {

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }
}

