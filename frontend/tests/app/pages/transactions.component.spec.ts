import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionsComponent } from '../../../src/app/pages/transactions/transactions.component';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TransactionService } from '../../../src/app/services/transaction.service';
import { HttpClientModule } from '@angular/common/http';
import { CurrencyPipe } from '@angular/common';

describe('TransactionsComponent', () => {
  let component: TransactionsComponent;
  let fixture: ComponentFixture<TransactionsComponent>;
  let compiled; 

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionsComponent , TitleSectionComponent], 
      imports: [HttpClientModule, FormsModule, ReactiveFormsModule],
      providers: [TransactionService, CurrencyPipe]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsComponent);
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
    expect(headers[0].innerHTML).toBe("Cliente")
    expect(headers[1].innerHTML).toBe("Fecha")
    expect(headers[2].innerHTML).toBe("No. Cuenta")
    expect(headers[3].innerHTML).toBe("Tipo cuenta")
    expect(headers[4].innerHTML).toBe("Tipo Transacción")
    expect(headers[5].innerHTML).toBe("Valor")
    expect(headers[6].innerHTML).toBe("Saldo disponible")

  }) 
});
