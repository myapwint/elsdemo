package com.es.elsdemo.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class SearchRequestBuilder {

	private SearchRequestBuilder() {
	}

	public static SearchRequest from(SearchPayload<?> payload) {
		Objects.requireNonNull(payload, "payload must not be null");

		Query query = buildQuery(payload.getFilters());
		List<SortOptions> sorters = payload.getSorters() == null
				? List.of()
				: payload.getSorters().stream()
				.map(SearchRequestBuilder::sortOption)
				.toList();

		return new SearchRequest(query, sorters);
	}

	private static Query buildQuery(List<FilterCriteria> filters) {
		if (filters == null || filters.isEmpty()) {
			return ElasticsearchQueryBuilder.matchAll();
		}

		ElasticsearchQueryBuilder.Bool bool = ElasticsearchQueryBuilder.bool();
		filters.stream()
				.map(SearchRequestBuilder::query)
				.forEach(bool::filter);
		return bool.build();
	}

	private static Query query(FilterCriteria filter) {
		Objects.requireNonNull(filter, "filter must not be null");

		return switch (normalizedType(filter.getType())) {
			case "equals", "term" -> ElasticsearchQueryBuilder.term(filter.getField(), filter.getValue());
			case "in", "terms" -> ElasticsearchQueryBuilder.terms(filter.getField(), values(filter));
			case "exists" -> ElasticsearchQueryBuilder.exists(filter.getField());
			case "match" -> ElasticsearchQueryBuilder.match(filter.getField(), stringValue(filter.getValue(), "value"));
			case "match_phrase", "phrase" -> ElasticsearchQueryBuilder.matchPhrase(filter.getField(), stringValue(filter.getValue(), "value"));
			case "prefix" -> ElasticsearchQueryBuilder.prefix(filter.getField(), stringValue(filter.getValue(), "value"));
			case "wildcard" -> ElasticsearchQueryBuilder.wildcard(filter.getField(), stringValue(filter.getValue(), "value"));
			case "range" -> rangeQuery(filter);
			default -> throw new IllegalArgumentException("unsupported filter type: " + filter.getType());
		};
	}

	private static Query rangeQuery(FilterCriteria filter) {
		Object from = filter.getFrom();
		Object to = filter.getTo();

		if (from instanceof Number || to instanceof Number) {
			return ElasticsearchQueryBuilder.numberRange(filter.getField(), doubleValue(from), doubleValue(to));
		}

		return ElasticsearchQueryBuilder.termRange(
				filter.getField(),
				from == null ? null : stringValue(from, "from"),
				to == null ? null : stringValue(to, "to"));
	}

	private static SortOptions sortOption(SorterCriteria sorter) {
		Objects.requireNonNull(sorter, "sorter must not be null");

		return SortOptions.of(sort -> sort.field(field -> field
				.field(requireText(sorter.getField(), "sorter.field"))
				.order(sortOrder(sorter.getDirection()))));
	}

	private static SortOrder sortOrder(String direction) {
		if (direction == null || direction.isBlank()) {
			return SortOrder.Asc;
		}

		return switch (direction.toLowerCase(Locale.ROOT)) {
			case "asc", "ascending" -> SortOrder.Asc;
			case "desc", "descending" -> SortOrder.Desc;
			default -> throw new IllegalArgumentException("unsupported sort direction: " + direction);
		};
	}

	private static String normalizedType(String type) {
		return requireText(type, "filter.type").toLowerCase(Locale.ROOT);
	}

	private static List<?> values(FilterCriteria filter) {
		if (filter.getValues() != null && !filter.getValues().isEmpty()) {
			return filter.getValues();
		}
		if (filter.getValue() instanceof List<?> list && !list.isEmpty()) {
			return list;
		}
		throw new IllegalArgumentException("terms filter requires values");
	}

	private static Double doubleValue(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number number) {
			return number.doubleValue();
		}
		throw new IllegalArgumentException("range bounds must both be numeric when either bound is numeric");
	}

	private static String stringValue(Object value, String name) {
		if (value == null) {
			throw new IllegalArgumentException(name + " must not be null");
		}
		return requireText(String.valueOf(value), name);
	}

	private static String requireText(String value, String name) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(name + " must not be blank");
		}
		return value;
	}
}
