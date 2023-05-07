import { Component, OnInit } from '@angular/core';
import { Client } from '../../interfaces/client.interfaces';
import { Report } from '../../interfaces/report.interface';
import { ClientService } from '../../services/client.service';
import { ReportService } from '../../services/report.service';
import * as fileSaver from 'file-saver';

@Component({
  selector: 'app-reports',
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css']
})
export class ReportsComponent implements OnInit {

  constructor(private clientService: ClientService, 
              private reportService:ReportService) { }

  clients: Client[] =[];

  startDate; 
  endDate; 
  clientId; 

  showTable = false; 

  reports: Report[] =[]; 
  filteredReports: Report[] = []

  ngOnInit(): void {

    this.clientService.getAll().subscribe(response => {
      this.clients = response; 
    })
  }

  disableShow(){
    if(this.startDate == undefined || this.endDate == undefined || this.clientId == undefined) return true
    return false;
  }

  disableDownload(){
    if(this.startDate == undefined || this.endDate == undefined || this.clientId == undefined) return true
    return false;
  }

  onShow(){
    let startDate = this.startDate.toLocaleString().replaceAll("-", ""); 
    let endDate = this.endDate.toLocaleString().replaceAll("-", ""); 
  
    this.reportService.getReport(startDate, endDate, this.clientId).subscribe(response => {
      this.reports = response; 
      this.filteredReports = response; 
    })
    this.showTable = true; 

  }

  onDownload(){
    let startDate = this.startDate.toLocaleString().replaceAll("-", ""); 
    let endDate = this.endDate.toLocaleString().replaceAll("-", ""); 

    this.reportService.downloasReport(startDate, endDate, this.clientId).subscribe(res => {
      
      let blob: any = new Blob([res], {type: 'application/pdf'})
      fileSaver.saveAs(blob, 'Reporte.pdf')

    })
  }

  getCurrentDate(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }

}
