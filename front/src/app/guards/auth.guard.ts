import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { take, switchMap, filter } from 'rxjs/operators';
import { AuthService } from '../features/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {

    if (this.authService.isAuthenticated()) {
      return of(true);
    }

    return this.authService.isUserLoaded$.pipe(
      filter(loaded => loaded === true),
      take(1),
      switchMap(() => {
        const isAuthenticated = this.authService.isAuthenticated();
        if (isAuthenticated) {
          return of(true);
        }
        this.router.navigate(['/auth/login']);
        return of(false);
      })
    );
  }
}