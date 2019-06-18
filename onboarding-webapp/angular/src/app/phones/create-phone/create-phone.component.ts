import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { VerifyPhoneComponent } from '../verify-phone/verify-phone.component';
import { PhoneService } from 'src/app/services/phone.service';
import { FormBuilder, NgForm, Validators } from '@angular/forms';
import { Phone } from 'src/app/models/phone';

@Component({
  selector: 'app-create-phone',
  templateUrl: './create-phone.component.html',
  styleUrls: ['./create-phone.component.scss']
})
export class CreatePhoneComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<VerifyPhoneComponent>, private phoneService: PhoneService,private fb : FormBuilder) { }

  newPhoneForm = this.fb.group({
    phoneNumber: ['',[Validators.required, Validators.maxLength(10),]],
    makePrimary:['']
  });

  userId: string;
  newPhoneNumber:string;
  phoneToSubmit:Phone;
  makePrimary:boolean;
  errors:string[];

  ngOnInit() {
    this.userId = this.data.userId;
  }

  close() {
    this.dialogRef.close();
  }

  createPhone() {
    this.newPhoneNumber = this.newPhoneForm.get('phoneNumber').value;
    this.makePrimary = this.newPhoneForm.get('makePrimary').value;
    this.phoneToSubmit = new Phone();
    this.phoneToSubmit.phoneNumber = this.newPhoneNumber;

    this.makePrimary;


    this.phoneService.save(this.userId, this.phoneToSubmit).subscribe(
      data => {
          if(this.makePrimary){
            this.makePhonePrimary(data.phoneId);
          }else{
            this.phoneCreated();
          }
      },
      err => {
        alert(JSON.stringify(err.error));
      }
    );
  }

  phoneCreated(){
    alert("Phone was successfully created!");
    this.dialogRef.close({ phoneCreated: true});
  }

  makePhonePrimary(phoneId:string){
    this.phoneService.setPrimary(this.userId, phoneId).subscribe(data => {
        this.phoneCreated();
    },
    err =>{
      alert(JSON.stringify(err.error));
    });
  }

}
