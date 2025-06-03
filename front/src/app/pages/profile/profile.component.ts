import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../features/auth/services/auth.service';
import { SubscriptionService, SubscriptionDto } from '../../features/topics/services/subscription.service';
import { Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { User } from '../../features/auth/interfaces/user.interafce';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  currentUser$: Observable<User | null>;
  isLoading = false;
  subscriptions: SubscriptionDto[] = [];
  subscriptionsLoading = false;
  subscriptionsError = '';
  unsubscribingTopics: { [topicId: number]: boolean } = {};

  constructor(
    private authService: AuthService,
    private subscriptionService: SubscriptionService
  ) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    this.currentUser$.subscribe(user => {
      if (user && (!user.id || user.id === 0 || !user.email)) {
        this.refreshUserData();
      }
    });
    this.loadUserSubscriptions();
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

  loadUserSubscriptions(): void {
    this.subscriptionsLoading = true;
    this.subscriptionsError = '';

    this.subscriptionService.getUserSubscriptions().pipe(
      tap(subscriptions => {
        this.subscriptions = subscriptions;
        this.subscriptionsLoading = false;
      }),
      catchError(error => {
        console.error('Error loading user subscriptions:', error);
        this.subscriptionsError = 'Failed to load subscriptions. Please try again.';
        this.subscriptionsLoading = false;
        return of([]);
      })
    ).subscribe();
  }

  onUnsubscribe(topicId: number): void {
    this.unsubscribingTopics[topicId] = true;

    this.subscriptionService.unsubscribe(topicId).pipe(
      tap(message => {
        // Remove the subscription from the list
        this.subscriptions = this.subscriptions.filter(sub => sub.topicId !== topicId);
        console.log(message);
      }),
      catchError(error => {
        console.error('Error unsubscribing from topic:', error);
        return of(null);
      })
    ).subscribe({
      complete: () => {
        this.unsubscribingTopics[topicId] = false;
      }
    });
  }

  isUnsubscribing(topicId: number): boolean {
    return this.unsubscribingTopics[topicId] || false;
  }

  logout(): void {
    this.authService.logout();
  }
}