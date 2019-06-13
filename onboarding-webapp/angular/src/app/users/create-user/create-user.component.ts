import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { Validators, FormArray, ValidatorFn, AbstractControl } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { Phone } from 'src/app/models/phone';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  newUser: User;

  newUserForm = this.fb.group({
    username: ['', [Validators.required, Validators.maxLength(20),]],
    firstName: ['', [Validators.required, Validators.maxLength(50)]],
    lastName: ['', [Validators.required, Validators.maxLength(50)]],
    phoneList: this.fb.array([this.fb.group({
      phoneNumber: ['', [Validators.required, Validators.maxLength(10), Validators.minLength(10)]],
      primary:['true']
  })])
  });



  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private fb : FormBuilder) {
  }

  onSubmit() {
    this.newUser = this.newUserForm.getRawValue() as User;
    this.userService.save(this.newUser).subscribe(response => {
      if (response.status == 201) {
        this.userCreated();
      }
      else {
        alert("Error creating user");
      }
    },
    err => {
      alert(JSON.stringify(err.error));
    });
  }

  ngOnInit() {
  }

  userCreated() {
    alert("User successfully created!");
    this.router.navigate(['/']);
  }

  get phoneList() {
    return this.newUserForm.get('phoneList') as FormArray;
  }

  get firstName() {
    return this.newUserForm.get('firstName');
  }

  get lastName() {
    return this.newUserForm.get('lastName');
  }

  get username() {
    return this.newUserForm.get('username');
  }

  addPhone() {
    this.phoneList.push(
      this.fb.group({
        phoneNumber: ['',[Validators.required, Validators.maxLength(10), Validators.minLength(10)]],
        primary:['false']
    }))
  }

  removePhone() {
    if (this.phoneList.length > 1) {
      this.phoneList.removeAt(this.phoneList.length - 1);
    }
  }

}