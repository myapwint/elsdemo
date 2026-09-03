package com.es.elsdemo.service;

import com.es.elsdemo.payload.JobPayload;
import com.es.elsdemo.repository.JobRepository;
import com.es.elsdemo.search.SearchRequestBuilder;
import com.es.elsdemo.standard.StandardApiService;
import com.es.elsdemo.standard.StandardRequestContainer;
import com.es.elsdemo.standard.StandardResponseContainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("jobApiService")
@RequiredArgsConstructor
public class JobApiService
		extends StandardApiService<StandardRequestContainer<JobPayload>, StandardResponseContainer> {

	private final JobRepository repo;

	@Override
	public StandardResponseContainer fetch(StandardRequestContainer<JobPayload> request) {
		return StandardResponseContainer.success(repo.search(SearchRequestBuilder.from(request.getPayload())));
	}

	@Override
	public StandardResponseContainer save(StandardRequestContainer<JobPayload> request) {
		return StandardResponseContainer.success(repo.save(request.getPayload().getDocument()));
	}
}
