import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AccountListComponent } from './account-list/account-list.component';
import { AccountFormComponent } from './account-form/account-form.component';
import { TransactionFormComponent } from './transaction-form/transaction-form.component';


const routes: Routes = [
  { path: 'account', component: AccountListComponent },
  { path: 'addaccount', component: AccountFormComponent },
  { path: 'editaccount/:id', component: AccountFormComponent },
  { path: 'transaction', component: TransactionFormComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
