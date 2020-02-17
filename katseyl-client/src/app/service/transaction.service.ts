import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../model/transaction';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  transactionUrl: string;

  constructor(private http: HttpClient) {
    this.transactionUrl = 'http://localhost:8080/api/transaction';
  }

  processTransaction(transaction: Transaction) {
    return this.http.post(this.transactionUrl, transaction);
  }
}
