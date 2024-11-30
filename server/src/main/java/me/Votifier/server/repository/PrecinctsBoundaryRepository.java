package me.Votifier.server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import me.Votifier.server.model.PrecinctsBoundary;

import java.util.List;
@Repository
public interface PrecinctsBoundaryRepository extends MongoRepository<PrecinctsBoundary, String> {
    List<PrecinctsBoundary> findAllByNAME(String NAME);
}