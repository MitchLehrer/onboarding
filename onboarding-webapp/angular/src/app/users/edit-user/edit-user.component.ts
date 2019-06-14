import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { VerifyPhoneComponent } from 'src/app/phones/verify-phone/verify-phone.component';
import { FormBuilder, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<VerifyPhoneComponent>, private userService: UserService, private fb : FormBuilder) { }

  oldUser:User;
  userToSubmit : User;

  editUserForm = this.fb.group({
    username: [{value:'', disabled:true}],
    firstName: ['', [Validators.required, Validators.maxLength(50)]],
    lastName: ['', [Validators.required, Validators.maxLength(50)]],
  });

  get firstName() {
    return this.editUserForm.get('firstName');
  }

  get lastName() {
    return this.editUserForm.get('lastName');
  }

  get username() {
    return this.editUserForm.get('username');
  }


  ngOnInit() {
    this.oldUser = this.data.user;
    this.userToSubmit = this.oldUser;
    this.editUserForm.patchValue(this.oldUser);
  }

  close() {
    this.dialogRef.close();
  }

  editUser() {
    this.userToSubmit.username = this.username.value;
    this.userToSubmit.firstName = this.firstName.value;
    this.userToSubmit.lastName = this.lastName.value;
    this.userService.update(this.userToSubmit).subscribe(
      data => {
        if(data.status == 200){
            this.userEdited();
          }
      },
      err => {
        alert(JSON.stringify(err.error));
      }
    );
  }

  userEdited(){
    alert("User was successfully edited!");
    this.dialogRef.close({ userEdited: true});
  }

}
