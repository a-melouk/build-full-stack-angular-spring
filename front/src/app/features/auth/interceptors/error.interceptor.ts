import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private router: Router) { }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';

        switch (error.status) {
          case 400:
            errorMessage = 'Bad Request - ' + (error.error?.message || 'Invalid request');
            break;
          case 401:
            errorMessage = 'Unauthorized - Please login again';
            this.handleAuthError();
            break;
          case 403:
            // Check if this is an authentication issue (invalid token) vs authorization issue
            const errorMsg = error.error?.message || '';
            if (errorMsg.toLowerCase().includes('token') || errorMsg.toLowerCase().includes('jwt') || errorMsg.toLowerCase().includes('unauthorized')) {
              errorMessage = 'Session expired - Please login again';
              this.handleAuthError();
            } else {
              errorMessage = 'Forbidden - You don\'t have permission to access this resource';
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

  private handleAuthError(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('tokenType');
    this.router.navigate(['/auth/login']);
  }
}