import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { Phone } from '../models/phone';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PhoneService {

  private usersUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:4445/users';
  }

  public findAll(userId:string): Observable<Phone[]> {
    return this.http.get<Phone[]>(this.usersUrl + "/" + userId + "/phones");
  }

  public find(userId:string, phoneId:string) {
    return this.http.get<Phone>(this.usersUrl + "/" + userId + "/phones/" + phoneId);
  }

  public save(userId: string, phone: Phone) {
    return this.http.post<Phone>(this.usersUrl + "/" + userId + "/phones", phone, {observe:'response'});
  }

  public delete(userId:string, phoneId:string) {
    console.log(this.usersUrl + "/" + userId + "/phones/" + phoneId)
    return this.http.delete(this.usersUrl + "/" + userId + "/phones/" + phoneId);
  }

  public sendVerificationCode(userId:string, phoneId:string){
    return this.http.post<Phone>(this.usersUrl + "/" + userId + "/phones/" + phoneId + "/sendVerificationCode", null, {observe:'response'});
  }

  public submitVerificationCode(userId:string, phoneId:string, code:string){
    return this.http.post<Phone>(this.usersUrl + "/" + userId + "/phones/" + phoneId + "/submitVerificationCode/" + code, null, {observe:'response'});
  }
}
