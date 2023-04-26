import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsComponent } from '../../../src/app/pages/clients/clients.component';
import { ClientService } from '../../../src/app/services/client.service';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

describe('ClientsComponent', () => {
  let component: ClientsComponent;
  let fixture: ComponentFixture<ClientsComponent>;
  let compiled; 

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientsComponent, TitleSectionComponent ], 
      imports: [HttpClientModule, FormsModule, ReactiveFormsModule],
      providers: [ClientService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    compiled = fixture.nativeElement; 
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test(('show table'), () => {
    const headers = compiled.querySelectorAll('th')
    expect(headers.length).toBe(8)
    expect(headers[0].innerHTML).toBe("Nombre")
    expect(headers[1].innerHTML).toBe("Genero")
    expect(headers[2].innerHTML).toBe("Edad")
    expect(headers[3].innerHTML).toBe("DNI")
    expect(headers[4].innerHTML).toBe("Dirección")
    expect(headers[5].innerHTML).toBe("Teléfono")
    expect(headers[6].innerHTML).toBe("Estado")

  }) 
});
