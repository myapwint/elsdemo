package com.es.elsdemo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName = "job")
@Mapping(mappingPath = "elasticsearch/job-mapping.json")
@Setting(settingPath = "elasticsearch/index-settings.json")
public class Job {
	@Id
	private String id;
	private String aboutCompany;
	private String companyDescription;
	private String aboutJob;
	private String jobDescription;
	private String howWeWork;
	private String whatWillYouBeWorkingOn;
	private String qualifications;
	private String niceToHave;
	private String additionalInformation;
	private String whatDoWeOffer;
}
