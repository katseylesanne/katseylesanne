import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Currency } from '../model/currency';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  currencyUrl: string;

  constructor(private http: HttpClient) {
	this.currencyUrl = 'http://localhost:8080/api/currency';
  }

  public findAll(): Observable<Currency[]> {
    return this.http.get<Currency[]>(this.currencyUrl);
  }
}
