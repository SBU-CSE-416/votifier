package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.GinglesIncomeDocuments.GinglesIncomeAnalysis;
@Repository
public interface GinglesIncomeRepository extends MongoRepository<GinglesIncomeAnalysis, String> {
    @Query(value = "{ 'NAME': ?0, 'data.REGION_TYPE': ?1 }", 
       fields = "{ 'NAME': 1, 'election': 1, 'candidates': 1, 'data': 1, 'lines': 1 }")
    GinglesIncomeAnalysis findByNameAndRegionType(String NAME, String REGION_TYPE);

    @Query(value = "{ 'NAME': ?0 }", fields = "{ 'NAME': 1, 'election': 1, 'candidates': 1, 'data': 1, 'lines': 1 }")
    GinglesIncomeAnalysis findByName(String NAME);
}