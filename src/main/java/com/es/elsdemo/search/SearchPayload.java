package com.es.elsdemo.search;

import com.es.elsdemo.standard.StandardRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchPayload<T> extends StandardRequestDTO<T> {
	private List<FilterCriteria> filters = new ArrayList<>();
	private List<SorterCriteria> sorters = new ArrayList<>();
}
