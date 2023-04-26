import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Account } from '../interfaces/account.interfaces';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) { }

  private baseUrl : string = environment.url_api + "accounts"

  getAll() : Observable<Account[]>{
    return this.http.get<Account[]>(this.baseUrl) 
  }

  post(account: Account) : Observable<Account> {
    return this.http.post<Account>(this.baseUrl, account); 
  }

  put(account: Account): Observable<Account> {
    return this.http.put<Account>(this.baseUrl, account); 
  }

  delete(id: number) : Observable<any>{
    return this.http.delete<any>(this.baseUrl+"/"+id); 
  }
}
