package com.debuggerme.fiverr.sportsclubscrape.component;

import com.debuggerme.fiverr.sportsclubscrape.entity.JuntaDeAndaluciaRecord;
import com.debuggerme.fiverr.sportsclubscrape.repo.JuntaDeAndaluciaScrapeRecordRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JuntaDeAndaluciaScrape {

    private final JuntaDeAndaluciaScrapeRecordRepo repo;

    @PostConstruct
    public void run(){
        try {
            int offSet = 0;
            while (offSet <= 25840) {
                Document document = Jsoup
                        .connect(String.format("https://www.juntadeandalucia.es/deporte/dpweb/buscadorRaed/result?offset=%s&max=10", offSet))
                        .userAgent("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36")
                        .header("Accept-Encoding", "gzip,deflate,sdch")
                        .timeout(0)
                        .maxBodySize(0)
                        .ignoreContentType(true)
                        .get();

                Elements tBody = document.select("body > form > div.container > div.container > div:nth-child(1) > table > tbody");
                Elements trs = tBody.get(0).getElementsByTag("tr");
                for (int i = 0; i < trs.size(); i +=2) {
                    JuntaDeAndaluciaRecord record = new JuntaDeAndaluciaRecord();
                    Elements tds = trs.get(i).getElementsByTag("td");
                    String regNo = tds.get(1).text().trim();
                    record.setRegNo(regNo);
                    String denomination = tds.get(2).text().trim();
                    record.setDenomination(denomination);
                    String municipality = tds.get(3).text().trim();
                    record.setMunicipality(municipality);
                    Elements moreInfo = trs.get(i+1).getElementsByTag("td");
                    String regDate = moreInfo.get(0).getAllElements().textNodes().get(1).text().trim();
                    record.setRegDate(regDate);
                    String address = moreInfo.get(0).getAllElements().textNodes().get(3).text().trim();
                    record.setAddress(address);
                    String province = moreInfo.get(0).getAllElements().textNodes().get(5).text().trim();
                    record.setProvince(province);
                    String city = moreInfo.get(0).getAllElements().textNodes().get(7).text().trim();
                    record.setCity(city);
                    String zipCode = moreInfo.get(0).getAllElements().textNodes().get(9).text().trim();
                    record.setZipCode(zipCode);
                    String telephone = moreInfo.get(0).getAllElements().textNodes().get(11).text().trim();
                    record.setTelephone(telephone);
                    String fax = moreInfo.get(0).getAllElements().textNodes().get(13).text().trim().replace("----","");
                    record.setFax(fax);
                    String email = moreInfo.get(0).getAllElements().textNodes().get(15).text().trim().replace("----","");
                    record.setEmail(email);
                    String entity = moreInfo.get(0).getAllElements().textNodes().get(17).text().trim();
                    record.setEntity(entity);
                    String sport = moreInfo.get(0).getAllElements().textNodes().get(19).text().trim();
                    record.setSport(sport);
                    String dateStatuteModification = moreInfo.get(0).getAllElements().textNodes().get(21).text().trim();
                    record.setDateStatuteModification(dateStatuteModification);
                    String otherSports = moreInfo.get(0).getAllElements().textNodes().get(23).text().trim();
                    record.setOtherSports(otherSports);

                    if (!repo.existsById(record.getRegNo())){
                        repo.save(record);
                        log.info("Saved New Record {}!", record.getRegNo());
                    } else {
                        log.info("{} record already exists!", record.getRegNo());
                    }



                }
                offSet += 10;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
