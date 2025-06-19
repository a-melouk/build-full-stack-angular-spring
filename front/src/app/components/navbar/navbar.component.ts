import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { Observable } from 'rxjs';
import { filter } from 'rxjs/operators';
import { AuthService } from '../../features/auth/services/auth.service';
import { User } from '../../features/auth/interfaces/user.interafce';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  currentUser$: Observable<User | null>;
  mobileMenuOpen = false;
  currentRoute = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    // Track route changes to update active states
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event) => {
      this.currentRoute = (event as NavigationEnd).url;
    });

    // Set initial route
    this.currentRoute = this.router.url;
  }

  navigateToArticles(): void {
    this.router.navigate(['/articles']);
  }

  navigateToTopics(): void {
    this.router.navigate(['/topics']);
  }

  logout(): void {
    this.authService.logout();
  }

  private getScrollbarWidth(): number {
    return window.innerWidth - document.documentElement.clientWidth;
  }

  private preventBodyScroll(): void {
    const scrollbarWidth = this.getScrollbarWidth();
    document.body.style.overflow = 'hidden';
    document.body.style.paddingRight = `${scrollbarWidth}px`;
  }

  private restoreBodyScroll(): void {
    document.body.style.overflow = '';
    document.body.style.paddingRight = '';
  }

  toggleMobileMenu(): void {
    this.mobileMenuOpen = !this.mobileMenuOpen;

    if (this.mobileMenuOpen) {
      this.preventBodyScroll();
    } else {
      this.restoreBodyScroll();
    }
  }

  closeMobileMenu(): void {
    this.mobileMenuOpen = false;
    this.restoreBodyScroll();
  }

  isActiveRoute(route: string): boolean {
    return this.currentRoute.includes(`/${route}`);
  }
}
