package de.hardtonline.worker1.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.hardtonline.worker1.model.Batch;

public interface BatchRepository extends MongoRepository<Batch, String> {

    public Batch findById(String id);
    public List<Batch> findByConsumer(String consumer);
}