import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { UserListComponent } from './users/user-list/user-list.component';
import { FooterComponent } from './footer/footer.component';
import { HttpClientModule } from '@angular/common/http';
import { UserComponent } from './users/user/user.component';
import { CreateUserComponent } from './users/create-user/create-user.component'; 
import { FormsModule }   from '@angular/forms';
import { EditUserComponent } from './users/edit-user/edit-user.component';
import { MatDialogModule} from '@angular/material/dialog';
import { DeleteUserComponent } from './users/delete-user/delete-user.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { NgxMaskModule, IConfig } from 'ngx-mask';
import { DeletePhoneComponent } from './phones/delete-phone/delete-phone.component';
import { EditPhoneComponent } from './phones/edit-phone/edit-phone.component';
import { PhoneListComponent } from './phones/phone-list/phone-list.component';
import { VerifyPhoneComponent } from './phones/verify-phone/verify-phone.component';
import { CreatePhoneComponent } from './phones/create-phone/create-phone.component';
import { SearchFilterPipe } from './filter/search-filter.pipe';

export var options: Partial<IConfig> | (() => Partial<IConfig>);

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    UserListComponent,
    FooterComponent,
    UserComponent,
    CreateUserComponent,
    EditUserComponent,
    DeleteUserComponent,
    DeletePhoneComponent,
    EditPhoneComponent,
    PhoneListComponent,
    VerifyPhoneComponent,
    CreatePhoneComponent,
    SearchFilterPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MatDialogModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    NgxMaskModule.forRoot(options)
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [
    DeleteUserComponent, 
    DeletePhoneComponent,
    VerifyPhoneComponent,
    CreatePhoneComponent,
    EditPhoneComponent,
    EditUserComponent
  ]
})
export class AppModule { }
