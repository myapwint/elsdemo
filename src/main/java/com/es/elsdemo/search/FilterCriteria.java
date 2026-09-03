package com.es.elsdemo.search;

import lombok.Data;

import java.util.List;

@Data
public class FilterCriteria {
	private String field;
	private String type;
	private Object value;
	private List<Object> values;
	private Object from;
	private Object to;
}
