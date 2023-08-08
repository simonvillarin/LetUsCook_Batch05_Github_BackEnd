package org.ssglobal.training.codes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.AttendanceProfDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.AttendanceProfessor;
import org.ssglobal.training.codes.tables.pojos.Professor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceProfessorService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.AttendanceProfessor ATTENDANCEPROF = org.ssglobal.training.codes.tables.AttendanceProfessor.ATTENDANCE_PROFESSOR;
	
	public List<AttendanceProfDTO> getAllProfAttendance() {
		List<AttendanceProfessor> attendanceProfs = dslContext.selectFrom(ATTENDANCEPROF)
				.fetchInto(AttendanceProfessor.class);
		List<AttendanceProfDTO> attendanceProfDTOs = new ArrayList<>();
		
		for (int i = 0; i < attendanceProfs.size(); i++) {
			Professor professor = dslContext.selectFrom(ATTENDANCEPROF)
					.where(ATTENDANCEPROF.PROFESSOR_ID.eq(attendanceProfs.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			
			AttendanceProfDTO attendanceProfDTO = AttendanceProfDTO.builder()
					.attendanceId(attendanceProfs.get(i).getAttendanceId())
					.professor(professor)
					.date(attendanceProfDTOs.get(i).getDate())
					.status(attendanceProfDTOs.get(i).getStatus())
					.build();
			attendanceProfDTOs.add(attendanceProfDTO);
		}
		return attendanceProfDTOs;
	}
	
	public List<AttendanceProfDTO> getProfAttendanceById(Integer professorId) {
		List<AttendanceProfessor> attendanceProfs = dslContext.selectFrom(ATTENDANCEPROF)
				.where(ATTENDANCEPROF.PROFESSOR_ID.eq(professorId))
				.fetchInto(AttendanceProfessor.class);
		List<AttendanceProfDTO> attendanceProfDTOs = new ArrayList<>();
		
		for (int i = 0; i < attendanceProfs.size(); i++) {
			Professor professor = dslContext.selectFrom(ATTENDANCEPROF)
					.where(ATTENDANCEPROF.PROFESSOR_ID.eq(attendanceProfs.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			
			AttendanceProfDTO attendanceProfDTO = AttendanceProfDTO.builder()
					.attendanceId(attendanceProfs.get(i).getAttendanceId())
					.professor(professor)
					.date(attendanceProfDTOs.get(i).getDate())
					.status(attendanceProfDTOs.get(i).getStatus())
					.build();
			attendanceProfDTOs.add(attendanceProfDTO);
		}
		return attendanceProfDTOs;
	}
	
	public Response addProfAttendance(AttendanceProfessor attendanceProf) {
		dslContext.insertInto(ATTENDANCEPROF)
		.set(ATTENDANCEPROF.PROFESSOR_ID, attendanceProf.getProfessorId())
		.set(ATTENDANCEPROF.DATE,  LocalDate.now())
		.set(ATTENDANCEPROF.TIME, LocalTime.now())
		.set(ATTENDANCEPROF.STATUS, attendanceProf.getStatus())
		.execute();
		
		return Response.builder()
				.status(201)
				.message("Professor's attendance sucessfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateProfAttendance(Integer attendanceId, AttendanceProfessor attendanceProf) {
		AttendanceProfessor _attendance = dslContext.selectFrom(ATTENDANCEPROF)
				.where(ATTENDANCEPROF.ATTENDANCE_ID.eq(attendanceId))
				.fetchOneInto(AttendanceProfessor.class);
		
		if (_attendance != null) {
			if (attendanceProf.getProfessorId() != null) {
				dslContext.update(ATTENDANCEPROF)
				.set(ATTENDANCEPROF.PROFESSOR_ID, attendanceProf.getProfessorId())
				.where(ATTENDANCEPROF.ATTENDANCE_ID.eq(attendanceId))
				.execute();
			}
			if (attendanceProf.getDate() != null ) {
				dslContext.update(ATTENDANCEPROF)
				.set(ATTENDANCEPROF.DATE, attendanceProf.getDate())
				.where(ATTENDANCEPROF.ATTENDANCE_ID.eq(attendanceId))
				.execute();
			}
			if (attendanceProf.getTime() != null ) {
				dslContext.update(ATTENDANCEPROF)
				.set(ATTENDANCEPROF.TIME, attendanceProf.getTime())
				.where(ATTENDANCEPROF.ATTENDANCE_ID.eq(attendanceId))
				.execute();
			}
			if (attendanceProf.getStatus() != null) {
				dslContext.update(ATTENDANCEPROF)
				.set(ATTENDANCEPROF.STATUS, attendanceProf.getStatus())
				.where(ATTENDANCEPROF.ATTENDANCE_ID.eq(attendanceId))
				.execute();
			}
			return Response.builder()
					.status(201)
					.message("Professor's attendance sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Professor's attendance not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}