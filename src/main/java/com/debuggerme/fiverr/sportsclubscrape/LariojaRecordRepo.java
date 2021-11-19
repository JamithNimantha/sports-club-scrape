package com.debuggerme.fiverr.sportsclubscrape;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LariojaRecordRepo extends JpaRepository<LariojaRecord, Long> {

    boolean existsByUrl(String url);
}
