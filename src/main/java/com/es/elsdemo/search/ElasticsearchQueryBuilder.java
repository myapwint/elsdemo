package com.es.elsdemo.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ElasticsearchQueryBuilder {

	private ElasticsearchQueryBuilder() {
	}

	public static Bool bool() {
		return new Bool();
	}

	public static Query matchAll() {
		return QueryBuilders.matchAll(matchAll -> matchAll);
	}

	public static Query match(String field, String query) {
		requireText(query, "query");
		return QueryBuilders.match(match -> match
				.field(requireText(field, "field"))
				.query(query));
	}

	public static Query match(String field, String query, Operator operator) {
		requireText(query, "query");
		return QueryBuilders.match(match -> match
				.field(requireText(field, "field"))
				.query(query)
				.operator(Objects.requireNonNull(operator, "operator must not be null")));
	}

	public static Query matchPhrase(String field, String query) {
		requireText(query, "query");
		return QueryBuilders.matchPhrase(matchPhrase -> matchPhrase
				.field(requireText(field, "field"))
				.query(query));
	}

	public static Query multiMatch(String query, List<String> fields) {
		requireText(query, "query");
		requireNotEmpty(fields, "fields");
		return QueryBuilders.multiMatch(multiMatch -> multiMatch
				.query(query)
				.fields(fields));
	}

	public static Query multiMatch(String query, List<String> fields, TextQueryType type) {
		requireText(query, "query");
		requireNotEmpty(fields, "fields");
		return QueryBuilders.multiMatch(multiMatch -> multiMatch
				.query(query)
				.fields(fields)
				.type(Objects.requireNonNull(type, "type must not be null")));
	}

	public static Query queryString(String query, List<String> fields) {
		requireText(query, "query");
		requireNotEmpty(fields, "fields");
		return QueryBuilders.queryString(queryString -> queryString
				.query(query)
				.fields(fields));
	}

	public static Query term(String field, Object value) {
		return QueryBuilders.term(term -> term
				.field(requireText(field, "field"))
				.value(fieldValue(value)));
	}

	public static Query terms(String field, List<?> values) {
		requireNotEmpty(values, "values");
		return QueryBuilders.terms(terms -> terms
				.field(requireText(field, "field"))
				.terms(termsField -> termsField.value(values.stream()
						.map(ElasticsearchQueryBuilder::fieldValue)
						.toList())));
	}

	public static Query exists(String field) {
		return QueryBuilders.exists(exists -> exists.field(requireText(field, "field")));
	}

	public static Query numberRange(String field, Double gte, Double lte) {
		requireAtLeastOneBound(gte, lte);
		return QueryBuilders.range(range -> range.number(number -> {
			number.field(requireText(field, "field"));
			if (gte != null) {
				number.gte(gte);
			}
			if (lte != null) {
				number.lte(lte);
			}
			return number;
		}));
	}

	public static Query longRange(String field, Long gte, Long lte) {
		requireAtLeastOneBound(gte, lte);
		return QueryBuilders.range(range -> range.longNumber(longNumber -> {
			longNumber.field(requireText(field, "field"));
			if (gte != null) {
				longNumber.gte(gte);
			}
			if (lte != null) {
				longNumber.lte(lte);
			}
			return longNumber;
		}));
	}

	public static Query termRange(String field, String gte, String lte) {
		requireAtLeastOneBound(gte, lte);
		return QueryBuilders.range(range -> range.term(term -> {
			term.field(requireText(field, "field"));
			if (gte != null) {
				term.gte(gte);
			}
			if (lte != null) {
				term.lte(lte);
			}
			return term;
		}));
	}

	public static Query prefix(String field, String value) {
		requireText(value, "value");
		return QueryBuilders.prefix(prefix -> prefix
				.field(requireText(field, "field"))
				.value(value));
	}

	public static Query wildcard(String field, String value) {
		requireText(value, "value");
		return QueryBuilders.wildcard(wildcard -> wildcard
				.field(requireText(field, "field"))
				.value(value));
	}

	public static final class Bool {

		private final List<Query> must = new ArrayList<>();
		private final List<Query> should = new ArrayList<>();
		private final List<Query> filter = new ArrayList<>();
		private final List<Query> mustNot = new ArrayList<>();
		private String minimumShouldMatch;

		private Bool() {
		}

		public Bool must(Query query) {
			must.add(requireQuery(query));
			return this;
		}

		public Bool should(Query query) {
			should.add(requireQuery(query));
			return this;
		}

		public Bool filter(Query query) {
			filter.add(requireQuery(query));
			return this;
		}

		public Bool mustNot(Query query) {
			mustNot.add(requireQuery(query));
			return this;
		}

		public Bool minimumShouldMatch(String minimumShouldMatch) {
			this.minimumShouldMatch = requireText(minimumShouldMatch, "minimumShouldMatch");
			return this;
		}

		public Query build() {
			if (must.isEmpty() && should.isEmpty() && filter.isEmpty() && mustNot.isEmpty()) {
				return matchAll();
			}

			return QueryBuilders.bool(bool -> {
				if (!must.isEmpty()) {
					bool.must(must);
				}
				if (!should.isEmpty()) {
					bool.should(should);
				}
				if (!filter.isEmpty()) {
					bool.filter(filter);
				}
				if (!mustNot.isEmpty()) {
					bool.mustNot(mustNot);
				}
				if (minimumShouldMatch != null) {
					bool.minimumShouldMatch(minimumShouldMatch);
				}
				return bool;
			});
		}
	}

	private static Query requireQuery(Query query) {
		return Objects.requireNonNull(query, "query must not be null");
	}

	private static String requireText(String value, String name) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(name + " must not be blank");
		}
		return value;
	}

	private static void requireNotEmpty(List<?> values, String name) {
		if (values == null || values.isEmpty()) {
			throw new IllegalArgumentException(name + " must not be empty");
		}
	}

	private static void requireAtLeastOneBound(Object lowerBound, Object upperBound) {
		if (lowerBound == null && upperBound == null) {
			throw new IllegalArgumentException("at least one range bound must be provided");
		}
	}

	private static FieldValue fieldValue(Object value) {
		Objects.requireNonNull(value, "value must not be null");

		if (value instanceof FieldValue fieldValue) {
			return fieldValue;
		}
		if (value instanceof String stringValue) {
			return FieldValue.of(stringValue);
		}
		if (value instanceof Integer integerValue) {
			return FieldValue.of(integerValue.longValue());
		}
		if (value instanceof Long longValue) {
			return FieldValue.of(longValue);
		}
		if (value instanceof Float floatValue) {
			return FieldValue.of(floatValue.doubleValue());
		}
		if (value instanceof Double doubleValue) {
			return FieldValue.of(doubleValue);
		}
		if (value instanceof Boolean booleanValue) {
			return FieldValue.of(booleanValue);
		}

		return FieldValue.of(value);
	}
}
