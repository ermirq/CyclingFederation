package com.zerogravitysolutions.core.results;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends MongoRepository<ResultDocument, String> {

    @Query("{ 'race.$id': ?0}")
    List<ResultDocument> findByRaceReferenceId(ObjectId raceId);
}
