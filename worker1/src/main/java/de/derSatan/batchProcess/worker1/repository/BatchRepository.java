package de.derSatan.batchProcess.worker1.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import de.derSatan.batchProcess.worker1.model.Batch;
import de.derSatan.batchProcess.worker1.model.BatchStatus;

@Service
@Configurable
public interface BatchRepository extends MongoRepository<Batch, String> {

    public Batch findById(String id);
    public List<Batch> findByConsumer(String consumer);
    public List<Batch> findByBatchStatus(BatchStatus batchStatus);
}