import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { VerifyPhoneComponent } from '../verify-phone/verify-phone.component';
import { PhoneService } from 'src/app/services/phone.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Phone } from 'src/app/models/phone';

@Component({
  selector: 'app-edit-phone',
  templateUrl: './edit-phone.component.html',
  styleUrls: ['./edit-phone.component.scss']
})
export class EditPhoneComponent implements OnInit {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<VerifyPhoneComponent>, private phoneService: PhoneService, private fb: FormBuilder) { }


  newPhoneForm = this.fb.group({
    phoneId:[''],
    phoneNumber: [''],
    primary: ['']
  });

  setPrimary: boolean;
  phoneToSubmit:Phone;
  errors: any;

  ngOnInit() {
    this.newPhoneForm.patchValue(this.data.phone);
  }

  close() {
    this.dialogRef.close();
  }

  editPhone() {
    this.phoneToSubmit = this.newPhoneForm.getRawValue() as Phone;
    this.setPrimary = this.phoneToSubmit.primary;
    this.phoneToSubmit.primary=false;
    this.phoneService.update(this.data.phone.userId, this.phoneToSubmit).subscribe(
      data => {
        if(this.setPrimary){
          this.makePhonePrimary(data);
        }else{
          this.phoneEdited();
        }
      },
      err => {
        this.errors = err.error;
        console.log(this.errors);
      }
    );
  }

  phoneEdited() {
    alert("Phone was successfully edited!");
    this.dialogRef.close({ phoneEdited: true });
  }

  makePhonePrimary(phone: Phone) {
    this.phoneService.setPrimary(this.data.phone.userId, phone.phoneId).subscribe(data => {
      this.phoneEdited();
    },
      err => {
        alert(JSON.stringify(err.error))
      });
  }
}
