package me.Votifier.server.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import me.Votifier.server.model.StateSummary;

@Repository
public interface StateSummaryRepository extends MongoRepository<StateSummary, String> {
    StateSummary findByNAME(String NAME);
}

