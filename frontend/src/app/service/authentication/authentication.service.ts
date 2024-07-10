import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {UserWithRankAndTeamDto} from "../user/user.service";

export interface SignInResDto {
  token: string;
  userUuid: string;
}

export interface SignUpResDto {
  uuid: string;
  username: string;
  score: number;
}

export interface SignUpReqDto {
  username: string;
  password: string;
}

export interface SignInReqDto {
  username: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private httpClient: HttpClient) { }

  isLoggedIn(): boolean {
    if (this.getStoredToken() && this.currentUser) {
      return true;
    }
    this.logOut();
    return false;
  }

  get currentUser(): UserWithRankAndTeamDto|undefined {
    return JSON.parse(this.getStoredKey('current-user'));
  }

  set teamOwner(value: string|undefined) {
    this.storeKey('owner', value);
  }

  get teamOwner(): string|undefined {
    return this.getStoredKey('owner');
  }

  set currentUser(value: UserWithRankAndTeamDto) {
    this.storeKey('current-user', JSON.stringify(value));
  }


  signIn(signInReq: SignInReqDto) : Observable<SignInResDto> {
    return this.httpClient.post<SignInResDto>(environment.baseUrl + environment.signInUrl, signInReq);
  }

  signUp(signUpReq: SignUpReqDto) : Observable<SignUpResDto> {
    return this.httpClient.post<SignUpResDto>(environment.baseUrl + environment.signUpUrl, signUpReq);
  }

  getStoredKey(key: string): any {
    return localStorage.getItem(key)
  }

  getStoredToken() {
    return this.getStoredKey("token")
  }

  storeToken(token: string) {
    this.storeKey('token', token);
  }

  storeKey(key: string, value: any) {
    localStorage.setItem(key, value);
  }

  removeKey(key: string) {
    localStorage.removeItem(key);
  }

  logOut() {
    this.removeKey('owner')
    this.removeKey('current-user')
    this.removeKey('token')
  }
}
