import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-title-section',
  templateUrl: './title-section.component.html',
  styleUrls: ['./title-section.component.css']
})
export class TitleSectionComponent implements OnInit {

  @Input() page: string = ""; 
  constructor() { }

  title: string = ""; 
  src: string = ""; 
  ngOnInit(): void {

    switch(this.page){
      case 'client': 
            this.title = "Clientes"
            this.src = "../../../assets/clients.svg"
            break;
      case 'account':
            this.title = "Cuentas"
            this.src = "../../../assets/accounts.svg"
            break; 
      case 'transaction':
            this.title = "Movimientos"
            this.src = "../../../assets/transactions.svg"
            break;
      case 'report':
            this.title = "Reportes"
            this.src = "./../../assets/report.svg"
            break; 
      default:
            break; 

    }
  }

}
