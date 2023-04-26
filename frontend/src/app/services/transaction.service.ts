import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Transaction } from '../interfaces/transaction.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  constructor(private http: HttpClient) { }
  
  private baseUrl : string = environment.url_api + "transactions"

  getAll() : Observable<Transaction[]>{
    return this.http.get<Transaction[]>(this.baseUrl) 
  }

  post(transaction: Transaction) : Observable<Transaction> {
    return this.http.post<Transaction>(this.baseUrl, transaction); 
  }

  put(transaction: Transaction): Observable<Transaction> {
    return this.http.put<Transaction>(this.baseUrl, transaction); 
  }

  delete(id: number) : Observable<any>{
    return this.http.delete<any>(this.baseUrl+"/"+id); 
  }
}
