package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.HeatmapDocuments.ElectionHeatMap;


@Repository
public interface ElectionHeatMapRepository extends MongoRepository<ElectionHeatMap, String> {

    @Query(value = "{ 'NAME': ?0}")
    ElectionHeatMap findByName(String NAME);
}