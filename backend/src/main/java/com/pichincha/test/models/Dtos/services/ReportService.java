package com.pichincha.test.models.Dtos.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pichincha.test.models.Dtos.classes.ReportDto;
import com.pichincha.test.models.Dtos.repository.ReportDtoRepository;

@Service
public class ReportService {
	
	@Autowired
	ReportDtoRepository reportRepo; 
	
	DateFormat dateFormat = new SimpleDateFormat("yyyymmdd");  
	
	public void checkDates(String startDate, String endDate) throws Exception{
		
		Date date = Calendar.getInstance().getTime();  
		String today = dateFormat.format(date);  
		
		endDate = endDate.replace("-", "");
		startDate = startDate.replace("-", "");
		
		if(Integer.valueOf(endDate) > Integer.valueOf(today)) throw new Exception ("INVALID END DATE"); 
		if(Integer.valueOf(startDate) > Integer.valueOf(today)) throw new Exception ("INVALID START DATE"); 
		if(Integer.valueOf(endDate) < Integer.valueOf(startDate)) throw new Exception ("END DATE MUST BE GREATER THAN START DATE"); 
	}
	
	public List<ReportDto> getReporByClientBtwnDates(String startDate, String endDate, int clientId) throws Exception{
		checkDates(startDate, endDate);
		return reportRepo.getReporByClientBtwnDates(startDate, endDate, clientId); 
	}
	
	
}
