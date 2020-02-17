import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, ParamMap } from '@angular/router';
import { Account } from '../model/account'
import { Currency } from '../model/currency';
import { AccountService } from '../service/account.service';
import { CurrencyService } from '../service/currency.service';
import { of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-account-form',
  templateUrl: './account-form.component.html',
  styleUrls: ['./account-form.component.css']
})
export class AccountFormComponent implements OnInit {

  account: Account;
  currencies: Currency[];

  constructor(private route: ActivatedRoute, 
              private router: Router, 
              private accountService: AccountService,
              private currencyService: CurrencyService) {
    this.account = new Account();
    this.account.currency = new Currency();
  }

  onSubmit() {
    this.accountService.save(this.account).subscribe(result => this.gotoAccountList());
  }

  gotoAccountList() {
    this.router.navigate(['/account']);
  }

  ngOnInit() {
    this.currencyService.findAll().subscribe(data => { this.currencies = data });

    this.route.paramMap.pipe(
      switchMap((params: ParamMap) => {
        let id = params.get('id');
        if (id === null) {
          return of(this.account);
        } else {
          return this.accountService.get(id);
        }
      })
    ).subscribe(account => { this.account = account; });

//    let id = this.route.snapshot.paramMap.get('id');
//    this.account = this.accountService.get(id);
  }

}
