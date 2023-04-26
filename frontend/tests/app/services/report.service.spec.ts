import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ReportService } from '../../../src/app/services/report.service';

describe('ReportService', () => {
  
  beforeEach(async() => 
  TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ]
    })
  );

  it('should be created', () => {
    const service: ReportService = TestBed.get(ReportService);
    expect(service).toBeTruthy();
  });

  let startDate = '20230415'
  let endDate = '20230430'
  let clientId = 1; 

  test('should return reports', (done) => {
    const service: ReportService = TestBed.get(ReportService);
    service.getReport(startDate, endDate, clientId).subscribe(response => {
      
      expect(response.length).toBeGreaterThan(0); 
      expect(typeof response[0].id).toBe("number"); 

      done()
    })
  })

  test('should download pdf', (done) => {
    const service: ReportService = TestBed.get(ReportService);
    service.downloasReport(startDate, endDate, clientId).subscribe(response => {
      expect('application/pdf').toBe(response.type)
      done()
    })
  })
});
