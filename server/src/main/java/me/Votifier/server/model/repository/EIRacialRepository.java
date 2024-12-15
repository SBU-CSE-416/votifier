package me.Votifier.server.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.EcologicalInferenceRacialDocuments.EIRacialAnalysis;

@Repository
public interface EIRacialRepository extends MongoRepository<EIRacialAnalysis, String> {

    // Queries for WHITE
    @Query(value = "{ 'NAME': ?0, 'data.WHITE.REPUBLICAN.ALL': { $exists: true }, 'data.WHITE.DEMOCRATIC.ALL': { $exists: true } }", 
           fields = "{ 'data.WHITE.REPUBLICAN.ALL': 1, 'data.WHITE.DEMOCRATIC.ALL': 1 }")
    EIRacialAnalysis findWhiteByNameAndAll(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.WHITE.REPUBLICAN.RURAL': { $exists: true }, 'data.WHITE.DEMOCRATIC.RURAL': { $exists: true } }", 
           fields = "{ 'data.WHITE.REPUBLICAN.RURAL': 1, 'data.WHITE.DEMOCRATIC.RURAL': 1 }")
    EIRacialAnalysis findWhiteByNameAndRural(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.WHITE.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.WHITE.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
           fields = "{ 'data.WHITE.REPUBLICAN.SUBURBAN': 1, 'data.WHITE.DEMOCRATIC.SUBURBAN': 1 }")
    EIRacialAnalysis findWhiteByNameAndSuburban(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.WHITE.REPUBLICAN.URBAN': { $exists: true }, 'data.WHITE.DEMOCRATIC.URBAN': { $exists: true } }", 
           fields = "{ 'data.WHITE.REPUBLICAN.URBAN': 1, 'data.WHITE.DEMOCRATIC.URBAN': 1 }")
    EIRacialAnalysis findWhiteByNameAndUrban(String NAME);

    // Queries for BLACK
    @Query(value = "{ 'NAME': ?0, 'data.BLACK.REPUBLICAN.ALL': { $exists: true }, 'data.BLACK.DEMOCRATIC.ALL': { $exists: true } }", 
           fields = "{ 'data.BLACK.REPUBLICAN.ALL': 1, 'data.BLACK.DEMOCRATIC.ALL': 1 }")
    EIRacialAnalysis findBlackByNameAndAll(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.BLACK.REPUBLICAN.RURAL': { $exists: true }, 'data.BLACK.DEMOCRATIC.RURAL': { $exists: true } }", 
           fields = "{ 'data.BLACK.REPUBLICAN.RURAL': 1, 'data.BLACK.DEMOCRATIC.RURAL': 1 }")
    EIRacialAnalysis findBlackByNameAndRural(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.BLACK.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.BLACK.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
           fields = "{ 'data.BLACK.REPUBLICAN.SUBURBAN': 1, 'data.BLACK.DEMOCRATIC.SUBURBAN': 1 }")
    EIRacialAnalysis findBlackByNameAndSuburban(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.BLACK.REPUBLICAN.URBAN': { $exists: true }, 'data.BLACK.DEMOCRATIC.URBAN': { $exists: true } }", 
           fields = "{ 'data.BLACK.REPUBLICAN.URBAN': 1, 'data.BLACK.DEMOCRATIC.URBAN': 1 }")
    EIRacialAnalysis findBlackByNameAndUrban(String NAME);

    // Queries for ASIAN
    @Query(value = "{ 'NAME': ?0, 'data.ASIAN.REPUBLICAN.ALL': { $exists: true }, 'data.ASIAN.DEMOCRATIC.ALL': { $exists: true } }", 
           fields = "{ 'data.ASIAN.REPUBLICAN.ALL': 1, 'data.ASIAN.DEMOCRATIC.ALL': 1 }")
    EIRacialAnalysis findAsianByNameAndAll(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.ASIAN.REPUBLICAN.RURAL': { $exists: true }, 'data.ASIAN.DEMOCRATIC.RURAL': { $exists: true } }", 
           fields = "{ 'data.ASIAN.REPUBLICAN.RURAL': 1, 'data.ASIAN.DEMOCRATIC.RURAL': 1 }")
    EIRacialAnalysis findAsianByNameAndRural(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.ASIAN.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.ASIAN.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
           fields = "{ 'data.ASIAN.REPUBLICAN.SUBURBAN': 1, 'data.ASIAN.DEMOCRATIC.SUBURBAN': 1 }")
    EIRacialAnalysis findAsianByNameAndSuburban(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.ASIAN.REPUBLICAN.URBAN': { $exists: true }, 'data.ASIAN.DEMOCRATIC.URBAN': { $exists: true } }", 
           fields = "{ 'data.ASIAN.REPUBLICAN.URBAN': 1, 'data.ASIAN.DEMOCRATIC.URBAN': 1 }")
    EIRacialAnalysis findAsianByNameAndUrban(String NAME);

    // Queries for HISPANIC
    @Query(value = "{ 'NAME': ?0, 'data.HISPANIC.REPUBLICAN.ALL': { $exists: true }, 'data.HISPANIC.DEMOCRATIC.ALL': { $exists: true } }", 
           fields = "{ 'data.HISPANIC.REPUBLICAN.ALL': 1, 'data.HISPANIC.DEMOCRATIC.ALL': 1 }")
    EIRacialAnalysis findHispanicByNameAndAll(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.HISPANIC.REPUBLICAN.RURAL': { $exists: true }, 'data.HISPANIC.DEMOCRATIC.RURAL': { $exists: true } }", 
           fields = "{ 'data.HISPANIC.REPUBLICAN.RURAL': 1, 'data.HISPANIC.DEMOCRATIC.RURAL': 1 }")
    EIRacialAnalysis findHispanicByNameAndRural(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.HISPANIC.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.HISPANIC.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
           fields = "{ 'data.HISPANIC.REPUBLICAN.SUBURBAN': 1, 'data.HISPANIC.DEMOCRATIC.SUBURBAN': 1 }")
    EIRacialAnalysis findHispanicByNameAndSuburban(String NAME);

    @Query(value = "{ 'NAME': ?0, 'data.HISPANIC.REPUBLICAN.URBAN': { $exists: true }, 'data.HISPANIC.DEMOCRATIC.URBAN': { $exists: true } }", 
           fields = "{ 'data.HISPANIC.REPUBLICAN.URBAN': 1, 'data.HISPANIC.DEMOCRATIC.URBAN': 1 }")
    EIRacialAnalysis findHispanicByNameAndUrban(String NAME);
}
