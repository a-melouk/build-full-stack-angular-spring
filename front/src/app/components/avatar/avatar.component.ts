import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrls: ['./avatar.component.scss']
})
export class AvatarComponent {
  @Input() username: string = '';
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() clickable: boolean = false;

  constructor(private router: Router) { }

  getUserInitial(): string {
    return this.username ? this.username.charAt(0).toUpperCase() : 'U';
  }

  onClick(): void {
    if (this.clickable) {
      this.router.navigate(['/me']);
    }
  }
}