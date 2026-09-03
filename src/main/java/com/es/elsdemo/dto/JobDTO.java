package com.es.elsdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {
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
