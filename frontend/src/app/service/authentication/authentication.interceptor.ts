import { HttpInterceptorFn } from '@angular/common/http';
import {AuthenticationService} from "./authentication.service";
import {inject} from "@angular/core";
import {catchError, throwError} from "rxjs";
import {Router} from "@angular/router";

export const authenticationInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthenticationService);
  const router = inject(Router);

  const token = authService.getStoredToken();

  if (!token) {
    return next(req);
  }

  const clonedReq = req.clone({
    url: req.url,
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });
  return next(clonedReq).pipe(
    catchError((err) => {
      console.log(err);
      if (err.status === 403) {
        authService.logOut();
        router.navigate(['/'])
      }
      return throwError(() => err);
    }
  ));
};
