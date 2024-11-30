package me.Votifier.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.DistrictsBoundary;

@Repository
public interface DistrictsBoundaryRepository extends MongoRepository<DistrictsBoundary, String> {
    DistrictsBoundary findByNAME(String NAME);
}