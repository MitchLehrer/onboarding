import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UserListComponent } from './users/user-list/user-list.component';
import { CreateUserComponent } from './users/create-user/create-user.component';
import { UserComponent } from './users/user/user.component';

const routes: Routes = [
  {
    path: '', 
    component: UserListComponent,
  },
  {
    path: 'users',
    component: UserListComponent,
  },
  {
    path: 'create-user',
    component: CreateUserComponent
  },
  {
    path: 'users/:id',
    component: UserComponent,
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
