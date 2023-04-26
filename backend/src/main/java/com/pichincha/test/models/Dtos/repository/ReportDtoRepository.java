package com.pichincha.test.models.Dtos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pichincha.test.models.Dtos.classes.ReportDto;

@Repository
public interface ReportDtoRepository extends CrudRepository<ReportDto, Long>{
	
	@Query(value = "CALL getReportByClientBtwnDates( :startDate , :endDate , :clientId )", nativeQuery = true)
	public List<ReportDto> getReporByClientBtwnDates(@Param("startDate") String startDate, 
													 @Param("endDate") String endDate,
													 @Param("clientId") int clientId);
}
