package com.es.elsdemo.search;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchRequestBuilderTests {

	@Test
	void mapsUiPayloadFiltersAndSortersToElasticsearchRequestParts() {
		FilterCriteria filter = new FilterCriteria();
		filter.setField("name");
		filter.setType("equals");
		filter.setValue("david");

		SorterCriteria sorter = new SorterCriteria();
		sorter.setField("name");

		SearchPayload<?> payload = new SearchPayload<>();
		payload.setFilters(List.of(filter));
		payload.setSorters(List.of(sorter));

		SearchRequest request = SearchRequestBuilder.from(payload);

		Query query = request.query();
		assertTrue(query.isBool());
		assertEquals(1, query.bool().filter().size());
		assertTrue(query.bool().filter().getFirst().isTerm());
		assertEquals(1, request.sorters().size());
		assertTrue(request.sorters().getFirst().isField());
		assertEquals("name", request.sorters().getFirst().field().field());
		assertEquals(SortOrder.Asc, request.sorters().getFirst().field().order());
	}

	@Test
	void mapsFullTextAndRangeFilters() {
		FilterCriteria fullTextFilter = new FilterCriteria();
		fullTextFilter.setField("name");
		fullTextFilter.setType("match");
		fullTextFilter.setValue("david smith");

		FilterCriteria rangeFilter = new FilterCriteria();
		rangeFilter.setField("experience");
		rangeFilter.setType("range");
		rangeFilter.setFrom(3);
		rangeFilter.setTo(7);

		SearchPayload<?> payload = new SearchPayload<>();
		payload.setFilters(List.of(fullTextFilter, rangeFilter));

		SearchRequest request = SearchRequestBuilder.from(payload);

		assertTrue(request.query().isBool());
		assertEquals(2, request.query().bool().filter().size());
		assertTrue(request.query().bool().filter().getFirst().isMatch());
		assertTrue(request.query().bool().filter().get(1).isRange());
	}

	@Test
	void defaultsToMatchAllWhenFiltersAreMissing() {
		SearchRequest request = SearchRequestBuilder.from(new SearchPayload<>());

		assertTrue(request.query().isMatchAll());
		assertTrue(request.sorters().isEmpty());
	}

	@Test
	void rejectsUnsupportedFilterType() {
		FilterCriteria filter = new FilterCriteria();
		filter.setField("name");
		filter.setType("unsupported");
		filter.setValue("david");

		SearchPayload<?> payload = new SearchPayload<>();
		payload.setFilters(List.of(filter));

		assertThrows(IllegalArgumentException.class, () -> SearchRequestBuilder.from(payload));
	}
}
