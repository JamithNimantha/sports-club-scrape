package com.debuggerme.fiverr.sportsclubscrape;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartScrape {

    private final RecordRepo recordRepo;

    private final Record2Repo record2Repo;

    @PostConstruct
    public void run(){
        scrapeListsOfSportsClubs();
    }


    private void scrapeListsOfSportsClubs(){

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
        driver.navigate().to("http://pagina.jccm.es/administracion_electronica/formularios/listadoRegistroEntidadesDeportivas.phtml");

        String[] array = {"02", "13", "16", "19", "45"};


//        for (String value : array) {
//            Select select = new Select(driver.findElement(By.xpath("//*[@id=\"BLANDDEP\"]")));
//            select.selectByValue(value);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            driver.findElement(By.xpath("//*[@id=\"BUSCAR\"]")).click();
//
//            WebElement elementBody = driver.findElement(By.xpath("//*[@id=\"Exportar_a_Excel\"]/tbody"));
//
//            List<WebElement> trs = elementBody.findElements(By.tagName("tr"));
//
//            trs.remove(0); // skip headers
//
//            for (WebElement tr : trs) {
//                List<WebElement> tds = tr.findElements(By.tagName("td"));
//
//                String reg = tds.get(0).getText();
//                String url = tds.get(0).findElement(By.tagName("a")).getAttribute("href");
//
//                Record2 record2 = new Record2();
//                record2.setRegNo(reg);
//                record2.setUrl(url);
//
//                if (record2Repo.existsByRegNo(reg)){
//                    log.warn("Reg No :{} already exists!", reg);
//                } else {
//                    record2Repo.save(record2);
//                    log.info("Fetched New Reg No :{}", reg);
//                }
//            }
//        }

        List<Record2> record2RepoAll = record2Repo.findAll();

        for (Record2 record : record2RepoAll) {

            driver.navigate().to(record.getUrl());

            WebElement element = driver.findElement(By.xpath("//*[contains(text(), 'Nombre Entidad:')]"));

            try {
                record.setNombre(driver.findElement(By.xpath("//*[contains(text(), 'Nombre Entidad:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setDireccion(driver.findElement(By.xpath("//*[contains(text(), 'Dirección:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setProvincia(driver.findElement(By.xpath("//*[contains(text(), 'Provincia:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setMunicipio(driver.findElement(By.xpath("//*[contains(text(), 'Municipio:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setPostal(driver.findElement(By.xpath("//*[contains(text(), 'Código postal:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setEmail(driver.findElement(By.xpath("//*[contains(text(), 'Email:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setTelefono(driver.findElement(By.xpath("//*[contains(text(), 'Teléfono móvil:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setModalidad1(driver.findElement(By.xpath("//*[contains(text(), '1.- MODALIDAD:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setModalidad2(driver.findElement(By.xpath("//*[contains(text(), '2.- MODALIDAD:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }

            try {
                record.setModalidad3(driver.findElement(By.xpath("//*[contains(text(), '3.- MODALIDAD:')]/following-sibling::span")).getText().trim());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage());
            }


            record2Repo.save(record);

            log.info("Updated record {}", record);


        }


    }

    private void scrapeClubs(){
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
        driver.navigate().to("https://deporteasturiano.org/federaciones-y-clubes/guia-de-clubes/");




        WebElement tableElement = driver.findElement(By.xpath("//*[@id=\"table_1\"]/tbody"));
        List<WebElement> trs = tableElement.findElements(By.tagName("tr"));
        log.error("Found {} Rows", trs.size());


        for (WebElement tr : trs) {
            List<WebElement> tds = tr.findElements(By.tagName("td"));

            Record record = new Record();
            record.setActividad(tds.get(0).getText().trim());
            record.setNombre(tds.get(1).getText().trim());
            record.setDireccion(tds.get(2).getText().trim());
            record.setCp(tds.get(3).getText().trim());
            record.setLocalidad(tds.get(4).getText().trim());
            record.setTelefono(tds.get(5).getText().trim());
            record.setWeb(tds.get(6).getText().trim());

            record = recordRepo.save(record);
            log.info("Saved New Record {}", record);

        }
        driver.quit();
    }
}
