import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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

  isEditing = false;
  profileForm: FormGroup;
  showPasswordFields = false;
  profileUpdateError = '';
  profileUpdateSuccess = false;

  constructor(
    private authService: AuthService,
    private subscriptionService: SubscriptionService,
    private formBuilder: FormBuilder
  ) {
    this.currentUser$ = this.authService.currentUser$;
    this.profileForm = this.createProfileForm();
  }

  ngOnInit(): void {
    this.currentUser$.subscribe(user => {
      if (user && (!user.id || user.id === 0 || !user.email)) {
        this.refreshUserData();
      }
      if (user) {
        this.updateFormWithUserData(user);
      }
    });
    this.loadUserSubscriptions();
  }

  private createProfileForm(): FormGroup {
    return this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      password: [''],
      passwordConfirmation: ['']
    });
  }

  private updateFormWithUserData(user: User): void {
    this.profileForm.patchValue({
      email: user.email,
      username: user.username,
      password: '',
      passwordConfirmation: ''
    });
  }

  onEditProfile(): void {
    this.isEditing = true;
    this.profileUpdateError = '';
    this.profileUpdateSuccess = false;
    this.showPasswordFields = false;
  }

  onCancelEdit(): void {
    this.isEditing = false;
    this.showPasswordFields = false;
    this.profileUpdateError = '';
    this.profileUpdateSuccess = false;

    this.currentUser$.subscribe(currentUser => {
      if (currentUser) {
        this.updateFormWithUserData(currentUser);
      }
    }).unsubscribe();
  }

  onTogglePasswordChange(): void {
    this.showPasswordFields = !this.showPasswordFields;
    if (!this.showPasswordFields) {
      this.profileForm.patchValue({
        password: '',
        passwordConfirmation: ''
      });
    }
  }

  onSaveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const formValue = this.profileForm.value;

    if (this.showPasswordFields) {
      if (formValue.password !== formValue.passwordConfirmation) {
        this.profileUpdateError = 'Passwords do not match';
        return;
      }
      if (formValue.password.length < 6) {
        this.profileUpdateError = 'Password must be at least 6 characters';
        return;
      }
    }

    const updateData = {
      email: formValue.email,
      username: formValue.username,
      ...(this.showPasswordFields && formValue.password ? {
        password: formValue.password,
        passwordConfirmation: formValue.passwordConfirmation
      } : {})
    };

    this.isLoading = true;
    this.profileUpdateError = '';

    this.authService.updateProfile(updateData).subscribe({
      next: () => {
        this.isLoading = false;
        this.isEditing = false;
        this.showPasswordFields = false;
        this.profileUpdateSuccess = true;
        this.profileForm.patchValue({
          password: '',
          passwordConfirmation: ''
        });
        setTimeout(() => {
          this.profileUpdateSuccess = false;
        }, 3000);
      },
      error: (error) => {
        this.isLoading = false;
        this.profileUpdateError = error.error?.message || 'Failed to update profile';
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