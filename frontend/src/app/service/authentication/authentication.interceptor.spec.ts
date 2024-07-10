import { TestBed } from '@angular/core/testing';
import {HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';

import { authenticationInterceptor } from './authentication.interceptor';
import {AuthenticationService} from "./authentication.service";
import {Router} from "@angular/router";
import {of, throwError} from "rxjs";

describe('authenticationInterceptor', () => {

  let authService: jasmine.SpyObj<AuthenticationService>;
  let router: jasmine.SpyObj<Router>;

  const interceptor: HttpInterceptorFn = (req, next) =>
    TestBed.runInInjectionContext(() => authenticationInterceptor(req, next));

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthenticationService', ['getStoredToken', 'logOut']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthenticationService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    authService = TestBed.inject(AuthenticationService) as jasmine.SpyObj<AuthenticationService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should not modify the request if no token is present', (done) => {
    authService.getStoredToken.and.returnValue(null);

    const req = new HttpRequest('GET', '/api/test');
    const next: HttpHandlerFn = jasmine.createSpy('next').and.returnValue(of({}));

    interceptor(req, next).subscribe(() => {
      expect(next).toHaveBeenCalledWith(req);
      done();
    });
  });

  it('should add Authorization header if token is present', (done) => {
    const token = 'test-token';
    authService.getStoredToken.and.returnValue(token);

    const req = new HttpRequest('GET', '/api/test');
    const next: HttpHandlerFn = jasmine.createSpy('next').and.returnValue(of({}));

    interceptor(req, next).subscribe(() => {
      const modifiedReq = (next as jasmine.Spy).calls.first().args[0] as HttpRequest<any>;
      expect(modifiedReq.headers.get('Authorization')).toBe(`Bearer ${token}`);
      done();
    });
  });

  it('should handle 403 error and log out user', (done) => {
    authService.getStoredToken.and.returnValue('test-token');

    const req = new HttpRequest('GET', '/api/test');
    const error = new HttpErrorResponse({ status: 403 });
    const next: HttpHandlerFn = jasmine.createSpy('next').and.returnValue(throwError(() => error));

    interceptor(req, next).subscribe({
      error: (err) => {
        expect(err).toBe(error);
        expect(authService.logOut).toHaveBeenCalled();
        expect(router.navigate).toHaveBeenCalledWith(['/']);
        done();
      }
    });
  });

  it('should pass through other errors', (done) => {
    authService.getStoredToken.and.returnValue('test-token');

    const req = new HttpRequest('GET', '/api/test');
    const error = new HttpErrorResponse({ status: 500 });
    const next: HttpHandlerFn = jasmine.createSpy('next').and.returnValue(throwError(() => error));

    interceptor(req, next).subscribe({
      error: (err) => {
        expect(err).toBe(error);
        expect(authService.logOut).not.toHaveBeenCalled();
        expect(router.navigate).not.toHaveBeenCalled();
        done();
      }
    });
  });
});
