import { CurrencyPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Account } from '../../interfaces/account.interfaces';
import { Transaction } from '../../interfaces/transaction.interface';
import { AccountService } from '../../services/account.service';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  constructor(private accountService: AccountService, 
              private transactionService: TransactionService, 
              private currencyPipe: CurrencyPipe) { }

  filter: string; 
  accounts : Account[] = []
  transactions: Transaction[] = []
  filteredTransactions : Transaction[] = []; 

  loading = false; 
  saveDone = false; 
  deleteDone = false; 

  saveDialog; 
  deleteDialog; 
  formType; 
  message: string; 

  selectedTransaction: Transaction; 

  form; 
  ngOnInit(): void {
    this.form = new FormGroup({
      'account' : new FormControl(null, Validators.required), 
      'date'    : new FormControl(null, Validators.required), 
      'amount'  : new FormControl(null, Validators.required), 
      'type'    : new FormControl(null, Validators.required)
    })

    this.accountService.getAll().subscribe(response => {
      this.accounts = response; 
    })

    this.transactionService.getAll().subscribe(response => {
      this.transactions = response; 
      this.filteredTransactions = response; 
    })

    this.saveDialog = document.getElementById("saveDialog");
    this.deleteDialog = document.getElementById("deleteDialog"); 

  }

  onFilter(){
    this.filteredTransactions = this.transactions.filter(
      t => t.account.clientName.toUpperCase().includes(this.filter.toUpperCase()) ||
      t.date.toString().includes(this.filter)                                     ||
      t.account.number.toString().includes(this.filter)                           ||
      t.account.type.includes(this.filter.toUpperCase())                          ||
      t.type.includes(this.filter.toUpperCase())                                  ||
      t.amount.toString().includes(this.filter)                                   ||
      t.balance.toString().includes(this.filter)
    )
  }

  onOpenSaveDialog(){
    this.formType = 'post'
    this.saveDone = false; 
    this.saveDialog.style.display = 'block'
    this.form = new FormGroup({
      'account' : new FormControl(null, Validators.required), 
      'date'    : new FormControl(null, Validators.required), 
      'amount'  : new FormControl(null, Validators.required), 
      'type'    : new FormControl(null, Validators.required)
    })
  }

  onCloseSaveDialog(){
    this.saveDialog.style.display = 'none'; 
    this.formType = null; 
    this.saveDone = false; 
  }
  onSaveTransaction(){
    this.loading = true;
    let account = (this.accounts.filter(a => a.id == this.form.controls.account.value))[0]
    this.form.controls['amount'].setValue(Number(this.form.controls.amount.value.replaceAll(",", "").replace("$", "")))
    let newTransaction : Transaction = this.form.value; 
    newTransaction.account = account; 

    this.transactionService.post(newTransaction).subscribe(resposne => {
      this.loading = false; 
      this.saveDone = true; 
      this.filter = ""
      this.transactions.push(resposne); 
      this.message = "¡Transacción creada exitosamente!"
    }, error => {
      this.saveDone = true; 
      this.loading = false; 
      this.message = "No se ha podido crear la transacción. "+ error.error; 
    })
  }

  onOpenEditDialog(transaction: Transaction){
    this.formType = 'put'
    let account: string = transaction.account.clientName +  " - " + transaction.account.number + " (" + transaction.account.type + ")";
    this.form = new FormGroup({
      'account' : new FormControl({value: account, disabled: true}, Validators.required), 
      'date'    : new FormControl({value :transaction.date, disabled: true}, Validators.required), 
      'amount'  : new FormControl( Math.abs(transaction.amount), Validators.required), 
      'type'    : new FormControl({value : transaction.type, disabled: true}, Validators.required)
    })

    this.selectedTransaction = transaction; 
    this.saveDialog.style.display = 'block'
    let num = this.currencyPipe.transform(Number(this.form.controls.amount.value), '$');
    this.form.controls['amount'].setValue(num);

  }

  onUpdateTransaction(){ 

    this.loading = true; 
    let amount = this.form.controls.amount.value; 
    amount = amount.replaceAll(",", ""); 
    amount = amount.replace("$", "")
    this.form.controls['amount'].setValue(Number(amount))
    let newTransaction : Transaction = this.form.value; 
    newTransaction.id = this.selectedTransaction.id; 

    let account = (this.accounts.filter(a => a.id == this.selectedTransaction.account.id))[0]
    newTransaction.account = account; 

    newTransaction.balance = 0;
    newTransaction.type = this.selectedTransaction.type; 
    newTransaction.date = this.selectedTransaction.date; 

    this.transactionService.put(newTransaction).subscribe(response => {
      this.loading = false;
      this.saveDone = true; 
      this.filter = ""; 
      
      let row: Transaction[] = this.transactions.filter(t => t.id == newTransaction.id); 
      let index: number = this.transactions.indexOf(row[0]); 
      this.transactions[index] = response; 
      this.message = "¡Transacción actualizada correctamente!"
    }, error => {
      this.saveDone = true;
      this.loading = false; 
      this.message = "No se ha podido actualizar la transacción " + error.error; 
    })
  }

  onOpenDeleteDialog(transaction: Transaction){
    this.selectedTransaction = transaction; 
    this.deleteDialog.style.display = 'block'; 
  }
  onCloseDeleteDialog(){
    this.deleteDialog.style.display = 'none'; 
    this.deleteDone = false;  
  }

  onDeleteTransaction(){
    this.loading = true; 
    this.transactionService.delete(this.selectedTransaction.id).subscribe(response => {
      this.loading = false; 
      this.deleteDone = true; 
      let row: Transaction[] = this.transactions.filter(t => t.id == this.selectedTransaction.id); 
      let index : number = this.transactions.indexOf(row[0]); 
      this.transactions.splice(index, 1); 
      this.message = "Transacción eliminada exitosamente"
    }, error => {
      this.deleteDone = true;
      this.loading = false; 
      this.message = "No se ha podido eliminar la transacción "+ error.error; 
    })
  }

  transformAmount(){
    let amount = this.form.controls.amount.value.replaceAll(',', '').replaceAll('$', ''); 
    let num = this.currencyPipe.transform(Number(amount), '$');
    this.form.controls['amount'].setValue(num); 
  }

}
