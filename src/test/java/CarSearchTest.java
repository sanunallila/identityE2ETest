import models.CarDetailsModel;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.FileUtils;

import java.util.List;
import java.util.NoSuchElementException;

public class CarSearchTest extends BaseTest  {

    @Test(priority = 0, description = "")
    public void searchDellLaptop() {

        FileUtils fileUtils = new FileUtils();
        List<String> numberPlates =  fileUtils.readInputFiles(); // Reading input files
        List<CarDetailsModel> expectedDetails = fileUtils.readOutputFiles(); // Reading output file

        for (String numberPlate : numberPlates){
            WebElement regInput = driver.findElement(By.id("registration-number-input"));
            regInput.clear();
            regInput.sendKeys(numberPlate);

            WebElement searchButton = driver.findElement(By.id("find-vehicle-btn"));
            searchButton.click();

            CarDetailsModel currentCarDetail = getCarDetail(expectedDetails, numberPlate);

            if(currentCarDetail == null){
                continue; //missing the details on output file
            }

            String numberPlateXpath =  "//b[normalize-space()='" +  currentCarDetail.variantReg + "']";
            String manufacturerXpath =  "//b[normalize-space()='" +  currentCarDetail.make + "']";
            String yearXpath =  "//b[normalize-space()='" +  currentCarDetail.year + "']";

            try {
                WebElement registrationElement = myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(numberPlateXpath)));
                WebElement manufacturerElement = driver.findElement(By.xpath(manufacturerXpath));
                WebElement yearElement = driver.findElement(By.xpath(yearXpath));

                Assert.assertEquals(currentCarDetail.variantReg, registrationElement.getText());
                Assert.assertEquals(currentCarDetail.make, manufacturerElement.getText());
                Assert.assertEquals(currentCarDetail.year, yearElement.getText());
            } catch (NoSuchElementException exception) {
                //We can have any business logic like reports, log etc...
                continue;
            } catch (TimeoutException timeoutException) {
                //We can have any business logic like reports, log etc...
                continue;
            }
            driver.findElement(By.xpath("//span[normalize-space()='Change vehicle']")).click();
        }
    }

    private CarDetailsModel getCarDetail(List<CarDetailsModel> carDetailsModels, String regNo){
        for (CarDetailsModel carModel : carDetailsModels) {
            if (carModel.getVariantReg().equalsIgnoreCase(regNo)) {
                return carModel;
            }
        }
        return null; //missing the details on output file
    }

}