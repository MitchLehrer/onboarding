import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Phone } from 'src/app/models/phone';
import { PhoneService } from 'src/app/services/phone.service';

@Component({
  selector: 'app-delete-phone',
  templateUrl: './delete-phone.component.html',
  styleUrls: ['./delete-phone.component.scss']
})
export class DeletePhoneComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private dialogRef: MatDialogRef<DeletePhoneComponent>, private phoneService: PhoneService) { }

  phoneToDelete: Phone;
  phoneNumber: string;

  ngOnInit() {
    this.phoneToDelete = this.data.phone;
    this.phoneNumber= this.data.phoneToDisplay;
  }

  close() {
    this.dialogRef.close();
  }

  deletePhone() {
    this.phoneService.delete(this.phoneToDelete.userId, this.phoneToDelete.phoneId).subscribe(data => {
    });
    this.dialogRef.close({ phoneDeleted: true });
  }


}

