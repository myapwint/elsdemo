package com.es.elsdemo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName = "candidate")
@Mapping(mappingPath = "elasticsearch/candidate-mapping.json")
@Setting(settingPath = "elasticsearch/index-settings.json")
public class Candidate {
	@Id
	private String id;
	private String email;
	private String name;
	private Address address;




}
