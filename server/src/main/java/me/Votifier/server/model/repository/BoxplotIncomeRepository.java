package me.Votifier.server.model.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.BoxplotIncomeDocuments.BoxplotIncomeAnalysis;

@Repository

public interface BoxplotIncomeRepository extends MongoRepository<BoxplotIncomeAnalysis, String> {
    // Queries for 0_35K
    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.0_35K': { $exists: true }, 'data.ensemble_2.0_35K': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.0_35K': 1, 'data.ensemble_2.0_35K': 1, '_id': 0 }"
    )
    BoxplotIncomeAnalysis findIncome0_35KByName(String NAME);

    // Queries for 35K_60K

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.35K_60K': { $exists: true }, 'data.ensemble_2.35K_60K': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.35K_60K': 1, 'data.ensemble_2.35K_60K': 1, '_id': 0 }"
    )
    BoxplotIncomeAnalysis findIncome35K_60KByName(String NAME);

    // Queries for 60K_100K

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.60K_100K': { $exists: true }, 'data.ensemble_2.60K_100K': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.60K_100K': 1, 'data.ensemble_2.60K_100K': 1, '_id': 0 }"
    )

    BoxplotIncomeAnalysis findIncome60K_100KByName(String NAME);

    // Queries for 100K_125K

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.100K_125K': { $exists: true }, 'data.ensemble_2.100K_125K': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.100K_125K': 1, 'data.ensemble_2.100K_125K': 1, '_id': 0 }"
    )

    BoxplotIncomeAnalysis findIncome100K_125KByName(String NAME);

    // Queries for 125K_150K

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.125K_150K': { $exists: true }, 'data.ensemble_2.125K_150K': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.125K_150K': 1, 'data.ensemble_2.125K_150K': 1, '_id': 0 }"
    )

    BoxplotIncomeAnalysis findIncome125K_150KByName(String NAME);

    // Queries for 150K_MORE

    @Query(
        value = "{ 'NAME': ?0, 'data.ensemble_1.150K_MORE': { $exists: true }, 'data.ensemble_2.150K_MORE': { $exists: true } }", 
        fields = "{ 'data.ensemble_1.150K_MORE': 1, 'data.ensemble_2.150K_MORE': 1, '_id': 0 }"
    )

    BoxplotIncomeAnalysis findIncome150K_MOREByName(String NAME);


}
