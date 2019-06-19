import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DeleteUserComponent } from '../delete-user/delete-user.component';
import { EditUserComponent } from '../edit-user/edit-user.component';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})


export class UserListComponent implements OnInit {
  
  page:number;
  pageSize=4;

  constructor(private dialog: MatDialog, private userService: UserService, private router: Router, private fb: FormBuilder) { this.page=1;}

  users: User[];
  totalElements: number;
  searchInput: string;


  ngOnInit() {
    this.getAllUsers(null);
  }

  refreshUserList() {
    this.searchInput = null;
    this.page=1;
    this.getAllUsers(null);
  }

  onSearchChange(search:string){
    this.page=1;
    this.getAllUsers(search);
  }

  getAllUsers(search:string){
    this.userService.findAllByPage(this.page - 1, this.pageSize, search).subscribe(data => {
      this.users = data.content;
      this.totalElements = data.totalElements;
    });
  }

  navigateToUser(user: User) {
    this.router.navigateByUrl('/users/' + user.userId);
  };

  confirmDelete(user: User) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = { user: user }

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(DeleteUserComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {console.log(data),
      this.refreshUserList()}
    );
  }

  editUser(user: User) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = { user: user }

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(EditUserComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {
        if (data && data.userEdited) {
          this.refreshUserList()
        }
      });
  }
}