package com.debuggerme.fiverr.sportsclubscrape;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartScrape {

    private final RecordRepo recordRepo;

    private final Record2Repo record2Repo;

    @PostConstruct
    public void run(){
//        scrapeListsOfSportsClubs();
    }


    private void scrapeListsOfSportsClubs(){

//        FirefoxOptions options = new FirefoxOptions();
//        options.setHeadless(true);
//        WebDriver driver = new FirefoxDriver(options);
//        driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
//        driver.navigate().to("http://pagina.jccm.es/administracion_electronica/formularios/listadoRegistroEntidadesDeportivas.phtml");
//
//        String[] array = {"02", "13", "16", "19", "45"};


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

        
        
        List<Record2> record2RepoAll = record2Repo.findAllByProvinciaIsNull();
        Map<String, String> cookies = new HashMap<>();

        try {
            Connection.Response response = Jsoup.connect("http://pagina.jccm.es/administracion_electronica/formularios/detalleREGED.phtml?COD_REGISTRO=0000004186")
                    .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                    .header("Accept-Encoding", "gzip,deflate,sdch")
                    .timeout(0)
                    .maxBodySize(0)
                    .ignoreContentType(true)
                    .header("Cookie", "PHPSESSID=1bolcrtt3adgkim778kkc8k842; QueueITAccepted-SDFrts345E-V3_jccmadminelectronica=EventId=jccmadminelectronica&QueueId=fea05791-55f4-4bfa-87a1-cac7b2de6c5b&RedirectType=safetynet&IssueTime=1637201187&Hash=408c14756c656397520f82e1545e9ae586af1aa5fcd9c3ea650497f0806a1989")
                    .execute();

            cookies = response.cookies();

        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Record2 record : record2RepoAll) {

            log.info("Scraping URL {}", record.getUrl());
            try {
                Connection.Response response = Jsoup
                        .connect(record.getUrl())
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .header("Accept-Encoding", "gzip,deflate,sdch")
                        .timeout(0)
                        .maxBodySize(0)
                        .ignoreContentType(true)
                        .cookies(cookies)
                        .execute();

                cookies = response.cookies();

                Element element = response.parse().selectFirst("#area_contenidos_formu > div.contenido_azul");


                try {
                    record.setNombre(element.selectFirst("label:containsOwn(Nombre Entidad:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setDireccion(element.selectFirst("label:containsOwn(Dirección:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setProvincia(element.selectFirst("label:containsOwn(Provincia:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setMunicipio(element.selectFirst("label:containsOwn(Municipio:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setPostal(element.selectFirst("label:containsOwn(Código postal:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setEmail(element.selectFirst("label:containsOwn(Email:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setTelefono(element.selectFirst("label:containsOwn(Teléfono móvil:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setModalidad1(element.selectFirst("label:containsOwn(1.- MODALIDAD:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setModalidad2(element.selectFirst("label:containsOwn(2.- MODALIDAD:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }

                try {
                    record.setModalidad3(element.selectFirst("label:containsOwn(3.- MODALIDAD:)").nextElementSibling().text().trim());
                } catch (Exception e) {
//                log.error(e.getMessage());
                }


                record2Repo.save(record);

//                driver.quit();

                log.info("Updated record {}", record);
            } catch (IOException e) {
                e.printStackTrace();
            }


            


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
