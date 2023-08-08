package org.ssglobal.training.codes.service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.ScheduleDTO;
import org.ssglobal.training.codes.dto.StudentDTO;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Student;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PDFService {
	private final DSLContext dslContext;
	private final JavaMailSender mailSender;
	private final StudentService studentService;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	@Value("${spring.mail.username}")
	private String sender;

	public void generatePDF(Integer studentId) {
		Student student = dslContext.selectFrom(STUDENT).where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		if (student != null) {
			Program program = dslContext.selectFrom(PROGRAM).where(PROGRAM.PROGRAM_ID.eq(student.getProgramId()))
					.fetchOneInto(Program.class);
			StudentDTO studentDTO = studentService.getStudentById(student.getStudentId());
			List<ScheduleDTO> scheds = new ArrayList<>(studentDTO.getSchedules());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
			try {
				Document document = new Document(PageSize.A6.rotate());
				File tempFile = File.createTempFile("document", ".pdf");
				PdfWriter.getInstance(document, new FileOutputStream(tempFile));

				document.open();
				Font font = FontFactory.getFont(FontFactory.HELVETICA);
				font.setSize(6);
				
				Font italic = FontFactory.getFont(FontFactory.HELVETICA, Font.ITALIC);
				italic.setSize(6);

				PdfPTable table = new PdfPTable(13);
				table.setWidthPercentage(100);

				PdfPCell cell = new PdfPCell(new Paragraph("Student Information", font));
				cell.setColspan(2);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph("SY & Term: ", font));
				cell.setColspan(2);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(student.getAcademicYear() + " / " + student.getSem(), font));
				cell.setColspan(2);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph("Program: ", font));
				cell.setColspan(2);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(program.getProgramCode(), font));
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph("Year Level: ", font));
				cell.setColspan(2);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Paragraph(student.getYearLevel(), font));
				cell.setColspan(2);
				cell.setPadding(5);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				PdfPTable table1 = new PdfPTable(4);
				table1.setWidthPercentage(100);

				PdfPCell cell1 = new PdfPCell(new Paragraph(student.getStudentNo(), font));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(student.getLastname(), font));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(student.getFirstname(), font));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph(student.getMiddlename(), font));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph("Student Number", font));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph("Last Name", italic));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph("First Name", italic));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);

				cell1 = new PdfPCell(new Paragraph("Middle Name", italic));
				cell1.setPadding(5);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table1.addCell(cell1);
				
				PdfPTable table2 = new PdfPTable(1);
				table2.setWidthPercentage(100);
				
				PdfPCell cell2 = new PdfPCell(new Paragraph("Schedules", font));
				cell2.setPadding(5);
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table2.addCell(cell2);
				
				PdfPTable table3 = new PdfPTable(11);
				table3.setWidthPercentage(100);
				
				PdfPCell cell3 = new PdfPCell(new Paragraph("Subject Title", font));
				cell3.setColspan(2);
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				cell3 = new PdfPCell(new Paragraph("Units", font));
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				cell3 = new PdfPCell(new Paragraph("Days", font));
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				cell3 = new PdfPCell(new Paragraph("Time", font));
				cell3.setColspan(2);
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				cell3 = new PdfPCell(new Paragraph("Room", font));
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				cell3 = new PdfPCell(new Paragraph("Section", font));
				cell3.setColspan(2);
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				cell3 = new PdfPCell(new Paragraph("Instructor", font));
				cell3.setColspan(2);
				cell3.setPadding(5);
				cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table3.addCell(cell3);
				
				for (int i = 0; i < scheds.size(); i++) {
					cell3 = new PdfPCell(new Paragraph(scheds.get(i).getSubject().getSubjectTitle(), font));
					cell3.setColspan(2);
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
					
					cell3 = new PdfPCell(new Paragraph(scheds.get(i).getSubject().getUnits().toString(), font));
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
					
					cell3 = new PdfPCell(new Paragraph(formatDays(scheds.get(i).getDays()), font));
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
					
					cell3 = new PdfPCell(new Paragraph(scheds.get(i).getStartTime().format(formatter) + " - " + scheds.get(i).getEndTime().format(formatter), font));
					cell3.setColspan(2);
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
					
					cell3 = new PdfPCell(new Paragraph(scheds.get(i).getRoom().getRoomNumber(), font));
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
					
					cell3 = new PdfPCell(new Paragraph(scheds.get(i).getSection().getSection(), font));
					cell3.setColspan(2);
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
					
					cell3 = new PdfPCell(new Paragraph(scheds.get(i).getProfessor().getFullname(), font));
					cell3.setColspan(2);
					cell3.setPadding(5);
					cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table3.addCell(cell3);
				}

				document.add(table);
				document.add(table1);
				document.add(table2);
				document.add(table3);
				document.close();

				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true);
				helper.setFrom(sender);
				helper.setTo(student.getEmail());
				helper.setText("");
				helper.setSubject("Educate University - Your subjects are approved");

				FileSystemResource file = new FileSystemResource(tempFile);
				helper.addAttachment("Document.pdf", file);

				mailSender.send(message);

				tempFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String formatDays(List<String> days) {
		String day = "";
		for (int i = 0; i < days.size(); i++) {
			if (days.get(i).equalsIgnoreCase("Monday")) {
				day += "M";
			} else if (days.get(i).equalsIgnoreCase("Tuesday")) {
				day += "T";
			} else if (days.get(i).equalsIgnoreCase("Wednesday")) {
				day += "W";
			} else if (days.get(i).equalsIgnoreCase("Thursday")) {
				day += "Th";
			} else if  (days.get(i).equalsIgnoreCase("Friday")) {
				day += "F";
			} else if  (days.get(i).equalsIgnoreCase("Saturday")) {
				day += "S";
			}
		}
		return day;
	}
}
