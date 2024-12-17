package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.HeatmapDocuments.RacialHeatMap;



@Repository
public interface RacialHeatMapRepository extends MongoRepository<RacialHeatMap, String> {

    @Query(value = "{ 'NAME': ?0}")
    RacialHeatMap findByName(String NAME);
}