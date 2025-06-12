import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { take, switchMap, filter } from 'rxjs/operators';
import { AuthService } from '../features/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class GuestGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {

    // First check if user is authenticated (this will be fast with localStorage)
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/me']);
      return of(false);
    }

    // If not authenticated immediately, wait for user loading to complete
    // This handles the case where the app is initializing and checking with backend
    return this.authService.isUserLoaded$.pipe(
      filter(loaded => loaded === true),
      take(1),
      switchMap(() => {
        const isAuthenticated = this.authService.isAuthenticated();
        if (isAuthenticated) {
          this.router.navigate(['/me']);
          return of(false);
        }
        return of(true);
      })
    );
  }
}