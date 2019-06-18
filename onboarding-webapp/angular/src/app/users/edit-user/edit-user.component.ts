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
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<VerifyPhoneComponent>, private userService: UserService, private fb: FormBuilder) { }


  userToSubmit: User;

  editUserForm = this.fb.group({
    userId: [''],
    username: [{ value: '', disabled: true }],
    firstName: [''],
    lastName: [''],
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
    this.editUserForm.patchValue(this.data.user);
  }

  close() {
    this.dialogRef.close();
  }

  editUser() {

    this.userToSubmit = this.editUserForm.getRawValue() as User;
    this.userService.update(this.userToSubmit).subscribe(
      data => {
        this.userEdited();
        console.log(data);
      },
      err => {
        alert(JSON.stringify(err.error));
      }
    );
  }

  userEdited() {
    alert("User was successfully edited!");
    this.dialogRef.close({ userEdited: true });
  }

}
