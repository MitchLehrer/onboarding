import { Component, OnInit, Input } from '@angular/core';
import { Phone } from 'src/app/models/phone';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { DeletePhoneComponent } from '../delete-phone/delete-phone.component';
import { PhoneService } from 'src/app/services/phone.service';
import { Router } from '@angular/router';
import { VerifyPhoneComponent } from '../verify-phone/verify-phone.component';

@Component({
  selector: 'app-phone-list',
  templateUrl: './phone-list.component.html',
  styleUrls: ['./phone-list.component.scss']
})
export class PhoneListComponent implements OnInit {

  @Input() phones:Phone[];
  @Input() userId:string;
  constructor(private dialog: MatDialog, private phoneService:PhoneService, private router: Router) { }

  ngOnInit() {
  }
  

  refreshPhoneList(){
    this.phoneService.findAll(this.userId).subscribe(data => {
      this.phones = data;
    });
  }


  confirmDelete(phone:Phone){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={phone : phone, phoneToDisplay : this.formatPhoneNumber(phone.phoneNumber)}

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(DeletePhoneComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => this.refreshPhoneList()
    );    
  }

  sendVerification(phone:Phone){
    console.log(phone);
    this.phoneService.sendVerificationCode(phone.userId, phone.phoneId).subscribe(response => {
      console.log(response);
      this.refreshPhoneList();
      console.log(phone.verificationCode);
    });
  }


  public formatPhoneNumber(phoneNumberString) {
    var cleaned = ('' + phoneNumberString).replace(/\D/g, '')
    var match = cleaned.match(/^(\d{3})(\d{3})(\d{4})$/)
    if (match) {
      return '(' + match[1] + ') ' + match[2] + '-' + match[3]
    }
    return null
  }

   submitVerification(phone:Phone){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={phone : phone}

    let dialogRef = this.dialog.open(VerifyPhoneComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => data.phoneVerified ? this.refreshPhoneList() : null 
    );    
  }
  
}
