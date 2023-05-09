import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TransactionsComponent } from '../../../src/app/pages/transactions/transactions.component';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { TransactionService } from '../../../src/app/services/transaction.service';
import { HttpClientModule } from '@angular/common/http';
import { CurrencyPipe } from '@angular/common';
import { AccountService } from '../../../src/app/services/account.service';
import { of, throwError } from 'rxjs';
import replaceAllInserter from 'string.prototype.replaceall';

describe('TransactionsComponent', () => {
  let component: TransactionsComponent;
  let fixture: ComponentFixture<TransactionsComponent>;
  let accountService: Partial<AccountService>;
  let transactionService: Partial<TransactionService>;
  let currencyPipe: CurrencyPipe;
  replaceAllInserter.shim();

  let account = {
    id: 1, 
    number: 12345, 
    balance: 100, 
    type: "AHORROS", 
    state: true, 
    clientName: "Camila", 
    clientId: 1, 
    client: {
       id: 1, 
       name: 'Camila', 
       gender: 'FEMALE', 
       age: 30, 
       dni: '123456789', 
       address: '123 Street', 
       phone: '1234567890', 
       password: 'password', 
       state: true 
    }
  }

  let transaction = 
  { id: 1, 
    date: new Date(), 
    type: 'CREDITO', 
    amount: 100, 
    balance: 1000, 
    accountId: 1, 
    account: account 
  }

  beforeEach(async(() => {
    accountService = {
      getAll: jest.fn().mockReturnValue(of([])),
      post: jest.fn().mockReturnValue(of({})),
      put: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({}))
    };

    transactionService = {
      getAll: jest.fn().mockReturnValue(of([])),
      post: jest.fn().mockReturnValue(of({})),
      put: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({}))
    };
    TestBed.configureTestingModule({
      declarations: [TransactionsComponent, TitleSectionComponent],
      imports: [ReactiveFormsModule, FormsModule, HttpClientModule],
      providers: [
        { provide: AccountService, useValue: accountService },
        { provide: TransactionService, useValue: transactionService },
        CurrencyPipe
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsComponent);
    component = fixture.componentInstance;
    currencyPipe = TestBed.inject(CurrencyPipe);
    fixture.detectChanges();

    const account = {
      id: 1, 
      number: 12345, 
      balance: 100, 
      type: "AHORROS", 
      state: true, 
      clientName: "Camila", 
      clientId: 1
    }
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should initialize form and fetch accounts and transactions', () => {
    const accounts = [{ id: 1, number: 12345, balance: 1000, type: 'AHORROS', state: true, clientName: 'John', clientId: 1 }];
    const transactions = [{ id: 1, date: new Date(), type: 'CREDITO', amount: 100, balance: 1000, accountId: 1, account: accounts[0] }];

    jest.spyOn(accountService, 'getAll').mockReturnValue(of(accounts));
    jest.spyOn(transactionService, 'getAll').mockReturnValue(of(transactions));

    component.ngOnInit();

    expect(component.form).toBeDefined();
    expect(component.accounts).toEqual(accounts);
    expect(component.transactions).toEqual(transactions);
    expect(component.filteredTransactions).toEqual(transactions);
  });

  test('should filter transactions based on the filter value', () => {
    const accountA = {
      id: 1, 
      number: 12345, 
      balance: 100, 
      type: "AHORROS", 
      state: true, 
      clientName: "John", 
      clientId: 1
    }

    const transactions = [
      { id: 1, date: new Date(), type: 'CREDITO', amount: 100, balance: 1000, accountId: 1, account: accountA },
      { id: 2, date: new Date(), type: 'DEBITO', amount: -50, balance: 950, accountId: 2, account: accountA }
    ];
    component.transactions = transactions;

    component.filter = 'John';
    component.onFilter();
    expect(component.filteredTransactions[0].id).toEqual(transactions[0].id);

    component.filter = 'DEBITO';
    component.onFilter();
    expect(component.filteredTransactions).toEqual([transactions[1]]);

    component.filter = '2000';
    component.onFilter();
    expect(component.filteredTransactions).toEqual([]);

    component.filter = '';
    component.onFilter();
    expect(component.filteredTransactions).toEqual(transactions)
  })

  describe('SaveTransaction', () => {

    test('should set loading to false and saveDone to true on successful transaction creation', () => {
      
      component.transactions = []
      jest.spyOn(transactionService, 'post').mockReturnValue(of(transaction));
      component.form.controls.amount = new FormControl('$1,000', Validators.required);

      component.onSaveTransaction();
  
      // expect(transactionServiceSpy).toHaveBeenCalledWith({ account: { id: 1 }, amount: 100 });
      expect(component.loading).toBe(false);
      expect(component.saveDone).toBe(true);
      expect(component.transactions).toContain(transaction);
      expect(component.message).toEqual('¡Transacción creada exitosamente!');
    });
  
    test('should set loading to false and saveDone to true on failed transaction creation', () => {

      const errorResponse = { error: 'Failed to create transaction.' };
      jest.spyOn(transactionService, 'post').mockReturnValue(throwError(errorResponse)); 
      component.form.controls.amount = new FormControl('$1,000', Validators.required);
      component.onSaveTransaction();
  
      expect(component.message).toBe('No se ha podido crear la transacción. Failed to create transaction.')
      expect(component.loading).toBe(false);
      expect(component.saveDone).toBe(true);
    });
  })

  describe('UpdateTransaction', () => {
    
    beforeEach(() => {
      component.form.controls['account'].setValue(1); 
      component.form.controls['date'].setValue(new Date()); 
      component.form.controls['amount'].setValue('100'); 
      component.form.controls['type'].setValue("CREDITO"); 

    })
    test('should set loading to false and saveDone to true on successful transaction update', () => {
      let updatedTransaction = 
      { id: 1, 
        date: new Date(), 
        type: 'CREDITO', 
        amount: 100, 
        balance: 2000, 
        accountId: 1, 
        account: account 
      }
     
      component.transactions = [transaction]
      component.selectedTransaction = transaction; 
      jest.spyOn(transactionService, 'put').mockReturnValue(of(updatedTransaction));
      component.form.controls.amount = new FormControl('$2,000', Validators.required);

      component.onUpdateTransaction();
  
      expect(component.filter).toBe('');
      expect(component.loading).toBe(false);
      expect(component.saveDone).toBe(true);
      expect(component.transactions[0]).toBe(updatedTransaction)
      expect(component.message).toEqual('¡Transacción actualizada correctamente!');
    });

    test('should set loading to false and saveDone to true on failed transaction update', () => {

      component.selectedTransaction = transaction; 
      const errorResponse = { error: 'Failed to update transaction.' };
      jest.spyOn(transactionService, 'put').mockReturnValue(throwError(errorResponse)); 
      component.form.controls.amount = new FormControl('$1,000', Validators.required);

      component.onUpdateTransaction();
  
      expect(component.loading).toBe(false);
      expect(component.saveDone).toBe(true);
      expect(component.message).toBe('No se ha podido actualizar la transacción Failed to update transaction.')
     
    });
  })



  describe('DeleteTransaction', () => {
    test('should set loading to false, deleteDone and remove transaction to true on successful transaction deletion', () => {
      
      component.selectedTransaction = transaction; 
      component.onDeleteTransaction();
  
      expect(component.loading).toBe(false);
      expect(component.deleteDone).toBe(true);
      expect(component.transactions.length).toBe(0);
      expect(component.message).toBe('Transacción eliminada exitosamente');
    });

    test('should set the error message on failed transaction deletion', () => {
      const errorMessage = 'Failed to delete transaction.';
      jest.spyOn(transactionService, 'delete').mockReturnValue(throwError({ error: errorMessage }));
  
      component.selectedTransaction = transaction;
      component.onDeleteTransaction();
  
      expect(component.message).toBe('No se ha podido eliminar la transacción ' + errorMessage);
    });
  })

  


});
