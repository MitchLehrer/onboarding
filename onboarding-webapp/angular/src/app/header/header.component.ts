import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  //FIXME remove unused constructor and OnInit. we remove all unused code as all code must serve a purpose
  constructor() { }

  ngOnInit() {
  }

}
