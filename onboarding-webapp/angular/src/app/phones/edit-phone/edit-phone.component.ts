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
  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<VerifyPhoneComponent>, private phoneService: PhoneService,private fb : FormBuilder) { }

  oldPhone:Phone;

  PhoneNumber:string;
  phoneToSubmit:Phone;
  primary: boolean;

  newPhoneForm = this.fb.group({
    phoneNumber: ['',[Validators.required, Validators.maxLength(10),]],
    makePrimary:['']
  });


  ngOnInit() {
    this.oldPhone = this.data.phone;
    this.newPhoneForm.patchValue({phoneNumber : this.oldPhone.phoneNumber});
  }

  close() {
    this.dialogRef.close();
  }

  editPhone() {
    this.PhoneNumber = this.newPhoneForm.get('phoneNumber').value;
    this.primary = this.newPhoneForm.get('makePrimary').value;
    this.phoneToSubmit = this.oldPhone;
    this.phoneToSubmit.phoneNumber = this.PhoneNumber;

    this.phoneService.update(this.oldPhone.userId, this.phoneToSubmit).subscribe(
      data => {
        if(data.status == 200){
          if(this.primary){
            this.makePhonePrimary(data.body.phoneId);
          }else{
            this.phoneCreated();
          }
        }
      },
      err => {
        alert(JSON.stringify(err.error));
      }
    );
  }

  phoneCreated(){
    alert("Phone was successfully created!");
    this.dialogRef.close({ phoneEdited: true});
  }

  makePhonePrimary(phoneId:string){
    this.phoneService.setPrimary(this.oldPhone.userId, phoneId).subscribe(data => {
      if(data.status == 200){
        this.phoneCreated();
      }
    },
    err => {
      alert(JSON.stringify(err.error))
    });
  }

}
