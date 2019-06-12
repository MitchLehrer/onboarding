import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialogConfig, MatDialog } from '@angular/material';
import { DeleteUserComponent } from '../delete-user/delete-user.component';
import { Phone } from 'src/app/models/phone';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  user:User;
  phones: Phone[];

  constructor(private dialog: MatDialog, private userService:UserService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userService.find(params['id']).subscribe(data => {
        console.log(data);
        this.user = data;
        this.phones = this.user.phoneList;
      });
    });
  }

  confirmDelete(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data ={user : this.user}

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(DeleteUserComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => data.userDeleted ? this.router.navigate(['/']) : null
    );    
  }

}
