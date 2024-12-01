package me.Votifier.server.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.StateSummary;

@Repository
public interface StateSummaryRepository extends MongoRepository<StateSummary, String> {
    StateSummary findByNAME(String NAME);
}

