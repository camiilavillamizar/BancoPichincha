package com.pichincha.test.models.Dtos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pichincha.test.models.Dtos.classes.ReportDto;
import com.pichincha.test.models.Dtos.repository.ReportDtoRepository;

@Service
public class ReportService {
	
	@Autowired
	ReportDtoRepository reportRepo; 
	
	public List<ReportDto> getReporByClientBtwnDates(String startDate, String endDate, int clientId){
		return reportRepo.getReporByClientBtwnDates(startDate, endDate, clientId); 
	}
	
	
}
