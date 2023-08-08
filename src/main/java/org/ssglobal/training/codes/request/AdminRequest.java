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
public class AdminRequest {
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
	private String username;
	private String password;
	private Boolean activeDeactive;
}
