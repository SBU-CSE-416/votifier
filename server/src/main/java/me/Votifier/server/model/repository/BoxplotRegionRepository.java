package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.BoxplotRegionDocuments.BoxplotRegionAnalysis;;

@Repository
public interface BoxplotRegionRepository extends MongoRepository<BoxplotRegionAnalysis, String> {
    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.RURAL': { $exists: true }, 'data.ensemble_2.RURAL': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.RURAL': 1, 'data.ensemble_2.RURAL': 1, '_id': 0 }"
    )
    BoxplotRegionAnalysis findRURALByName(String NAME);


    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.URBAN': { $exists: true }, 'data.ensemble_2.URBAN': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.URBAN': 1, 'data.ensemble_2.URBAN': 1, '_id': 0 }"
    )
    BoxplotRegionAnalysis findURBANByName(String NAME);

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.SUBURBAN': { $exists: true }, 'data.ensemble_2.SUBURBAN': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.SUBURBAN': 1, 'data.ensemble_2.SUBURBAN': 1, '_id': 0 }"
    )

    BoxplotRegionAnalysis findSUBURBANByName(String NAME);
}