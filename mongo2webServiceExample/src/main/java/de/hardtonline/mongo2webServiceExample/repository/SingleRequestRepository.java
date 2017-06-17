package de.hardtonline.mongo2webServiceExample.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.hardtonline.mongo2webServiceExample.model.SingleRequest;

public interface SingleRequestRepository extends MongoRepository<SingleRequest, String> {
    public SingleRequest findById(String id);
    public List<SingleRequest> findByBatchId(String batchId);
    public List<SingleRequest> findByBatchIdAndStatus(String batchId, String status);
}