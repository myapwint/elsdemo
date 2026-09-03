package com.es.elsdemo.repository;

import com.es.elsdemo.document.Job;
import com.es.elsdemo.dto.JobDTO;
import com.es.elsdemo.search.SearchRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobRepository {

	private final ElasticsearchOperations elasticsearchOperations;

	public JobRepository(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	public JobDTO save(JobDTO dto) {
		Job saved = elasticsearchOperations.save(toDocument(dto));
		return toDto(saved);
	}

	public List<JobDTO> search(SearchRequest request) {
		NativeQuery query = NativeQuery.builder()
				.withQuery(request.query())
				.withSort(request.sorters())
				.build();

		return elasticsearchOperations.search(query, Job.class)
				.getSearchHits()
				.stream()
				.map(SearchHit::getContent)
				.map(this::toDto)
				.toList();
	}

	private Job toDocument(JobDTO dto) {
		Job job = new Job();
		job.setId(dto.getId());
		job.setAboutCompany(dto.getAboutCompany());
		job.setCompanyDescription(dto.getCompanyDescription());
		job.setAboutJob(dto.getAboutJob());
		job.setJobDescription(dto.getJobDescription());
		job.setHowWeWork(dto.getHowWeWork());
		job.setWhatWillYouBeWorkingOn(dto.getWhatWillYouBeWorkingOn());
		job.setQualifications(dto.getQualifications());
		job.setNiceToHave(dto.getNiceToHave());
		job.setAdditionalInformation(dto.getAdditionalInformation());
		job.setWhatDoWeOffer(dto.getWhatDoWeOffer());
		return job;
	}

	private JobDTO toDto(Job job) {
		JobDTO dto = new JobDTO();
		dto.setId(job.getId());
		dto.setAboutCompany(job.getAboutCompany());
		dto.setCompanyDescription(job.getCompanyDescription());
		dto.setAboutJob(job.getAboutJob());
		dto.setJobDescription(job.getJobDescription());
		dto.setHowWeWork(job.getHowWeWork());
		dto.setWhatWillYouBeWorkingOn(job.getWhatWillYouBeWorkingOn());
		dto.setQualifications(job.getQualifications());
		dto.setNiceToHave(job.getNiceToHave());
		dto.setAdditionalInformation(job.getAdditionalInformation());
		dto.setWhatDoWeOffer(job.getWhatDoWeOffer());
		return dto;
	}
}
