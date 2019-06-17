import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, EMPTY, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { User } from '../models/user';

//FIXME
//const USERS_URI = './users'

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private usersUrl: string;

  constructor(private http: HttpClient) {
    //FIXME, don't use a url (full location). This wouldn't work except for localhost. use a uri on ./users
    this.usersUrl = 'http://localhost:4445/users';
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }

  //FIXME return types : Observable<User>
  //FIXME use ES6 template literal string to avoid string concatenation
  public find(userId:string) {
    //return this.http.get<User>(`${USERS_URI}/${userId}`);
    return this.http.get<User>(this.usersUrl + "/" + userId);
  }

  public save(user: User) {
    return this.http.post<User>(this.usersUrl, user, {observe:'response'});
  }

  public update(user: User) {
    return this.http.put<User>(this.usersUrl +  "/" + user.userId, user, {observe:'response'});
  }

  public delete(userId:string) {
    return this.http.delete(this.usersUrl + "/" + userId, {observe:'response'});
  }

}
