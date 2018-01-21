package de.derSatan.batchProcess.dispatcher.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.derSatan.batchProcess.dispatcher.model.SingleRequest;

public interface SingleRequestRepository extends MongoRepository<SingleRequest, String> {
    public SingleRequest findById(String id);
    public List<SingleRequest> findByBatchId(String batchId);
    public List<SingleRequest> findByBatchIdAndStatus(String batchId, String status);
}