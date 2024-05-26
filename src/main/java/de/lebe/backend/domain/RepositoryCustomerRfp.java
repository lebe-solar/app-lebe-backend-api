package de.lebe.backend.domain;

import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

@Repository
public interface RepositoryCustomerRfp extends CosmosRepository<MCustomerRfp, String> {

}
