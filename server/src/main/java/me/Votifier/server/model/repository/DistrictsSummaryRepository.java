package me.Votifier.server.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.DistrictsSummary;

@Repository
public interface DistrictsSummaryRepository extends MongoRepository<DistrictsSummary, String> {
    DistrictsSummary findByName(String NAME);
}
