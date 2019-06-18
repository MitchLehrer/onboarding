import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { Phone } from '../models/phone';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

const USERS_URI = 'http://localhost:4445/users';

@Injectable({
  providedIn: 'root'
})

export class PhoneService {

  constructor(private http: HttpClient) {
  }

  public findAll(userId:string): Observable<Phone[]> {
    return this.http.get<Phone[]>(`${USERS_URI}/${userId}/phones`);
  }

  public find(userId:string, phoneId:string): Observable<Phone> {
    return this.http.get<Phone>(`${USERS_URI}/${userId}/phones/${phoneId}`);
  }

  public save(userId: string, phone: Phone): Observable<Phone> {
    return this.http.post<Phone>(`${USERS_URI}/${userId}/phones`, phone);
  }

  public update(userId: string, phone: Phone): Observable<Phone> {
    return this.http.put<Phone>(`${USERS_URI}/${userId}/phones/${phone.phoneId}`, phone);
  }

  public delete(userId:string, phoneId:string) {
    return this.http.delete(`${USERS_URI}/${userId}/phones/${phoneId}`);
  }
  public setPrimary(userId:string, phoneId:string): Observable<Phone>{
    return this.http.post<Phone>(`${USERS_URI}/${userId}/phones/${phoneId}/setPrimary`, null);
  }

  public sendVerificationCode(userId:string, phoneId:string): Observable<Phone>{
    return this.http.post<Phone>(`${USERS_URI}/${userId}/phones/${phoneId}/sendVerificationCode`, null);
  }

  public submitVerificationCode(userId:string, phoneId:string, code:string): Observable<Phone>{
    return this.http.post<Phone>(`${USERS_URI}/${userId}/phones/${phoneId}/submitVerificationCode/${code}`, null);
  }
}
