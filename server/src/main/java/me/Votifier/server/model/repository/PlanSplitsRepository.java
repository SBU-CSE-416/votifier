package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import me.Votifier.server.model.documents.PlanSplitsDocuments.PlanSplitsAnalysis;

@Repository

public interface PlanSplitsRepository extends MongoRepository<PlanSplitsAnalysis, String> {
   @Query(
      value = "{ 'NAME': ?0 }", 
      fields = "{ 'data': 1, '_id': 0 }"
   )
    PlanSplitsAnalysis findByNameAndRace(String NAME);
}