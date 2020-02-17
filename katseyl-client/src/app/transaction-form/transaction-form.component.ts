import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Account } from '../model/account'
import { Transaction } from '../model/transaction';
import { AccountService } from '../service/account.service';
import { TransactionService } from '../service/transaction.service';

@Component({
  selector: 'app-transaction-form',
  templateUrl: './transaction-form.component.html',
  styleUrls: ['./transaction-form.component.css']
})
export class TransactionFormComponent implements OnInit {

  accounts: Account[];
  transaction: Transaction;

  constructor(private route: ActivatedRoute, 
              private router: Router,
              private accountService: AccountService,
              private transactionService: TransactionService) {
    this.transaction = new Transaction();
  }

  onSubmit() {
    this.transactionService.processTransaction(this.transaction).subscribe(result => this.gotoAccountList());
  }

  gotoAccountList() {
    this.router.navigate(['/account']);
  }

  ngOnInit(): void {
    this.accountService.findAll().subscribe(data => { this.accounts = data });
  }

}
