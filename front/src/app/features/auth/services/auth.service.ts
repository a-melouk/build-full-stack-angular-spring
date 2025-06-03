import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../interfaces/user.interafce';
import { AuthResponse } from '../interfaces/response/authresponse.interface.ts';
import { LoginRequest } from '../interfaces/request/loginrequest.interface';
import { RegisterRequest } from '../interfaces/request/registerrequest.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'api/auth';
  private readonly USER_API_URL = 'api/me';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private isUserLoadedSubject = new BehaviorSubject<boolean>(false);
  public isUserLoaded$ = this.isUserLoadedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadUserFromStorage();
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials, { withCredentials: true })
      .pipe(
        tap(response => {
          this.setSession(response);
        })
      );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, userData, { withCredentials: true })
      .pipe(
        tap(response => {
          this.setSession(response);
        })
      );
  }

  logout(): void {
    // Call backend logout endpoint to clear cookies
    this.http.post(`${this.API_URL}/logout`, {}, { withCredentials: true }).subscribe({
      next: () => {
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        // Even if backend call fails, clear local state
        this.currentUserSubject.next(null);
        this.router.navigate(['/auth/login']);
      }
    });
  }

  private setSession(authResponse: AuthResponse): void {
    // Since the backend sets HTTP-only cookies, we only need to handle user data
    const user: User = {
      id: 0,
      email: authResponse.email,
      username: authResponse.username
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
    // Try to get current user data from backend
    // If HTTP-only cookies exist, this will succeed
    this.isUserLoadedSubject.next(false);
    this.getCurrentUser().subscribe({
      next: (user) => {
        this.currentUserSubject.next(user);
        this.isUserLoadedSubject.next(true);
      },
      error: (error) => {
        if (error.status === 401) {
          this.currentUserSubject.next(null);
        }
        this.isUserLoadedSubject.next(true);
      }
    });
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(this.USER_API_URL, { withCredentials: true });
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
    const user = this.currentUserSubject.value;
    const authenticated = user !== null;
    return authenticated;
  }
}