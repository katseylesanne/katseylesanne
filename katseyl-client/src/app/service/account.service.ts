import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../model/account'

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  accountUrl: string;

  constructor(private http: HttpClient) {
	this.accountUrl = 'http://localhost:8080/api/account';
  }

  public findAll(): Observable<Account[]> {
    return this.http.get<Account[]>(this.accountUrl);
  }

  public get(id: string): Observable<Account> {
    const getUrl = `${this.accountUrl}/${id}`;
    return this.http.get<Account>(getUrl);
  }

  public save(account: Account) {
    return this.http.post<Account>(this.accountUrl, account);
  }

  public delete(id: number) {
    const deleteUrl = `${this.accountUrl}/${id}`;
    return this.http.delete(deleteUrl);
  }
}
