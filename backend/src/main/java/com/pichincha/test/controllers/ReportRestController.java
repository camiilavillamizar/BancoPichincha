package com.pichincha.test.controllers;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.test.models.Dtos.classes.ReportDto;
import com.pichincha.test.models.Dtos.services.ReportService;
import com.pichincha.test.utils.exports.ExportPdf;

@RestController()
@RequestMapping("api/reports")
public class ReportRestController {

	@Autowired
	ReportService reportService; 
	
	@GetMapping("")
	public ResponseEntity getReport(@RequestParam String startDate, @RequestParam String endDate, @RequestParam int clientId) {
		try {
			return ResponseEntity.status(200).body(reportService.getReporByClientBtwnDates(startDate, endDate, clientId)); 
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
	
	@GetMapping("/json")
	public ResponseEntity getReportJSON(@RequestParam String startDate, @RequestParam String endDate, @RequestParam int clientId) {
		try {
			List<ReportDto> reports = reportService.getReporByClientBtwnDates(startDate, endDate, clientId);
			ObjectMapper mapper = new ObjectMapper(); 
			byte[] buf = mapper.writeValueAsBytes(reports);
			return ResponseEntity
			        .ok()
			        .header("Content-Disposition", "attachment; filename=\"report.json\"")
			        .contentLength(buf.length)
			        .contentType(
			                MediaType.parseMediaType("application/octet-stream"))
			        .body(new InputStreamResource(new ByteArrayInputStream(buf))); 
			
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
	@GetMapping("/pdf")
	public ResponseEntity getReportPDF(@RequestParam String startDate, @RequestParam String endDate, @RequestParam int clientId) {
		try {
			List<ReportDto> reports = reportService.getReporByClientBtwnDates(startDate, endDate, clientId);
			
			ByteArrayInputStream bis = ExportPdf.reportPdf(reports); 
			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-Disposition", "attachment;filename=report.pdf");

			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
			
			
		} catch(Exception e) {
			return ResponseEntity.status(400).body("Bad Request " + e.getMessage()); 
		}
	}
}
