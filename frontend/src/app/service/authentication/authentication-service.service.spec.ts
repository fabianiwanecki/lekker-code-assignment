import { TestBed } from '@angular/core/testing';

import {AuthenticationService, SignInReqDto, SignInResDto, SignUpReqDto, SignUpResDto} from './authentication.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {UserWithRankAndTeamDto} from "../user/user.service";
import {environment} from "../../../environments/environment.development";

describe('AuthenticationServiceService', () => {
  let service: AuthenticationService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(AuthenticationService);
    httpMock = TestBed.inject(HttpTestingController);

  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('isLoggedIn', () => {
    it('should return true when token and currentUser exist', () => {
      spyOn(service, 'getStoredToken').and.returnValue('token');
      spyOnProperty(service, 'currentUser', 'get').and.returnValue({ uuid: '1', username: 'TestUser' } as UserWithRankAndTeamDto);
      expect(service.isLoggedIn()).toBeTrue();
    });

    it('should return false and call logOut when token or currentUser is missing', () => {
      spyOn(service, 'getStoredToken').and.returnValue(null);
      spyOn(service, 'logOut');
      expect(service.isLoggedIn()).toBeFalse();
      expect(service.logOut).toHaveBeenCalled();
    });
  });

  describe('currentUser', () => {
    it('should get and set currentUser', () => {
      const user: UserWithRankAndTeamDto = { uuid: '1', username: 'TestUser', score: 100, rank: 1 };
      service.currentUser = user;
      expect(service.currentUser).toEqual(user);
    });
  });

  describe('teamOwner', () => {
    it('should get and set teamOwner', () => {
      service.teamOwner = 'owner1';
      expect(service.teamOwner).toBe('owner1');
    });
  });

  describe('signIn', () => {
    it('should send POST request to sign in', () => {
      const signInReq: SignInReqDto = { username: 'TestUser', password: 'password' };
      const mockResponse: SignInResDto = { token: 'token', userUuid: '1' };

      service.signIn(signInReq).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${environment.baseUrl}${environment.signInUrl}`);
      expect(req.request.method).toBe('POST');
      req.flush(mockResponse);
    });
  });

  describe('signUp', () => {
    it('should send POST request to sign up', () => {
      const signUpReq: SignUpReqDto = { username: 'TestUser', password: 'password' };
      const mockResponse: SignUpResDto = { uuid: '1', username: 'TestUser', score: 0 };

      service.signUp(signUpReq).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${environment.baseUrl}${environment.signUpUrl}`);
      expect(req.request.method).toBe('POST');
      req.flush(mockResponse);
    });
  });

  describe('localStorage operations', () => {
    it('should store and retrieve token', () => {
      service.storeToken('testToken');
      expect(service.getStoredToken()).toBe('testToken');
    });

    it('should store and retrieve key', () => {
      service.storeKey('testKey', 'testValue');
      expect(service.getStoredKey('testKey')).toBe('testValue');
    });

    it('should remove key', () => {
      service.storeKey('testKey', 'testValue');
      service.removeKey('testKey');
      expect(service.getStoredKey('testKey')).toBeNull();
    });
  });

  describe('logOut', () => {
    it('should remove all stored keys', () => {
      service.storeKey('owner', 'testOwner');
      service.storeKey('current-user', JSON.stringify({ uuid: '1' }));
      service.storeKey('token', 'testToken');

      service.logOut();

      expect(service.getStoredKey('owner')).toBeNull();
      expect(service.getStoredKey('current-user')).toBeNull();
      expect(service.getStoredKey('token')).toBeNull();
    });
  });
});
