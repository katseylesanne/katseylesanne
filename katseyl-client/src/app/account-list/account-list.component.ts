import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../service/account.service';
import { Account } from '../model/account'

@Component({
  selector: 'app-account-list',
  templateUrl: './account-list.component.html',
  styleUrls: ['./account-list.component.css']
})
export class AccountListComponent implements OnInit {

  accounts: Account[];

  constructor(private route: ActivatedRoute, 
              private router: Router,
              private accountService: AccountService) { }

  edit(account: Account): void {
    this.router.navigate(['/editaccount', account.id ]);
  }

  delete(account: Account): void {
    this.accountService.delete(account.id).subscribe(result => { this.ngOnInit(); });
  }

  ngOnInit(): void {
    this.accountService.findAll().subscribe(data => { this.accounts = data });
  }

}
