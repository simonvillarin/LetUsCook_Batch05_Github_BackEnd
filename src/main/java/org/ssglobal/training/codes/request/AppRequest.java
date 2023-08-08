package org.ssglobal.training.codes.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppRequest {
	private String programCode;
	private Integer studentId;
	private String yearLevel;
	private String sem;
	private String academicYear;
	private String firstname;
	private String middlename;
	private String lastname;
	private String suffix;
	private String gender;
	private String civilStatus;
	private LocalDate birthdate;
	private String birthplace;
	private String citizenship;
	private String religion;
	private String unit;
	private String street;
	private String subdivision;
	private String barangay;
	private String city;
	private String province;
	private Integer zipcode;
	private String telephone;
	private String mobile;
	private String email;
	private String lastSchoolAttended;
	private String programTaken;
	private String lastSem;
	private String lastYearLevel;
	private String lastSchoolYear;
	private LocalDate dateOfGraduation;
	private String parentFirstname;
	private String parentMiddlename;
	private String parentLastname;
	private String parentSuffix;
	private String parentAddress;
	private String parentContact;
	private String parentEmail;
	private String parentRelationship;
	private String status;
}
