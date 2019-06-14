import { Component, OnInit, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.scss']
})
export class DeleteUserComponent implements OnInit {

  constructor(  @Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<DeleteUserComponent>, private userService: UserService) { }

  userToDelete : User;

  ngOnInit() {
    this.userToDelete = this.data.user;
  }

  close(){
    this.dialogRef.close();
  }

  deleteUser(){
    this.userService.delete(this.userToDelete.userId).subscribe(data => {
        if(data.status == 204){
          this.dialogRef.close({ userDeleted: true});
        }
    },
    err =>{
      alert(JSON.stringify(err.error));
      this.dialogRef.close();
    });
  }

}
