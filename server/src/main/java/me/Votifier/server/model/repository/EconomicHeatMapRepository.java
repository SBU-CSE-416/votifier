package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.HeatmapDocuments.EconomicHeatMap;


@Repository
public interface EconomicHeatMapRepository extends MongoRepository<EconomicHeatMap, String> {

    @Query(value = "{ 'NAME': ?0}")
    EconomicHeatMap findByName(String NAME);
}