package me.Votifier.server.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.EcologicalInferenceIncomeDocuments.EIIncomeAnalysis;

@Repository
public interface EIIncomeRepository extends MongoRepository<EIIncomeAnalysis, String> {

       @Query(
              value = "{ 'NAME': ?0, 'data.0_35K.REPUBLICAN.ALL': { $exists: true }, 'data.0_35K.DEMOCRATIC.ALL': { $exists: true } }",
              fields = "{ 'data.0_35K.REPUBLICAN.ALL': 1, 'data.0_35K.DEMOCRATIC.ALL': 1 }"
       )
       EIIncomeAnalysis findIncome0_35KByNameAndAll(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.0_35K.REPUBLICAN.RURAL': { $exists: true }, 'data.0_35K.DEMOCRATIC.RURAL': { $exists: true } }", 
              fields = "{ 'data.0_35K.REPUBLICAN.RURAL': 1, 'data.0_35K.DEMOCRATIC.RURAL': 1 }")
       EIIncomeAnalysis findIncome0_35KByNameAndRural(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.0_35K.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.0_35K.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
              fields = "{ 'data.0_35K.REPUBLICAN.SUBURBAN': 1, 'data.0_35K.DEMOCRATIC.SUBURBAN': 1 }")
       EIIncomeAnalysis findIncome0_35KByNameAndSuburban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.0_35K.REPUBLICAN.URBAN': { $exists: true }, 'data.0_35K.DEMOCRATIC.URBAN': { $exists: true } }", 
              fields = "{ 'data.0_35K.REPUBLICAN.URBAN': 1, 'data.0_35K.DEMOCRATIC.URBAN': 1 }")
       EIIncomeAnalysis findIncome0_35KByNameAndUrban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.35K_60K.REPUBLICAN.ALL': { $exists: true }, 'data.35K_60K.DEMOCRATIC.ALL': { $exists: true } }", 
              fields = "{ 'data.35K_60K.REPUBLICAN.ALL': 1, 'data.35K_60K.DEMOCRATIC.ALL': 1 }")
       EIIncomeAnalysis findIncome35K_60KByNameAndAll(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.35K_60K.REPUBLICAN.RURAL': { $exists: true }, 'data.35K_60K.DEMOCRATIC.RURAL': { $exists: true } }", 
              fields = "{ 'data.35K_60K.REPUBLICAN.RURAL': 1, 'data.35K_60K.DEMOCRATIC.RURAL': 1 }")
       EIIncomeAnalysis findIncome35K_60KByNameAndRural(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.35K_60K.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.35K_60K.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
              fields = "{ 'data.35K_60K.REPUBLICAN.SUBURBAN': 1, 'data.35K_60K.DEMOCRATIC.SUBURBAN': 1 }")
       EIIncomeAnalysis findIncome35K_60KByNameAndSuburban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.35K_60K.REPUBLICAN.URBAN': { $exists: true }, 'data.35K_60K.DEMOCRATIC.URBAN': { $exists: true } }", 
              fields = "{ 'data.35K_60K.REPUBLICAN.URBAN': 1, 'data.35K_60K.DEMOCRATIC.URBAN': 1 }")
       EIIncomeAnalysis findIncome35K_60KByNameAndUrban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.60K_100K.REPUBLICAN.ALL': { $exists: true }, 'data.60K_100K.DEMOCRATIC.ALL': { $exists: true } }", 
              fields = "{ 'data.60K_100K.REPUBLICAN.ALL': 1, 'data.60K_100K.DEMOCRATIC.ALL': 1 }")
       EIIncomeAnalysis findIncome60K_100KByNameAndAll(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.60K_100K.REPUBLICAN.RURAL': { $exists: true }, 'data.60K_100K.DEMOCRATIC.RURAL': { $exists: true } }", 
              fields = "{ 'data.60K_100K.REPUBLICAN.RURAL': 1, 'data.60K_100K.DEMOCRATIC.RURAL': 1 }")
       EIIncomeAnalysis findIncome60K_100KByNameAndRural(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.60K_100K.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.60K_100K.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
              fields = "{ 'data.60K_100K.REPUBLICAN.SUBURBAN': 1, 'data.60K_100K.DEMOCRATIC.SUBURBAN': 1 }")
       EIIncomeAnalysis findIncome60K_100KByNameAndSuburban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.60K_100K.REPUBLICAN.URBAN': { $exists: true }, 'data.60K_100K.DEMOCRATIC.URBAN': { $exists: true } }", 
              fields = "{ 'data.60K_100K.REPUBLICAN.URBAN': 1, 'data.60K_100K.DEMOCRATIC.URBAN': 1 }")
       EIIncomeAnalysis findIncome60K_100KByNameAndUrban(String NAME);
       @Query(value = "{ 'NAME': ?0, 'data.100K_125K.REPUBLICAN.ALL': { $exists: true }, 'data.100K_125K.DEMOCRATIC.ALL': { $exists: true } }", 
       fields = "{ 'data.100K_125K.REPUBLICAN.ALL': 1, 'data.100K_125K.DEMOCRATIC.ALL': 1 }")
       EIIncomeAnalysis findIncome100K_125KByNameAndAll(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.100K_125K.REPUBLICAN.RURAL': { $exists: true }, 'data.100K_125K.DEMOCRATIC.RURAL': { $exists: true } }", 
       fields = "{ 'data.100K_125K.REPUBLICAN.RURAL': 1, 'data.100K_125K.DEMOCRATIC.RURAL': 1 }")
       EIIncomeAnalysis findIncome100K_125KByNameAndRural(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.100K_125K.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.100K_125K.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
       fields = "{ 'data.100K_125K.REPUBLICAN.SUBURBAN': 1, 'data.100K_125K.DEMOCRATIC.SUBURBAN': 1 }")
       EIIncomeAnalysis findIncome100K_125KByNameAndSuburban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.100K_125K.REPUBLICAN.URBAN': { $exists: true }, 'data.100K_125K.DEMOCRATIC.URBAN': { $exists: true } }", 
       fields = "{ 'data.100K_125K.REPUBLICAN.URBAN': 1, 'data.100K_125K.DEMOCRATIC.URBAN': 1 }")
       EIIncomeAnalysis findIncome100K_125KByNameAndUrban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.125K_150K.REPUBLICAN.ALL': { $exists: true }, 'data.125K_150K.DEMOCRATIC.ALL': { $exists: true } }", 
       fields = "{ 'data.125K_150K.REPUBLICAN.ALL': 1, 'data.125K_150K.DEMOCRATIC.ALL': 1 }")
       EIIncomeAnalysis findIncome125K_150KByNameAndAll(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.125K_150K.REPUBLICAN.RURAL': { $exists: true }, 'data.125K_150K.DEMOCRATIC.RURAL': { $exists: true } }", 
       fields = "{ 'data.125K_150K.REPUBLICAN.RURAL': 1, 'data.125K_150K.DEMOCRATIC.RURAL': 1 }")
       EIIncomeAnalysis findIncome125K_150KByNameAndRural(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.125K_150K.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.125K_150K.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
       fields = "{ 'data.125K_150K.REPUBLICAN.SUBURBAN': 1, 'data.125K_150K.DEMOCRATIC.SUBURBAN': 1 }")
       EIIncomeAnalysis findIncome125K_150KByNameAndSuburban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.125K_150K.REPUBLICAN.URBAN': { $exists: true }, 'data.125K_150K.DEMOCRATIC.URBAN': { $exists: true } }", 
       fields = "{ 'data.125K_150K.REPUBLICAN.URBAN': 1, 'data.125K_150K.DEMOCRATIC.URBAN': 1 }")
       EIIncomeAnalysis findIncome125K_150KByNameAndUrban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.150K_MORE.REPUBLICAN.ALL': { $exists: true }, 'data.150K_MORE.DEMOCRATIC.ALL': { $exists: true } }", 
       fields = "{ 'data.150K_MORE.REPUBLICAN.ALL': 1, 'data.150K_MORE.DEMOCRATIC.ALL': 1 }")
       EIIncomeAnalysis findIncome150K_MOREByNameAndAll(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.150K_MORE.REPUBLICAN.RURAL': { $exists: true }, 'data.150K_MORE.DEMOCRATIC.RURAL': { $exists: true } }", 
       fields = "{ 'data.150K_MORE.REPUBLICAN.RURAL': 1, 'data.150K_MORE.DEMOCRATIC.RURAL': 1 }")
       EIIncomeAnalysis findIncome150K_MOREByNameAndRural(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.150K_MORE.REPUBLICAN.SUBURBAN': { $exists: true }, 'data.150K_MORE.DEMOCRATIC.SUBURBAN': { $exists: true } }", 
       fields = "{ 'data.150K_MORE.REPUBLICAN.SUBURBAN': 1, 'data.150K_MORE.DEMOCRATIC.SUBURBAN': 1 }")
       EIIncomeAnalysis findIncome150K_MOREByNameAndSuburban(String NAME);

       @Query(value = "{ 'NAME': ?0, 'data.150K_MORE.REPUBLICAN.URBAN': { $exists: true }, 'data.150K_MORE.DEMOCRATIC.URBAN': { $exists: true } }", 
       fields = "{ 'data.150K_MORE.REPUBLICAN.URBAN': 1, 'data.150K_MORE.DEMOCRATIC.URBAN': 1 }")
       EIIncomeAnalysis findIncome150K_MOREByNameAndUrban(String NAME);
}

