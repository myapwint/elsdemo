package com.es.elsdemo.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ElasticsearchQueryBuilderTests {

	@Test
	void buildsBooleanQueryWithCompoundClauses() {
		Query query = ElasticsearchQueryBuilder.bool()
				.must(ElasticsearchQueryBuilder.match("name", "john smith", Operator.And))
				.filter(ElasticsearchQueryBuilder.term("status", "active"))
				.should(ElasticsearchQueryBuilder.prefix("name", "jo"))
				.mustNot(ElasticsearchQueryBuilder.term("deleted", true))
				.minimumShouldMatch("1")
				.build();

		assertTrue(query.isBool());
		assertEquals(1, query.bool().must().size());
		assertEquals(1, query.bool().filter().size());
		assertEquals(1, query.bool().should().size());
		assertEquals(1, query.bool().mustNot().size());
		assertEquals("1", query.bool().minimumShouldMatch());
	}

	@Test
	void buildsTermLevelQueries() {
		assertTrue(ElasticsearchQueryBuilder.term("name", "john").isTerm());
		assertTrue(ElasticsearchQueryBuilder.terms("status", List.of("active", "pending")).isTerms());
		assertTrue(ElasticsearchQueryBuilder.exists("name").isExists());
		assertTrue(ElasticsearchQueryBuilder.longRange("experience", 3L, 7L).isRange());
		assertTrue(ElasticsearchQueryBuilder.wildcard("name", "jo*").isWildcard());
	}

	@Test
	void buildsFullTextQueries() {
		assertTrue(ElasticsearchQueryBuilder.match("name", "john").isMatch());
		assertTrue(ElasticsearchQueryBuilder.matchPhrase("name", "john smith").isMatchPhrase());
		assertTrue(ElasticsearchQueryBuilder.multiMatch("john", List.of("name", "summary")).isMultiMatch());
		assertTrue(ElasticsearchQueryBuilder.queryString("john AND smith", List.of("name", "summary")).isQueryString());
	}

	@Test
	void rejectsInvalidInput() {
		assertThrows(IllegalArgumentException.class, () -> ElasticsearchQueryBuilder.match(" ", "john"));
		assertThrows(IllegalArgumentException.class, () -> ElasticsearchQueryBuilder.terms("status", List.of()));
		assertThrows(IllegalArgumentException.class, () -> ElasticsearchQueryBuilder.longRange("experience", null, null));
	}
}
