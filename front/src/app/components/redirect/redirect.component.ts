import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';

@Component({
  selector: 'app-redirect',
  template: '<div>Redirecting...</div>'
})
export class RedirectComponent implements OnInit {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/topics']);
    } else {
      this.router.navigate(['/auth/login']);
    }
  }
}