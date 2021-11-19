package com.debuggerme.fiverr.sportsclubscrape;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class LariojaDotOrgScrape {

    private final LariojaRecordRepo lariojaRecordRepo;

    private WebDriver driver;

    ArrayList<String> tabs;

    @PostConstruct
    public void run(){
        scrapeClubs();
        scrapeInfo();

    }

    private void scrapeInfo(){

        List<LariojaRecord> recordRepoAll = lariojaRecordRepo.findAll();
        for (LariojaRecord lariojaRecord : recordRepoAll) {
            driver.navigate().to(lariojaRecord.getUrl());
            try {
                lariojaRecord.setRegNo(driver.findElement(By.xpath("//*[@id=\"idcampos1\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
               log.error("No Reg Number Found!");
            }

            try {
                lariojaRecord.setEntityName(driver.findElement(By.xpath("//*[@id=\"idcampos2\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No Entity Name Found!");
            }

            try {
                lariojaRecord.setAddress(driver.findElement(By.xpath("//*[@id=\"idcampos3\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No Addreess Name Found!");
            }

            try {
                lariojaRecord.setPostalCode(driver.findElement(By.xpath("//*[@id=\"idcampos4\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No Postal Code Found!");
            }

            try {
                lariojaRecord.setPopulation(driver.findElement(By.xpath("//*[@id=\"idcampos5\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No Population  Found!");
            }

            try {
                lariojaRecord.setTelephone(driver.findElement(By.xpath("//*[@id=\"idcampos6\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No telephone  Found!");
            }

            try {
                lariojaRecord.setFax(driver.findElement(By.xpath("//*[@id=\"idcampos7\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No Population  Found!");
            }

            try {
                lariojaRecord.setEmail(driver.findElement(By.xpath("/html/body/form/div[2]/div[2]/div/fieldset/div/div[8]/a")).getText());
            } catch (NoSuchElementException e) {
                log.error("No Email  Found!");
            }

            try {
                lariojaRecord.setSports(driver.findElement(By.xpath("//*[@id=\"idcampos9\"]")).getAttribute("value"));
            } catch (NoSuchElementException e) {
                log.error("No Sports  Found!");
            }

            lariojaRecordRepo.save(lariojaRecord);
            log.info("Updated: {}", lariojaRecord);
        }
    }

    private void scrapeClubs(){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        ((JavascriptExecutor)driver).executeScript("window.open()");
//         tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.get("https://ias1.larioja.org/cex/sistemas/ede/busqueda.jsp");
        WebElement filterElement = driver.findElement(By.xpath("//*[@id=\"filtrarDeporte\"]"));
        Select select = new Select(filterElement);
        List<WebElement> optionsList = select.getOptions();
        for (int index = 1; index < optionsList.size(); index++) {
            WebElement filterSport = driver.findElement(By.xpath("//*[@id=\"filtrarDeporte\"]"));
            Select selectSport = new Select(filterSport);
            selectSport.selectByIndex(index);

            driver.findElement(By.xpath("//*[@id=\"btnFiltrar\"]")).click();

            boolean hasNext = true;

            try {
                while (hasNext){
                    List<WebElement> trs = driver.findElement(By.xpath("//*[@id=\"idTablaSubfi_oDatos\"]/tbody")).findElements(By.tagName("tr"));
                    for (WebElement tr : trs) {
                        String url = tr.findElement(By.tagName("a")).getAttribute("href");
                        LariojaRecord lariojaRecord = new LariojaRecord();
                        lariojaRecord.setUrl(url);


                        if (!lariojaRecordRepo.existsByUrl(url)) {
                            lariojaRecordRepo.save(lariojaRecord);
                            log.info("Found new Record {}", lariojaRecord);
                        } else {
                            log.error("URL {} already exists!", url);
                        }

                    }

                    WebElement element = null;
                    try {
                        element = driver.findElement(By.xpath("//*[@id=\"btnSiguiente\"]"));
                    } catch (Exception e) {
                        log.error("No Next Page!");
                    }

                    if (element != null){
                        log.info("Next Page is available!");
                        hasNext = true;
                        element.click();
                    } else {
                        hasNext = false;
                        log.info("Returning to Home!");
                        driver.get("https://ias1.larioja.org/cex/sistemas/ede/busqueda.jsp");
                    }
                }
            } catch (NoSuchElementException e) {
                log.error("No Table Found!!");
                log.info("Returning to Home!");
                driver.get("https://ias1.larioja.org/cex/sistemas/ede/busqueda.jsp");
            }
        }
    }
}
