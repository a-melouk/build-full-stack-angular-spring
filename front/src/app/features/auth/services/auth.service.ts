import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
import { Router } from '@angular/router';

export interface LoginRequest {
  emailOrUsername: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  firstName: string;
  lastName: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  firstName: string;
  lastName: string;
}

export interface User {
  id: number;
  email: string;
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
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
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
    localStorage.removeItem('accessToken');
    localStorage.removeItem('tokenType');
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  private setSession(authResponse: AuthResponse): void {
    localStorage.setItem('accessToken', authResponse.token);
    localStorage.setItem('tokenType', 'Bearer');

    const user: User = {
      id: 0,
      email: authResponse.email,
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
    const token = localStorage.getItem('accessToken');

    if (token) {
      const tempUser: User = {
        id: 0,
        email: '',
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
    const token = localStorage.getItem('accessToken');
    return !!token;
  }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }
}