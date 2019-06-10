import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  constructor(private userService: UserService, private router: Router) { }

  users: User[];

  ngOnInit() {
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  refreshUserList(){
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  navigateToUser(user:User) {
    console.log(user);
    this.router.navigateByUrl('/users/'+ user.userId);
  };

}
