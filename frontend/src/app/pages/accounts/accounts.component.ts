
import { CurrencyPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Account } from '../../interfaces/account.interfaces';
import { Client } from '../../interfaces/client.interfaces';
import { AccountService } from '../../services/account.service';
import { ClientService } from '../../services/client.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {

  constructor(private accountService: AccountService, 
              private clientService: ClientService, 
              private currencyPipe: CurrencyPipe) { }

  filter: string; 
  clients: Client[] = []; 
  filteredClients: Client[] = []
  accounts: Account[] = [];
  filteredAccounts: Account[] = []; 

  loading = false; 
  saveDone = false; 
  deleteDone = false; 

  saveDialog; 
  deleteDialog; 
  customSelect; 
  formType; 
  message: string; 

  selectedAccount: Account; 

  form; 

  ngOnInit(): void {

    this.form = new FormGroup({
      'client': new FormControl(null, Validators.required), 
      'number': new FormControl(null, Validators.required), 
      'balance':new FormControl(null, Validators.required),
      'type'  : new FormControl(null, Validators.required),
      'state' : new FormControl(null, Validators.required)
    })

    this.clientService.getAll().subscribe(response => {
      this.clients = response;
      this.filteredClients = response; 
    })

    this.accountService.getAll().subscribe(response => {
      this.accounts = response;
      this.filteredAccounts = response;
    })

    this.saveDialog = document.getElementById("saveDialog");
    this.deleteDialog = document.getElementById("deleteDialog"); 
  }

  
  onFilter(){
    this.filteredAccounts = this.accounts.filter(
      a => a.clientName.toUpperCase().includes(this.filter.toUpperCase())     ||
      a.number.toString().includes(this.filter)                               ||
      a.balance.toString().includes(this.filter)                              ||
      a.type.toString().includes(this.filter.toUpperCase())                   ||
      (a.state == true ? "TRUE".includes(this.filter.toUpperCase()) : null
      || a.state == false ? "FALSE".includes(this.filter.toUpperCase()) : null)    
      
    )
  }
  onOpenSaveDialog(){
    this.formType = 'post'
    this.saveDone = false; 
    this.saveDialog.style.display = 'block'
    this.form = new FormGroup({
      'client': new FormControl(null, Validators.required), 
      'number': new FormControl(null, Validators.required), 
      'balance':new FormControl(null, Validators.required),
      'type'  : new FormControl(null, Validators.required),
      'state' : new FormControl(null, Validators.required)
    })
  }

  onCloseSaveDialog(){
    this.saveDialog.style.display = 'none'; 
    this.formType = null; 
    this.saveDone = false; 
  }

  onSaveAccount(){
    this.loading = true; 
    let client = (this.clients.filter(c => c.id == this.form.controls.client.value))[0]; 
    this.form.controls['balance'].setValue(Number(this.form.controls.balance.value.replaceAll(",", "").replace("$", "")));
    let newAccount: Account = this.form.value; 
    newAccount.client = client; 

    this.accountService.post(newAccount).subscribe(response => {
      this.loading = false; 
      this.saveDone = true; 
      this.filter = ""
      this.accounts.push(response); 
      this.message = "¡Cuenta creada exitosamente!"
    }, error => {
      this.saveDone = true; 
      this.loading = false; 
      this.message = "No se ha podido crear la cuenta. "+ error.error; 
    })
  }

  onOpenEditDialog(account: Account){
    this.formType = 'put'
    this.form = new FormGroup({
      'client': new FormControl({value: account.clientName, disabled: true}, Validators.required,), 
      'number': new FormControl(account.number, Validators.required), 
      'balance':new FormControl(account.balance, Validators.required),
      'type'  : new FormControl(account.type, Validators.required),
      'state' : new FormControl(account.state, Validators.required)
    })
    this.selectedAccount = account; 
    this.saveDialog.style.display = 'block'

    let num = this.currencyPipe.transform(Number(this.form.controls.balance.value), '$');
    this.form.controls['balance'].setValue(num);
  }

  onUpdateAccount(){
    this.loading = true; 
    let client = this.clients.filter(c => c.id == this.selectedAccount.clientId)[0];

    //Set balance 
    let balance = this.form.controls.balance.value.replaceAll(',', '').replaceAll('$', '')
    this.form.controls['balance'].setValue(Number(balance));

    //Create Account
    let newAccount : Account = this.form.value; 

    //Set client and id 
    newAccount.id = this.selectedAccount.id; 
    newAccount.client = client; 

    this.accountService.put(newAccount).subscribe(response => {
      this.loading = false;
      this.saveDone = true; 
      this.filter = ""; 

      let row: Account[] = this.accounts.filter(a => a.id == newAccount.id); 
      let index : number = this.accounts.indexOf(row[0]);
      this.accounts[index] = response; 
      this.message = "¡Cuenta actualizada exitosamente!"; 
    }, error => {
      this.saveDone = true;
      this.loading = false; 
      this.message = "No se ha podido actualizar la cuenta " + error.error; 
    })
  }

  onOpenDeleteDialog(account: Account){
    this.selectedAccount = account; 
    this.deleteDialog.style.display = 'block'; 
  }

  onCloseDeleteDialog(){
    this.deleteDialog.style.display = 'none'; 
    this.deleteDone = false;  
  }

  onDeleteAccount(){
    this.loading = true; 
    this.accountService.delete(this.selectedAccount.id).subscribe(response => {
      this.loading = false; 
      this.deleteDone = true; 
      let row: Account[] = this.accounts.filter(a => a.id == this.selectedAccount.id); 
      let index : number = this.accounts.indexOf(row[0]);
      this.accounts.splice(index, 1); 
      this.message = "Cuenta eliminada exitosamente"; 
    }, error => {
      this.deleteDone = true;
      this.loading = false; 
      this.message = "No se ha posiso eliminar la cuenta "+ error.error; 
    })
  }

  transformAmount(){
    let balance = this.form.controls.balance.value.replaceAll(',', '').replaceAll('$', ''); 
    let num = this.currencyPipe.transform(Number(balance), '$');
    this.form.controls['balance'].setValue(num); 
  }
}
