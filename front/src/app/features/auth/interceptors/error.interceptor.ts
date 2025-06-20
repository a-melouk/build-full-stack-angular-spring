import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';

        switch (error.status) {
          case 400:
            errorMessage = 'Requête invalide - ' + (error.error?.message || 'Données invalides');
            break;
          case 401:
          case 403:
            // Handle both authentication (401) and authorization (403) errors
            if (error.status === 401) {
              errorMessage = 'Non autorisé - Veuillez vous reconnecter';
            } else {
              errorMessage = 'Accès interdit - Vous n\'avez pas les permissions nécessaires';
            }

            // Don't logout during profile updates - user should stay logged in
            if (!request.url.includes('/profile') && !request.url.includes('/me')) {
              // Only logout for other authentication failures
              this.authService.logout();
            }
            // For profile update errors, don't redirect - let the component handle it
            break;
          case 404:
            errorMessage = 'Ressource non trouvée - L\'élément demandé n\'existe pas';
            break;
          case 500:
            errorMessage = 'Erreur serveur interne - Veuillez réessayer plus tard';
            break;
          default:
            errorMessage = error.error?.message || 'Une erreur inattendue s\'est produite';
        }

        console.error('HTTP Error:', error.status, errorMessage);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}