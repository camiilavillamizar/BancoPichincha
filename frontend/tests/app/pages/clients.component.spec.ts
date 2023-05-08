import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { of } from 'rxjs';

import { ClientsComponent } from '../../../src/app/pages/clients/clients.component';
import { ClientService } from '../../../src/app/services/client.service';
import { TitleSectionComponent } from '../../../src/app/components/title-section/title-section.component';
import { Client } from '../../../src/app/interfaces/client.interfaces';
import { HttpClientModule } from '@angular/common/http';

describe('ClientsComponent', () => {
  let component: ClientsComponent;
  let fixture: ComponentFixture<ClientsComponent>;
  let clientService: Partial<ClientService>; 

  beforeEach(async(() => {

    clientService = {
      getAll: jest.fn().mockReturnValue(of([])),
      post: jest.fn().mockReturnValue(of({})),
      put: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({})),
    };

    TestBed.configureTestingModule({
      declarations: [ ClientsComponent, TitleSectionComponent ], 
      imports: [ HttpClientModule, ReactiveFormsModule, FormsModule],
      providers: [{ provide: ClientService, useValue: clientService}]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should load clients on initialization', () => {
    const mockClients: Client[] = [{ id: 1, name: 'Camila Villamizar', password: "2223", gender: 'FEMALE', age: 30, dni: '123456789', address: '123 Street', phone: '1234567890', state: true }];
    clientService.getAll = jest.fn().mockReturnValue(of(mockClients));

    component.ngOnInit();

    expect(clientService.getAll).toHaveBeenCalled();
    expect(component.clients).toEqual(mockClients);
    expect(component.filteredClients).toEqual(mockClients);
  });

  it('should filter clients based on input', () => {
    const mockClients: Client[] = [
      { id: 1, name: 'Camila Villamizar', password: "2223", gender: 'FEMALE', age: 30, dni: '123456789', address: '123 Street', phone: '1234567890', state: true },
      { id: 2, name: 'James Smith', password: "2453", gender: 'MALE', age: 25, dni: '987654321', address: '456 Avenue', phone: '9876543210', state: false }
    ];
    component.clients = mockClients;

    // Filter by name
    component.filter = 'Camila';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[0]]);

    // Filter by gender
    component.filter = 'Female';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[0]]);

    // Filter by age
    component.filter = '25';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[1]]);

    // Filter by DNI
    component.filter = '987654321';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[1]]);

    // Filter by address
    component.filter = '456 Avenue';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[1]]);

    // Filter by phone
    component.filter = '1234567890';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[0]]);

    // Filter by state (true)
    component.filter = 'TRUE';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[0]]);

    // Filter by state (false)
    component.filter = 'FALSE';
    component.onFilter();
    expect(component.filteredClients).toEqual([mockClients[1]]);
  });

  test('should open save dialog', () => {
    component.onOpenSaveDialog();
  });

  test('should open the edit dialog with correct client data', () => {
    const client = {id: 1, name: 'Camila Villamizar', password: "1234", gender: 'FEMALE', age: 30, dni: '12345678', address: '123 Street', phone: '123456789', state: true };

    component.onOpenEditDialog(client); 

    expect(component.formType).toBe('put');
    expect(component.createForm.controls.name.value).toEqual(client.name);
    expect(component.createForm.controls.gender.value).toEqual(client.gender);
    expect(component.createForm.controls.age.value).toEqual(client.age);
    expect(component.createForm.controls.phone.value).toEqual(client.phone);

  });

  it('should create a new client', () => {
    const newClient = { name: 'Camila Villamizar', password: "1234", gender: 'FEMALE', age: 30, dni: '12345678', address: '123 Street', phone: '123456789', state: true };
    component.createForm.setValue(newClient);

    component.onSaveClient();

    expect(clientService.post).toHaveBeenCalledWith(newClient);
    expect(component.createForm.controls.name.value).toEqual(newClient.name);
    expect(component.createForm.controls.gender.value).toEqual(newClient.gender);
    expect(component.createForm.controls.age.value).toEqual(newClient.age);
    expect(component.createForm.controls.phone.value).toEqual(newClient.phone);
  });

  it('should update an existing client', () => {
    let formValue = { name: 'Camila Villamizar', password: "1234", gender: 'FEMALE', age: 30, dni: '12345678', address: '123 Street', phone: '123456789', state: true };
    component.createForm.setValue(formValue);
    component.formType = 'put'; 
    let updatedClient = { id: 1, name: 'Camila Villamizar', password: "1234", gender: 'FEMALE', age: 30, dni: '12345678', address: '123 Street', phone: '123456789', state: true };
    component.selectedUser =  updatedClient; 

    component.onUpdateClient();

    expect(clientService.put).toHaveBeenCalledWith(updatedClient);
    expect(component.createForm.controls.name.value).toEqual(updatedClient.name);
    expect(component.createForm.controls.gender.value).toEqual(updatedClient.gender);
    expect(component.createForm.controls.age.value).toEqual(updatedClient.age);
    expect(component.createForm.controls.phone.value).toEqual(updatedClient.phone);
  });

  it('should delete a client', () => {
    const clientToDelete = { id: 1, name: 'Camila Villamizar', password: "1234",  gender: 'FEMALE', age: 30, dni: '12345678', address: '123 Street', phone: '123456789', state: true };
    component.selectedUser = clientToDelete; 

    component.onDeleteClient();

    expect(clientService.delete).toHaveBeenCalledWith(clientToDelete.id)
  });

});
