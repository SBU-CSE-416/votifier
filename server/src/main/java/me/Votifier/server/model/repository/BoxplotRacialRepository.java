package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.BoxplotRacialDocuments.BoxplotRacialAnalysis;
@Repository
public interface BoxplotRacialRepository extends MongoRepository<BoxplotRacialAnalysis, String> {
    // Queries for WHITE
    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.WHITE': { $exists: true }, 'data.ensemble_2.WHITE': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.WHITE': 1, 'data.ensemble_2.WHITE': 1, '_id': 0 }"
    )
    BoxplotRacialAnalysis findWhiteByName(String NAME);


    // Queries for BLACK

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.BLACK': { $exists: true }, 'data.ensemble_2.BLACK': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.BLACK': 1, 'data.ensemble_2.BLACK': 1, '_id': 0 }"
    )
    BoxplotRacialAnalysis findBlackByName(String NAME);

    // Queries for ASIAN

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.ASIAN': { $exists: true }, 'data.ensemble_2.ASIAN': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.ASIAN': 1, 'data.ensemble_2.ASIAN': 1, '_id': 0 }"
    )
    BoxplotRacialAnalysis findAsianByName(String NAME);

    // Queries for HISPANIC

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.HISPANIC': { $exists: true }, 'data.ensemble_2.HISPANIC': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.HISPANIC': 1, 'data.ensemble_2.HISPANIC': 1, '_id': 0 }"
    )
    BoxplotRacialAnalysis findHispanicByName(String NAME);
}

