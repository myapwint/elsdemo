package com.es.elsdemo.repository;


import com.es.elsdemo.document.Candidate;
import com.es.elsdemo.dto.CandidateDTO;
import com.es.elsdemo.search.SearchRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CandidateRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    public CandidateRepository(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public CandidateDTO save(CandidateDTO dto) {
        Candidate saved = elasticsearchOperations.save(toDocument(dto));
        return toDto(saved);
    }

    public List<CandidateDTO> search(SearchRequest request) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(request.query())
                .withSort(request.sorters())
                .build();

        return elasticsearchOperations.search(query, Candidate.class)
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .map(this::toDto)
                .toList();
    }

    private Candidate toDocument(CandidateDTO dto) {
        Candidate candidate = new Candidate();
        candidate.setId(dto.getId());
        candidate.setEmail(dto.getEmail());
        candidate.setName(dto.getName());
        candidate.setAddress(toDocumentAddress(dto.getAddress()));
        return candidate;
    }

    private com.es.elsdemo.document.Address toDocumentAddress(com.es.elsdemo.dto.Address address) {
        if (address == null) {
            return null;
        }

        com.es.elsdemo.document.Address documentAddress = new com.es.elsdemo.document.Address();
        documentAddress.setLine1(address.getLine1());
        documentAddress.setLine2(address.getLine2());
        documentAddress.setCity(address.getCity());
        documentAddress.setPostalCode(address.getPostalCode());
        documentAddress.setCountry(address.getCountry());
        return documentAddress;
    }

    private CandidateDTO toDto(Candidate candidate) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getId());
        dto.setEmail(candidate.getEmail());
        dto.setName(candidate.getName());
        dto.setAddress(toDtoAddress(candidate.getAddress()));
        return dto;
    }

    private com.es.elsdemo.dto.Address toDtoAddress(com.es.elsdemo.document.Address address) {
        if (address == null) {
            return null;
        }

        com.es.elsdemo.dto.Address dtoAddress = new com.es.elsdemo.dto.Address();
        dtoAddress.setLine1(address.getLine1());
        dtoAddress.setLine2(address.getLine2());
        dtoAddress.setCity(address.getCity());
        dtoAddress.setPostalCode(address.getPostalCode());
        dtoAddress.setCountry(address.getCountry());
        return dtoAddress;
    }
}
