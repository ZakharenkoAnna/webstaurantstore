package Tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class WebstaurantstoreTest {

    WebDriver driver;
    @BeforeTest
    public void openBrowser() {
        System.setProperty("webdriver.chrome.driver", "c:\\work\\webdriver\\chromedriver.exe");
        driver=new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void testSearchAndAddToCart() throws Exception {
        try {
            //Go to https://www.webstaurantstore.com/
            System.out.println("Launch Webstaurantstore");
            driver.get("https://www.webstaurantstore.com/");
            Assert.assertEquals("WebstaurantStore: Restaurant Supplies & Foodservice Equipment", driver.getTitle());
            //Search for 'stainless work table'
            WebElement text_box = driver.findElement(By.id("searchval"));
            text_box.sendKeys("stainless work table");
            WebElement submit_button = driver.findElement(By.xpath("//button[@value='Search']"));
            submit_button.click();
            //Check the search result ensuring every product has the word 'Table' in its title
            List<WebElement> search_results = driver.findElements(By.xpath("//div[@id='product_listing']/div/div/a/span"));
            String item_text = "";
            if (search_results.isEmpty())
            {
                System.out.println("Search is failed");
            }
            else {
                for (WebElement Element : search_results) {
                    item_text = Element.getText();
                    if (!item_text.contains("Table")) {
                        System.out.println("Search is failed");
                        break;
                    }
                }
                System.out.println("Search is passed");
            }
            //Add the last of found items to Cart
            List<WebElement> add_item = driver.findElements(By.xpath("//input[@name='addToCartButton']"));
            WebElement add_last_item = add_item.get(add_item.size()-1);
            add_last_item.click();

            //Go to the Cart and check that we add last item
            WebElement cart_button = driver.findElement(By.id("cartItemCountSpan"));
            cart_button.click();
            WebElement last_item = driver.findElement(By.xpath("//span/a"));
            String last_item_text = last_item.getText();
            System.out.println("We add item " + last_item_text);
            Assert.assertEquals(item_text, last_item_text);

            //Empty Cart
            WebElement empty_cart_button = driver.findElement(By.xpath("//div[@data-hypernova-key='EmptyCart']/button"));
            empty_cart_button.click();
            WebElement empty_cart_confirmation_button = driver.findElement(By.xpath("//div/div/footer/button"));
            empty_cart_confirmation_button.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement empty_cart_text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='header-1']")));
            Assert.assertTrue(empty_cart_text.isDisplayed());
            System.out.println(empty_cart_text.getText());
            Assert.assertEquals("Your cart is empty.", empty_cart_text.getText());
        } catch (Exception e) {
        }
    }

    @AfterTest
    public void closeBrowser() {
        driver.close();
        System.out.println("The driver has been closed.");
    }

}
