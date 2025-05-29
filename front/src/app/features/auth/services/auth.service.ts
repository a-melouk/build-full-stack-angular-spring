import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
import { Router } from '@angular/router';
import { CookieService } from './cookie.service';

export interface LoginRequest {
  emailOrUsername: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
}

export interface User {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:3001/api/auth';
  private readonly USER_API_URL = 'http://localhost:3001/api/me';
  private readonly ACCESS_TOKEN_NAME = 'accessToken';
  private readonly TOKEN_TYPE_NAME = 'tokenType';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private cookieService: CookieService
  ) {
    this.loadUserFromStorage();
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          this.setSession(response);
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, userData)
      .pipe(
        tap(response => {
          this.setSession(response);
        })
      );
  }

  logout(): void {
    this.cookieService.deleteCookie(this.ACCESS_TOKEN_NAME);
    this.cookieService.deleteCookie(this.TOKEN_TYPE_NAME);
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  private setSession(authResponse: AuthResponse): void {
    // Store tokens in secure cookies instead of localStorage
    this.cookieService.setSecureCookie(this.ACCESS_TOKEN_NAME, authResponse.token, 7);
    this.cookieService.setSecureCookie(this.TOKEN_TYPE_NAME, 'Bearer', 7);

    const user: User = {
      id: 0,
      email: authResponse.email,
      username: authResponse.username,
      firstName: authResponse.firstName,
      lastName: authResponse.lastName
    };
    this.currentUserSubject.next(user);

    // Fetch complete user data from backend
    this.getCurrentUser().subscribe({
      next: (fullUser) => {
        this.currentUserSubject.next(fullUser);
      },
      error: () => {
        // Keep the user from auth response if backend call fails
      }
    });
  }

  private loadUserFromStorage(): void {
    const token = this.cookieService.getCookie(this.ACCESS_TOKEN_NAME);

    if (token) {
      const tempUser: User = {
        id: 0,
        email: '',
        username: '',
        firstName: '',
        lastName: ''
      };
      this.currentUserSubject.next(tempUser);

      this.getCurrentUser().subscribe({
        next: (user) => {
          this.currentUserSubject.next(user);
        },
        error: (error) => {
          if (error.status === 401) {
            this.logout();
          }
        }
      });
    }
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(this.USER_API_URL);
  }

  refreshUserData(): Observable<User> {
    return this.getCurrentUser().pipe(
      tap(user => {
        this.currentUserSubject.next(user);
      }),
      catchError(error => {
        return of(null as any);
      })
    );
  }

  isAuthenticated(): boolean {
    const token = this.cookieService.getCookie(this.ACCESS_TOKEN_NAME);
    return !!token;
  }

  getToken(): string | null {
    return this.cookieService.getCookie(this.ACCESS_TOKEN_NAME);
  }
}