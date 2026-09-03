package com.es.elsdemo.service;

import com.es.elsdemo.payload.CandidatePayload;
import com.es.elsdemo.repository.CandidateRepository;
import com.es.elsdemo.search.SearchRequestBuilder;
import com.es.elsdemo.standard.StandardApiService;
import com.es.elsdemo.standard.StandardRequestContainer;
import com.es.elsdemo.standard.StandardResponseContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("candidateApiService")
@RequiredArgsConstructor
public class CandidateApiService
        extends StandardApiService<StandardRequestContainer<CandidatePayload>, StandardResponseContainer> {

    private final CandidateRepository repo;

    @Override
    public StandardResponseContainer fetch(StandardRequestContainer<CandidatePayload> request) {
        return StandardResponseContainer.success(repo.search(SearchRequestBuilder.from(request.getPayload())));
    }

    @Override
    public StandardResponseContainer save(StandardRequestContainer<CandidatePayload> request) {
        return StandardResponseContainer.success(repo.save(request.getPayload().getDocument()));
    }
}
