package de.derSatan.batchProcess.dispatcher.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.derSatan.batchProcess.dispatcher.model.Batch;

public interface BatchRepository extends MongoRepository<Batch, String> {

    public Batch findById(String id);
    public List<Batch> findByConsumer(String consumer);
}