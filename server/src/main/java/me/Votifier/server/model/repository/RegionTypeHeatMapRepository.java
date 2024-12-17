package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.HeatmapDocuments.RegionTypeHeatMap;



@Repository
public interface RegionTypeHeatMapRepository extends MongoRepository<RegionTypeHeatMap, String> {

    @Query(value = "{ 'NAME': ?0}")
    RegionTypeHeatMap findByName(String NAME);
}