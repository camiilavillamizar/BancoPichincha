import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Report } from '../interfaces/report.interface';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient) { }

  private baseUrl : string = environment.url_api + "reports"

  getReport(startDate: string, endDate: string, clientId: number) : Observable<Report[]>{
    
    const options = { 
      params: new HttpParams()
      .set('startDate', startDate) 
      .set('endDate', endDate)
      .set('clientId', clientId.toString())
    } ;
    return this.http.get<Report[]>(this.baseUrl, options)
  }

  downloasReport(startDate: string, endDate: string, clientId: number) : Observable<any>{
    
    const requestOptions: Object = {
      params: new HttpParams()
      .set('startDate', startDate) 
      .set('endDate', endDate)
      .set('clientId', clientId.toString()),
      responseType: 'blob'
    }

    return this.http.get<Report[]>(this.baseUrl + "/pdf",  requestOptions)
  }
}
