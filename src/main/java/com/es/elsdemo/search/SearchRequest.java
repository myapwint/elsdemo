package com.es.elsdemo.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

import java.util.List;

public record SearchRequest(Query query, List<SortOptions> sorters) {
}
