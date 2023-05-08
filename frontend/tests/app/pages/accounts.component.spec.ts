import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AccountsComponent } from '../../../src/app/pages/accounts/accounts.component';
import { AccountService } from '../../../src/app/services/account.service';
import { HttpClientModule } from '@angular/common/http';
import { CurrencyPipe } from '@angular/common';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';
import { FormControl, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClientService } from '../../../src/app/services/client.service';
import { of, throwError } from 'rxjs';
import replaceAllInserter from 'string.prototype.replaceall';


describe('AccountsComponent', () => {
  let component: AccountsComponent;
  let fixture: ComponentFixture<AccountsComponent>;
  let accountService: Partial<AccountService>;
  let clientService: Partial<ClientService>; 
  let compiled; 
  replaceAllInserter.shim();

  beforeEach(async(() => {
    clientService = {
      getAll: jest.fn().mockReturnValue(of([]))
    };
    accountService = {
      getAll: jest.fn().mockReturnValue(of([])), 
      post: jest.fn().mockReturnValue(of({})), 
      put: jest.fn().mockReturnValue(of({})), 
      delete: jest.fn().mockReturnValue(of({}))

    }
    TestBed.configureTestingModule({
      declarations: [ AccountsComponent, TitleSectionComponent], 
      imports: [ HttpClientModule, ReactiveFormsModule, FormsModule ],
      providers: [  
        { provide: AccountService, useValue: accountService },
        { provide: ClientService, useValue: clientService }, CurrencyPipe ]
    })
    .compileComponents();
  }));

  beforeEach(async() => {
    fixture = TestBed.createComponent(AccountsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    compiled = fixture.nativeElement; 
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  
  test('should initialize form and fetch clients and accounts', () => {
    const clients = [{ id: 1, name: 'John', gender: 'MALE', age: 30, dni: '123456789', address: '123 Street', phone: '1234567890', password: 'password', state: true }];
    const accounts = [{ id: 1, number: 12345, balance: 1000, type: 'AHORROS', state: true, clientName: 'John', clientId: 1 }];
    
    jest.spyOn(clientService, 'getAll').mockReturnValue(of(clients));
    jest.spyOn(accountService, 'getAll').mockReturnValue(of(accounts));

    component.ngOnInit();

    expect(component.form).toBeDefined();
    expect(component.clients).toEqual(clients);
    expect(component.filteredClients).toEqual(clients);
    expect(component.accounts).toEqual(accounts);
    expect(component.filteredAccounts).toEqual(accounts);
  });

  test('should filter accounts based on the filter value', () => {
    const accounts = [
      { id: 1, number: 12345, balance: 1000, type: 'AHORROS', state: true, clientName: 'John', clientId: 1 },
      { id: 2, number: 54321, balance: 2000, type: 'CORRIENTE', state: true, clientName: 'Jane', clientId: 2 }
    ];
    component.accounts = accounts;

    component.filter = 'John';
    component.onFilter();
    expect(component.filteredAccounts).toEqual([accounts[0]]);

    component.filter = '2000';
    component.onFilter();
    expect(component.filteredAccounts).toEqual([accounts[1]]);

    component.filter = 'FALSE';
    component.onFilter();
    expect(component.filteredAccounts).toEqual([]);

    component.filter = '';
    component.onFilter();
    expect(component.filteredAccounts).toEqual(accounts);
  });

  test('should set the form type, reset saveDone, and display the save dialog', () => {
    component.onOpenSaveDialog();
    expect(component.formType).toEqual('post');
    expect(component.saveDone).toBe(false);
    expect(component.saveDialog.style.display).toEqual('block');
    expect(component.form.get('client').value).toBeNull();
    expect(component.form.get('number').value).toBeNull();
    expect(component.form.get('balance').value).toBeNull();
    expect(component.form.get('type').value).toBeNull();
    expect(component.form.get('state').value).toBeNull();
  });

  test('should hide the save dialog and reset formType and saveDone', () => {
    component.onCloseSaveDialog();
    expect(component.saveDialog.style.display).toEqual('none');
    expect(component.formType).toBeNull();
    expect(component.saveDone).toBe(false);
  });

  describe('onSaveAccount', () => {
    beforeEach(() => {
      component.form.controls['client'].setValue(1);
      component.form.controls['number'].setValue(12345);
      component.form.controls['balance'].setValue(1000);
      component.form.controls['type'].setValue('AHORROS');
      component.form.controls['state'].setValue(true);
    });
  
    test('should create a new account and add it to the accounts list', () => {
      const newAccount = {
        id: 2,
        number: 12345,
        balance: 1000,
        type: 'AHORROS',
        state: true,
        clientName: 'John',
        clientId: 1
      };
      jest.spyOn(clientService, 'getAll').mockReturnValue(of([{ id: 1, name: 'John', gender: 'MALE', age: 30, dni: '123456789', address: '123 Street', phone: '1234567890', password: 'password', state: true }]));
      jest.spyOn(accountService, 'post').mockReturnValue(of(newAccount));

      component.form.controls.balance = new FormControl('$1.000', Validators.required);
      component.onSaveAccount();
  
      expect(component.loading).toBe(false);
      expect(component.saveDone).toBe(true);
      expect(component.filter).toBe('');
      expect(component.accounts).toContain(newAccount);
      expect(component.message).toEqual('¡Cuenta creada exitosamente!');
    });
  
    test('should handle error if account creation fails', () => {
      const errorResponse = { error: 'Error creating account' };
      jest.spyOn(accountService, 'post').mockReturnValue(throwError(errorResponse));
  
      component.form.controls.balance = new FormControl('$1.000', Validators.required);
      component.onSaveAccount();
  
      expect(component.saveDone).toBe(true);
      expect(component.loading).toBe(false);
      expect(component.message).toEqual('No se ha podido crear la cuenta. Error creating account');
    });
  });

  test('should set the form type, populate the form with selected account details, and display the save dialog', () => {
    const account = { id: 1, number: 12345, balance: 1000, type: 'AHORROS', state: true, clientName: 'John', clientId: 1 };

    component.onOpenEditDialog(account);
    expect(component.formType).toEqual('put');
    expect(component.form.get('client').value).toEqual('John');
    expect(component.form.get('number').value).toEqual(12345);
    expect(component.form.get('type').value).toEqual('AHORROS');
    expect(component.form.get('state').value).toEqual(true);
    expect(component.selectedAccount).toEqual(account);
    expect(component.saveDialog.style.display).toEqual('block');
  });

  describe('onUpdateAccount', () => {
    beforeEach(() => {
      component.form.controls['client'].setValue('John');
      component.form.controls['number'].setValue(12345);
      component.form.controls['balance'].setValue(1000);
      component.form.controls['type'].setValue('AHORROS');
      component.form.controls['state'].setValue(true);
      component.selectedAccount = { id: 1, number: 12345, balance: 1000, type: 'AHORROS', state: true, clientName: 'John', clientId: 1 };
    });
  
    test('should update the selected account and display success message', () => {
      const updatedAccount = { id: 1, number: 12345, balance: 2000, type: 'CORRIENTE', state: false, clientName: 'John', clientId: 1 };
      jest.spyOn(clientService, 'getAll').mockReturnValue(of([{ id: 1, name: 'John', gender: 'MALE', age: 30, dni: '123456789', address: '123 Street', phone: '1234567890', password: 'password', state: true }]));
      jest.spyOn(accountService, 'put').mockReturnValue(of(updatedAccount));
  
      component.accounts = [{ id: 1, number: 11111, balance: 2000, type: 'CORRIENTE', state: false, clientName: 'John', clientId: 1 }]
      component.form.controls.balance = new FormControl('$1.000', Validators.required);
      component.onUpdateAccount();
  
      expect(component.loading).toBe(false);
      expect(component.saveDone).toBe(true);
      expect(component.filter).toBe('');
      expect(component.accounts).toContain(updatedAccount);
      expect(component.message).toEqual('¡Cuenta actualizada exitosamente!');
    });
  
    test('should handle error if account update fails', () => {
      const errorResponse = { error: 'Error updating account' };
      jest.spyOn(accountService, 'put').mockReturnValue(throwError(errorResponse));
  
      component.form.controls.balance = new FormControl('$1.000', Validators.required);

      component.onUpdateAccount();
  
      expect(component.saveDone).toBe(true);
      expect(component.loading).toBe(false);
      expect(component.message).toEqual('No se ha podido actualizar la cuenta Error updating account');
    });
  });

  test('should hide the delete dialog and reset deleteDone', () => {
    component.onCloseDeleteDialog();
    expect(component.deleteDialog.style.display).toEqual('none');
    expect(component.deleteDone).toBe(false);
  });

  describe('onDeleteAccount', () => {
    const account = { id: 1, number: 12345, balance: 1000, type: 'AHORROS', state: true, clientName: 'John', clientId: 1 };
  
    test('should delete the selected account and display success message', () => {
      jest.spyOn(accountService, 'delete').mockReturnValue(of({}));
  
      component.selectedAccount = account;
      component.onDeleteAccount();
  
      expect(component.loading).toBe(false);
      expect(component.deleteDone).toBe(true);
      expect(component.message).toEqual('Cuenta eliminada exitosamente');
      expect(component.accounts).not.toContain(account);
    });
  
    it('should handle error if account deletion fails', () => {
      const errorResponse = { error: 'Error deleting account' };
      jest.spyOn(accountService, 'delete').mockReturnValue(throwError(errorResponse));
  
      component.selectedAccount = account;
      component.onDeleteAccount();
  
      expect(component.deleteDone).toBe(true);
      expect(component.loading).toBe(false);
      expect(component.message).toEqual('No se ha podido eliminar la cuenta Error deleting account');
    });
  });

  test('should transform the balance value to currency format', () => {
    component.form.controls.balance = new FormControl('1000', Validators.required);
    component.transformAmount();
    expect(component.form.get('balance').value).toEqual('$1,000.00');
  });

});

