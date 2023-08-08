package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.SchoolCalendar;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchoolCalendarService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.SchoolCalendar CALENDAR = org.ssglobal.training.codes.tables.SchoolCalendar.SCHOOL_CALENDAR;
	
	public List<SchoolCalendar> getSchoolCalendar() {
		return dslContext.selectFrom(CALENDAR)
				.fetchInto(SchoolCalendar.class);
	}
	
	public Response addSchoolCalendar(SchoolCalendar calendar) {
		List<SchoolCalendar> calendars = dslContext.selectFrom(CALENDAR)
				.fetchInto(SchoolCalendar.class);
				
		if (calendars.size() > 0) {
			dslContext.update(CALENDAR)
			.set(CALENDAR.START_ENROLLEMENT, calendar.getStartEnrollement())
			.set(CALENDAR.END_ENROLLMENT, calendar.getEndEnrollment())
			.set(CALENDAR.START_CLASS, calendar.getStartClass())
			.set(CALENDAR.END_CLASS, calendar.getEndClass())
			.where(CALENDAR.CALENDAR_ID.eq(calendars.get(0).getCalendarId()))
			.execute();
		} else {
			dslContext.insertInto(CALENDAR)
			.set(CALENDAR.START_ENROLLEMENT, calendar.getStartEnrollement())
			.set(CALENDAR.END_ENROLLMENT, calendar.getEndEnrollment())
			.set(CALENDAR.START_CLASS, calendar.getStartClass())
			.set(CALENDAR.END_CLASS, calendar.getEndClass())
			.execute();
		}
		
		return Response.builder()
				.status(201)
				.message("School calendar successfully added")
				.timestamp(LocalDateTime.now())
				.build();
	}
}
