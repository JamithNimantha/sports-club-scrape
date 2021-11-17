package com.debuggerme.fiverr.sportsclubscrape;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Record2Repo extends JpaRepository<Record2, String> {

    boolean existsByRegNo(String regNo);
}
