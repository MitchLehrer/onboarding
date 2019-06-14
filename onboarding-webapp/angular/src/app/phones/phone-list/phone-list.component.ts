import { Component, OnInit, Input } from '@angular/core';
import { Phone } from 'src/app/models/phone';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { DeletePhoneComponent } from '../delete-phone/delete-phone.component';
import { PhoneService } from 'src/app/services/phone.service';
import { Router } from '@angular/router';
import { VerifyPhoneComponent } from '../verify-phone/verify-phone.component';
import { CreatePhoneComponent } from '../create-phone/create-phone.component';
import { EditPhoneComponent } from '../edit-phone/edit-phone.component';

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

  ngOnChanges(){
    this.sortPhones();
  }
  

  refreshPhoneList(){
    this.phoneService.findAll(this.userId).subscribe(data => {
      this.phones = data;
      this.sortPhones();
    });
  }


  confirmDelete(phone:Phone){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={phone : phone, phoneToDisplay : this.formatPhoneNumber(phone.phoneNumber)}

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(DeletePhoneComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => this.refreshPhoneList()
    );    
  }

  sendVerification(phone:Phone){
    this.phoneService.sendVerificationCode(phone.userId, phone.phoneId).subscribe(response => {
      this.refreshPhoneList();
      this.submitVerification(phone);
    },
    err => {
      alert(JSON.stringify(err.error))
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

  createPhone(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={userId : this.userId}

    let dialogRef = this.dialog.open(CreatePhoneComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => (data && data.phoneCreated) ? this.refreshPhoneList() : null 
    );    
  }

  editPhone(phone:Phone){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={phone : phone}

    let dialogRef = this.dialog.open(EditPhoneComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => (data && data.phoneEdited) ? this.refreshPhoneList() : null 
    );    
  }

   submitVerification(phone:Phone){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={phone : phone}

    let dialogRef = this.dialog.open(VerifyPhoneComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => (data && data.phoneVerified) ? this.refreshPhoneList() : null 
    );    
  }

  sortPhones(){
    this.phones.sort((a,b) => +b.primary - +a.primary);
  }
  
}
