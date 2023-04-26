import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AccountsComponent } from '../../../src/app/pages/accounts/accounts.component';
import { AccountService } from '../../../src/app/services/account.service';
import { HttpClientModule } from '@angular/common/http';
import { CurrencyPipe } from '@angular/common';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


describe('AccountsComponent', () => {
  let component: AccountsComponent;
  let fixture: ComponentFixture<AccountsComponent>;
  let compiled; 

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AccountsComponent, TitleSectionComponent], 
      imports: [ HttpClientModule, ReactiveFormsModule, FormsModule ],
      providers: [ AccountService, CurrencyPipe ]
    })
    .compileComponents();
  }));

  beforeEach(async() => {
    fixture = TestBed.createComponent(AccountsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    compiled = fixture.nativeElement; 
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  
  test(('show table'), () => {
    const headers = compiled.querySelectorAll('th')
    expect(headers.length).toBe(6)
    expect(headers[0].innerHTML).toBe("Cliente")
    expect(headers[1].innerHTML).toBe("Numero")
    expect(headers[2].innerHTML).toBe("Saldo inicial")
    expect(headers[3].innerHTML).toBe("Tipo cuenta")
    expect(headers[4].innerHTML).toBe("Estado")

  }) 
});
