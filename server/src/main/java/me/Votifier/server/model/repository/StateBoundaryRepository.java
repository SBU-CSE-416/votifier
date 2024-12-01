package me.Votifier.server.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.documents.StateBoundary;

@Repository
public interface StateBoundaryRepository extends MongoRepository<StateBoundary, String> {
    StateBoundary findByNAME(String NAME);
}
