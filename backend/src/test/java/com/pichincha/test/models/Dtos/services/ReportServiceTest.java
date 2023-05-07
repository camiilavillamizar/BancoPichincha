package com.pichincha.test.models.Dtos.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.pichincha.test.models.Dtos.classes.ReportDto;
import com.pichincha.test.models.Dtos.repository.ReportDtoRepository;
import com.pichincha.test.models.Entity.Account;
import com.pichincha.test.models.Entity.Client;
import com.pichincha.test.models.Entity.Transaction;
import com.pichincha.test.utils.enums.AccountType;
import com.pichincha.test.utils.enums.Gender;
import com.pichincha.test.utils.enums.TransactionType;

@SpringBootTest
public class ReportServiceTest {

	@InjectMocks
	ReportService reportService; 
	
	@Mock
	ReportDtoRepository reportRepo;
	
	
	
	ReportDto report = new ReportDto(1, "2023-01-15", "Laura Pérez", 12345, AccountType.AHORROS, 
			TransactionType.CREDITO, BigDecimal.ZERO, true, new BigDecimal(100), new BigDecimal(100)); 
	List<ReportDto> reports = Arrays.asList(report); 
	String from;
	String to; 
	
	DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
	
	@BeforeEach()
	void setUp() {
		
		
	
	}
	
	@Test
	void testGetReporByClientBtwnDates_validDates() {
		
		from = "2023-01-01"; 
		to = "2023-02-01";
			
		when(reportRepo.getReporByClientBtwnDates(from, to, 1)).thenReturn(reports);
		try {
			assertEquals("Must be same list", reports, reportService.getReporByClientBtwnDates(from, to, 1));
		} catch (Exception e) {
			fail("Must do not throw an exception"); 
		}
	}
	
	@Test
	void testGetReporByClientBtwnDates_invalidEndDate() {
		
		Calendar todayCalendar = Calendar.getInstance(); 
		Date todayDate = todayCalendar.getTime();
		String today = dateFormat.format(todayDate);
		
		Calendar tomorrowCalendar = Calendar.getInstance(); 
		tomorrowCalendar.add(Calendar.DATE, 1);
		Date tomorrowDate = tomorrowCalendar.getTime();
		String tomorrow = dateFormat.format(tomorrowDate); 
		
		from = "2023-01-01"; 
		to = tomorrow;
			
		Exception exception = assertThrows("Must throw invalid end date exception", Exception.class, () -> reportService.getReporByClientBtwnDates(from, to, 1)); 
		assertTrue("Exception must be invalid end date", exception.getMessage().equals("INVALID END DATE")); 
		
	}
	
	@Test
	void testGetReporByClientBtwnDates_startDateGreaterThanEndDate() {
		
		
		from = "2023-02-01"; 
		to = "2023-01-01";
			
		Exception exception = assertThrows("Must throw invalid end date exception", Exception.class, () -> reportService.getReporByClientBtwnDates(from, to, 1)); 
		assertTrue("Exception must be end date must be greater than start date", exception.getMessage().equals("END DATE MUST BE GREATER THAN START DATE")); 
		
	}
}



























