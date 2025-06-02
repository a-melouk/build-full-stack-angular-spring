import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../features/auth/services/auth.service';
import { Observable } from 'rxjs';
import { User } from '../../features/auth/interfaces/user.interafce';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  currentUser$: Observable<User | null>;
  isLoading = false;

  constructor(private authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    this.currentUser$.subscribe(user => {
      if (user && (!user.id || user.id === 0 || !user.email)) {
        this.refreshUserData();
      }
    });
  }

  refreshUserData(): void {
    this.isLoading = true;
    this.authService.refreshUserData().subscribe({
      next: () => {
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}