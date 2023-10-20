package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.List;

public class Assignment {

    public static void main(String[] args)  {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
        driver.manage().window().maximize();
        driver.findElement(By.xpath("//summary[text()='Table Data']")).click();
        WebElement jsonDataInputText = driver.findElement(By.xpath("//textarea[@id='jsondata']"));
        jsonDataInputText.click();
        jsonDataInputText.clear();
        String jsonData = "[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, " +
                "{\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, " +
                "{\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, " +
                "{\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, " +
                "{\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]";

        JsonArray jsonAPIData = JsonParser.parseString(jsonData).getAsJsonObject().getAsJsonArray("data");


        jsonDataInputText.sendKeys(jsonData);
        WebElement refreshBtn = driver.findElement(By.xpath("//button[text()='Refresh Table']"));
        System.out.println(jsonData);
        js.executeScript("arguments[0].scrollIntoView();", refreshBtn);
        refreshBtn.click();
        WebElement dynamicTable = driver.findElement(By.xpath("//table[@id='dynamictable']"));
        List<WebElement> rows = dynamicTable.findElements(By.tagName("tr"));

        String getTableData = dynamicTable.getText().substring(13);
        for (int i = 1; i < rows.size(); i++) {
            List<WebElement> columns = rows.get(i).findElements(By.tagName("td"));
            String name = columns.get(0).getText();
            String age = columns.get(1).getText();
            String gender = columns.get(1).getText();

            dynamicTable jsonRow = jsonData.get(i - 1).getAsJsonObject();
            String expectedName = jsonRow.get("name").getAsString();
            String expectedAge = jsonRow.get("age").getAsString();
            String expectedgender = jsonRow.get("gender").getAsString();


            if (name.equals(expectedName) && age.equals(expectedAge)) {
                System.out.println("Row " + i + " matches JSON");
            } else {
                System.out.println("Row " + i + " does not match JSON");
            }
        }
        System.out.println(getTableData);
        Assert.assertTrue(getTableData.contains(jsonData));
        driver.close();

    }
}
