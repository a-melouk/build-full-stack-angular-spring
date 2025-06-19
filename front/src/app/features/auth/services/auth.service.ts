import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, catchError, of } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../interfaces/user.interafce';
import { AuthResponse } from '../interfaces/response/authresponse.interface.ts';
import { LoginRequest } from '../interfaces/request/loginrequest.interface';
import { RegisterRequest } from '../interfaces/request/registerrequest.interface';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'api/auth';
  private readonly USER_API_URL = 'api/me';
  private readonly USER_STORAGE_KEY = 'currentUser';

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
        this.clearSession();
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        // Even if backend call fails, clear local state
        this.clearSession();
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

    // Store user data in localStorage and update BehaviorSubject
    this.setUserData(user);

    // Fetch complete user data from backend
    this.getCurrentUser().subscribe({
      next: (fullUser) => {
        this.setUserData(fullUser);
      },
      error: () => {
        // Keep the user from auth response if backend call fails
      }
    });
  }

  private loadUserFromStorage(): void {
    this.isUserLoadedSubject.next(false);

    // Only load from localStorage, don't make API calls immediately
    const storedUser = this.getUserFromStorage();
    if (storedUser) {
      this.currentUserSubject.next(storedUser);
    }

    this.isUserLoadedSubject.next(true);
  }

  private setUserData(user: User): void {
    // Update both localStorage and BehaviorSubject
    localStorage.setItem(this.USER_STORAGE_KEY, JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  private getUserFromStorage(): User | null {
    try {
      const storedUser = localStorage.getItem(this.USER_STORAGE_KEY);
      return storedUser ? JSON.parse(storedUser) : null;
    } catch (error) {
      console.error('Error parsing user data from localStorage:', error);
      localStorage.removeItem(this.USER_STORAGE_KEY);
      return null;
    }
  }

  private clearSession(): void {
    localStorage.removeItem(this.USER_STORAGE_KEY);
    this.currentUserSubject.next(null);
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/me`, { withCredentials: true });
  }

  refreshUserData(): Observable<User> {
    return this.getCurrentUser().pipe(
      tap(user => {
        this.setUserData(user);
      }),
      catchError(error => {
        return of(null as any);
      })
    );
  }

  updateProfile(userData: any): Observable<AuthResponse> {
    return this.http.put<AuthResponse>(`${this.API_URL}/profile`, userData, { withCredentials: true })
      .pipe(
        tap(response => {
          // Update local user data with response
          const updatedUser: User = {
            id: this.currentUserSubject.value?.id || 0,
            email: response.email,
            username: response.username
          };
          this.setUserData(updatedUser);

          // Force refresh user data from backend to ensure consistency
          this.refreshUserData().subscribe({
            next: (freshUserData) => {
              // User data refreshed successfully
              console.log('User data refreshed after profile update');
            },
            error: (error) => {
              console.warn('Unable to refresh user data:', error);
              // Don't logout on refresh error, keep the updated data we have
            }
          });
        }),
        catchError(error => {
          // Don't logout on profile update errors - let the component handle it
          console.error('Error updating profile:', error);
          throw error;
        })
      );
  }

  isAuthenticated(): boolean {
    const user = this.currentUserSubject.value;
    const authenticated = user !== null;

    if (!environment.production) {
      console.log('user', user);
      console.log('authenticated', authenticated);
    }

    return authenticated;
  }
}