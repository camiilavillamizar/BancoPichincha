import { Component, OnInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Client } from '../../interfaces/client.interfaces';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {

  constructor(private clientService: ClientService) { }

  filter: string; 
  clients: Client[] = []; 
  filteredClients: Client[] =[]

  loading = false; 
  saveDone = false; 
  deleteDone = false; 
  saveDialog;
  deleteDialog; 
  formType; 
  message: string; 
  

  selectedUser: Client; 
  
  createForm;

  ngOnInit(): void {

    this.createForm = new FormGroup({
      'name': new FormControl(null, Validators.required),
      'gender': new FormControl(null, Validators.required), 
      'age' : new FormControl(null, Validators.required),
      'dni':  new FormControl(null, Validators.required),
      'address' : new FormControl(null, Validators.required),
      'phone': new FormControl(null, Validators.required),
      'password': new FormControl(null, Validators.required), 
      'state': new FormControl(null, Validators.required)
    });

    this.clientService.getAll().subscribe(response => {
      this.clients = response;
      this.filteredClients = response; 
    })

    this.saveDialog = document.getElementById("saveDialog");
    this.deleteDialog = document.getElementById("deleteDialog"); 
  }
  onFilter(){
    this.filteredClients = this.clients.filter(
      c => c.name.toUpperCase().includes(this.filter.toUpperCase())   || 
      c.gender.toUpperCase().includes(this.filter.toUpperCase())         ||
      c.age.toString().includes(this.filter.toUpperCase()) ||
      c.dni.toUpperCase().includes(this.filter.toUpperCase())            ||
      c.address.toUpperCase().includes(this.filter.toUpperCase())        ||
      c.phone.toUpperCase().includes(this.filter.toUpperCase())          ||
      (c.state == true ? "TRUE".includes(this.filter.toUpperCase()) : null
      || c.state == false ? "FALSE".includes(this.filter.toUpperCase()) : null)    
      )
  }

  onOpenSaveDialog(){
    this.formType = 'post'
    this.saveDone = false; 
    this.saveDialog.style.display = 'block'
    this.createForm = new FormGroup({
      'name': new FormControl(null, Validators.required),
      'gender': new FormControl(null, Validators.required), 
      'age' : new FormControl(null, Validators.required),
      'dni':  new FormControl(null, Validators.required),
      'address' : new FormControl(null, Validators.required),
      'phone': new FormControl(null, Validators.required),
      'password': new FormControl(null, Validators.required), 
      'state': new FormControl(null, Validators.required)
    });
  }
  onCloseSaveDialog(){
    this.saveDialog.style.display = 'none'; 
    this.formType = null; 
    this.saveDone = false; 
  }
  
  onSaveClient(){
    this.loading = true; 
    let newClient : Client = this.createForm.value;

    this.clientService.post(newClient).subscribe(response => {
      this.loading = false; 
      this.saveDone = true; 
      this.filter = ""
      this.clients.push(response)
      this.message = "¡Cliente creado exitosamente!"
    }, error => {
      this.saveDone = true; 
      this.loading = false; 
      this.message = "No se ha podido crear el cliente "+ error.error; 
    })

  }

  onOpenEditDialog(client: Client){

    this.formType = 'put'
    this.createForm = new FormGroup({
      'name': new FormControl(client.name, Validators.required),
      'gender': new FormControl(client.gender, Validators.required), 
      'age' : new FormControl(client.age, Validators.required),
      'dni':  new FormControl(client.dni, Validators.required),
      'address' : new FormControl(client.address, Validators.required),
      'phone': new FormControl(client.phone, Validators.required),
      'password': new FormControl(client.password, Validators.required), 
      'state': new FormControl(client.state, Validators.required)
    });

    this.selectedUser = client; 
    this.saveDialog.style.display = 'block'

  }
  onUpdateClient(){

    this.loading = true; 
    let newClient : Client = this.createForm.value;
    newClient.id = this.selectedUser.id; 

    this.clientService.put(newClient).subscribe(response => {
      this.loading = false; 
      this.saveDone = true; 
      this.filter = ""

      let row : Client[] = this.clients.filter(c => c.id == newClient.id);
      let index: number = this.clients.indexOf(row[0])
      this.clients[index] = response;
      this.message = "¡Cliente actualizado exitosamente!"
    }, error => {
      this.saveDone = true; 
      this.loading = false; 
      this.message = "No se ha podido actualizar el cliente "+ error.error; 
    })
  }

  onOpenDeleteDialog(client: Client){
    this.selectedUser = client; 
    this.deleteDialog.style.display = 'block'; 
  }
  onCloseDeleteDialog(){
    this.deleteDialog.style.display = 'none'; 
    this.deleteDone = false;  
  }
  onDeleteClient(){
    this.loading = true; 
    this.clientService.delete(this.selectedUser.id).subscribe(response => {
      this.loading = false; 
      this.deleteDone = true; 
      let row : Client[] = this.clients.filter(c => c.id == this.selectedUser.id);
      let index: number = this.clients.indexOf(row[0])
      this.clients.splice(index, 1)
      this.message = "¡Cliente eliminado exitosamente!"
    }, error => {
      this.deleteDone = true; 
      this.loading = false; 
      this.message = "No se ha podido eliminar el cliente "+ error.error; 
    }) 
  }
}
