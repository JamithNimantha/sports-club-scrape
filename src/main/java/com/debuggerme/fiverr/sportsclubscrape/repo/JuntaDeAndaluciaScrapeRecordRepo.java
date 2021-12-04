package com.debuggerme.fiverr.sportsclubscrape.repo;


import com.debuggerme.fiverr.sportsclubscrape.entity.JuntaDeAndaluciaRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuntaDeAndaluciaScrapeRecordRepo extends CrudRepository<JuntaDeAndaluciaRecord, String> {
}
