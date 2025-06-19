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
            errorMessage = 'Bad Request - ' + (error.error?.message || 'Invalid request');
            break;
          case 401:
          case 403:
            // Handle both authentication (401) and authorization (403) errors
            if (error.status === 401) {
              errorMessage = 'Unauthorized - Please login again';
            } else {
              errorMessage = 'Forbidden - You don\'t have permission to access this resource';
            }

            // Only logout if this is not a profile update request
            if (!request.url.includes('/profile')) {
              // Properly clear session and redirect
              this.authService.logout();
            } else {
              // For profile update errors, just redirect without clearing session
              this.router.navigate(['/auth/login']);
            }
            break;
          case 404:
            errorMessage = 'Not Found - The requested resource was not found';
            break;
          case 500:
            errorMessage = 'Internal Server Error - Please try again later';
            break;
          default:
            errorMessage = error.error?.message || 'An unexpected error occurred';
        }

        console.error('HTTP Error:', error.status, errorMessage);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}