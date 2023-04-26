import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { ClientService } from '../../../src/app/services/client.service';
import { Client } from 'src/app/interfaces/client.interfaces';


describe('ClientService', () => {
  
   let client: Client = {
    id: 0,
    name: 'JOSE LEMA',
    gender: 'MALE',
    age: 29,
    dni: '8851774',
    address: 'OTAVALO SN Y PRINCIPAL',
    phone: '098254785',
    password: '1234',
    state: true
  }

  beforeEach(async() => 
  TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ]
    })
  );

  it('should be created', () => {
    const service: ClientService = TestBed.get(ClientService);
    expect(service).toBeTruthy();
  });

  test('should return clients', (done) => {
    const service = TestBed.get(ClientService)

    service.getAll().subscribe(clients => {

      expect(clients.length).toBeGreaterThan(0); 
      expect(typeof clients[0].id).toBe("number");   
      expect(typeof clients[0].age).toBe("number");   
      expect(typeof clients[0].dni).toBe("string");   
      expect(typeof clients[0].phone).toBe("string");   
      expect(typeof clients[0].name).toBe("string"); 
      expect(typeof clients[0].gender).toBe("string");    
      done()
    })
  })

  test('should return saved account', (done) => {

    
    const service = TestBed.get(ClientService)
    service.post(client).subscribe( clientResp => {

      client = clientResp;  
      expect(typeof clientResp.id).toBe("number");   
      expect(clientResp.age).toBe(29);   
      expect(clientResp.dni).toBe("8851774");   
      expect(clientResp.gender).toBe("MALE");   
      expect(clientResp.name).toBe("JOSE LEMA");    
      expect(clientResp.phone).toBe("098254785"); 
      done()
    })
  })

  test('should return updated account', (done) => {

    
    const service = TestBed.get(ClientService)
    service.post(client).subscribe( clientResp => {

      client = clientResp;  
      expect(typeof clientResp.id).toBe("number");   
      expect(clientResp.age).toBe(29);   
      expect(clientResp.dni).toBe("8851774");   
      expect(clientResp.gender).toBe("MALE");   
      expect(clientResp.name).toBe("JOSE LEMA");    
      expect(clientResp.phone).toBe("098254785"); 
      done()
    })
  })

  test('should delete an account', (done) => {

    
    const service = TestBed.get(ClientService)

    service.delete(client.id).subscribe( message => {
      expect("Client with ID "+ client.id + " deleted.").toBe(message.message)
      done()
    })
  })

});
