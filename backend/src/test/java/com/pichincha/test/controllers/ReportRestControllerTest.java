package com.pichincha.test.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.pichincha.test.models.Dtos.classes.ReportDto;
import com.pichincha.test.models.Dtos.repository.ReportDtoRepository;
import com.pichincha.test.models.Dtos.services.ReportService;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.TransactionType;

@SpringBootTest
public class ReportRestControllerTest {

	private MockMvc mockMvc;
	@InjectMocks
	private ReportRestController controller = new ReportRestController(); 
	
	@InjectMocks
	ReportService reportService; 
	
	@Mock
	ReportDtoRepository reportRepo; 
	
	@Mock
	ReportService reportServiceMocked; 
	
	List<ReportDto> reports = new ArrayList<>(); 
	
	@BeforeEach()
	void setUp() {
		
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		 
		ReportDto reporLineA = new ReportDto(1, "2023/04/23", "Maria Camila Villamizar", 
				123455, AccountType.AHORROS, TransactionType.CREDITO, new BigDecimal(100),
				true, new BigDecimal(100), new BigDecimal(200));
		ReportDto reportLineB = new ReportDto(1, "2023/04/20", "Maria Camila Villamizar", 
				123455, AccountType.AHORROS, TransactionType.CREDITO, BigDecimal.ZERO,
				true, new BigDecimal(100), new BigDecimal(100));
		
		reports.add(reporLineA); 
		reports.add(reportLineB);
		
		when(reportRepo.getReporByClientBtwnDates("20230303", "20230501", 1)).thenReturn(reports); 
	}
	
	@Test
	void getReportTest() {
		
		List<ReportDto> actualReports = reportService.getReporByClientBtwnDates("20230303", "20230501", 1); 
		assertEquals(reports, actualReports);
	}
	
	@Test
	void getReportJSONTest() throws Exception {
		 MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/reports/json?startDate=20230303&endDate=20230501&clientId=1").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
	     assertEquals(200, result.getResponse().getStatus());
	     assertEquals(2, result.getResponse().getContentAsByteArray().length);
	     assertEquals("application/octet-stream", result.getResponse().getContentType());
	}
	
	@Test
	void getReportPDFTest() throws Exception {
		 MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/reports/pdf?startDate=20230303&endDate=20230501&clientId=1").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
	     assertEquals(200, result.getResponse().getStatus());
	     assertEquals("application/pdf", result.getResponse().getContentType());
	}
}
