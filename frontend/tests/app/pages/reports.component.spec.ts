import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ReportsComponent } from '../../../src/app/pages/reports/reports.component';
import { ReportService } from '../../../src/app/services/report.service';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';



describe('ReportsComponent', () => {
  let component: ReportsComponent;
  let fixture: ComponentFixture<ReportsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportsComponent, TitleSectionComponent ], 
      imports: [HttpClientModule, FormsModule],
      providers: [ReportService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
