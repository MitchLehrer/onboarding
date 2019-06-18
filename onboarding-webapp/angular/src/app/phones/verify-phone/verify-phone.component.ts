import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { PhoneService } from 'src/app/services/phone.service';
import { Phone } from 'src/app/models/phone';
import { FormsModule, NgForm, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-verify-phone',
  templateUrl: './verify-phone.component.html',
  styleUrls: ['./verify-phone.component.scss']
})
export class VerifyPhoneComponent implements OnInit {

  verificationCode: string;

  verificationForm = this.fb.group({
    verificationCode: [''],
  });


  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<VerifyPhoneComponent>, private phoneService: PhoneService, private fb: FormBuilder) { }

  phoneToVerify: Phone;

  ngOnInit() {
    this.phoneToVerify = this.data.phone;
  }

  close() {
    this.dialogRef.close();
  }

  submitVerificationCode() {
    this.verificationCode = this.verificationForm.get('verificationCode').value;
    this.phoneService.submitVerificationCode(this.phoneToVerify.userId, this.phoneToVerify.phoneId, this.verificationCode).subscribe(data => {
      this.phoneVerified();
    },
      err => {
        alert(JSON.stringify(err.error))
      });
  }

  phoneVerified() {
    alert("Phone was successfully verified!");
    this.dialogRef.close({ phoneVerified: true });
  }

  resendVerificationCode() {
    this.phoneService.sendVerificationCode(this.phoneToVerify.userId, this.phoneToVerify.phoneId).subscribe(response => {
    },
      err => {
        alert(JSON.stringify(err.error))
      });
  }


}
