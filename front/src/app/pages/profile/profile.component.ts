import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { AuthService } from '../../features/auth/services/auth.service';
import { SubscriptionService, SubscriptionDto } from '../../features/topics/services/subscription.service';
import { Observable, of } from 'rxjs';
import { catchError, tap, take } from 'rxjs/operators';
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
    this.currentUser$.pipe(take(1)).subscribe(user => {
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

  passwordValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumeric = /[0-9]/.test(value);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);

    const passwordValid = hasUpperCase && hasLowerCase && hasNumeric && hasSpecialChar;
    return !passwordValid ? { invalidPassword: true } : null;
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

    this.currentUser$.pipe(take(1)).subscribe(currentUser => {
      if (currentUser) {
        this.updateFormWithUserData(currentUser);
      }
    });
  }

  onTogglePasswordChange(): void {
    this.showPasswordFields = !this.showPasswordFields;
    if (this.showPasswordFields) {
      // Add password validation when showing password fields
      this.profileForm.get('password')?.setValidators([
        Validators.required,
        Validators.minLength(8),
        this.passwordValidator
      ]);
      this.profileForm.get('passwordConfirmation')?.setValidators([Validators.required]);
    } else {
      // Remove password validation when hiding password fields
      this.profileForm.get('password')?.clearValidators();
      this.profileForm.get('passwordConfirmation')?.clearValidators();
      this.profileForm.patchValue({
        password: '',
        passwordConfirmation: ''
      });
    }
    this.profileForm.get('password')?.updateValueAndValidity();
    this.profileForm.get('passwordConfirmation')?.updateValueAndValidity();
  }

  onSaveProfile(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const formValue = this.profileForm.value;

    if (this.showPasswordFields) {
      if (formValue.password !== formValue.passwordConfirmation) {
        this.profileUpdateError = 'Les mots de passe ne correspondent pas';
        return;
      }
    }

    // Build update data with only changed fields
    const updateData: any = {};

    // Get current user data to compare changes
    this.currentUser$.pipe(take(1)).subscribe(currentUser => {
      if (currentUser) {
        // Only include email if it has changed
        if (formValue.email !== currentUser.email) {
          updateData.email = formValue.email;
        }

        // Only include username if it has changed
        if (formValue.username !== currentUser.username) {
          updateData.username = formValue.username;
        }

        // Only include password if it's being updated
        if (this.showPasswordFields && formValue.password) {
          updateData.password = formValue.password;
          updateData.passwordConfirmation = formValue.passwordConfirmation;
        }

        // Check if there are any changes to send
        if (Object.keys(updateData).length === 0) {
          this.profileUpdateError = 'Aucune modification détectée';
          return;
        }

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
            this.profileUpdateError = error.error?.message || 'Échec de la mise à jour du profil';
          }
        });
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
        this.subscriptionsError = 'Échec du chargement des abonnements. Veuillez réessayer.';
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