package de.hardtonline.mongo2webServiceExample.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.hardtonline.mongo2webServiceExample.model.Batch;

public interface BatchRepository extends MongoRepository<Batch, String> {

    public Batch findById(String id);
    public List<Batch> findByConsumer(String consumer);
}