import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, EMPTY, throwError } from 'rxjs';
import { catchError, debounceTime } from 'rxjs/operators';
import { User } from '../models/user';

const USERS_URI = './api/v1/users'

@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http: HttpClient) {
  }

  public findAll(): Observable<User[]> {
    return this.http.get<User[]>(USERS_URI);
  }

  public findAllBySearch(search:string): Observable<User[]>  {
    return this.http.get<User[]>(`${USERS_URI}?search=${search}`)
    .pipe(
      debounceTime(300)
    );
  }

  public find(userId:string): Observable<User>  {
    return this.http.get<User>(`${USERS_URI}/${userId}`);
  }

  public save(user: User): Observable<User>  {
    return this.http.post<User>(USERS_URI, user);
  }

  public update(user: User): Observable<User>  {
    return this.http.put<User>(`${USERS_URI}/${user.userId}`, user);
  }

  public delete(userId:string): Observable<any>  {
    return this.http.delete(`${USERS_URI}/${userId}`);
  }

}
