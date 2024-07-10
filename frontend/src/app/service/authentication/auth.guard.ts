import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateChild,
  Route,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import {Injectable} from "@angular/core";
import {map, Observable, of} from "rxjs";
import {AuthenticationService} from "./authentication.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate, CanActivateChild {
  // bring in our Auth future State
  constructor(private authService: AuthenticationService, private _router: Router) {}
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.secure(route);
  }
  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.secure(route);
  }
  private secure(route: ActivatedRouteSnapshot | Route): Observable<boolean> {
    if (!this.authService.isLoggedIn()) {
      this._router.navigate(['/']);
      return of(false);
    }
    return of(true);
  }
}