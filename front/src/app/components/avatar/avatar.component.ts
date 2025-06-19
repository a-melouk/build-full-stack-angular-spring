import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-avatar',
  templateUrl: './avatar.component.html',
  styleUrls: ['./avatar.component.scss']
})
export class AvatarComponent {
  @Input() username: string = '';
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() clickable: boolean = false;

  getUserInitial(): string {
    return this.username ? this.username.charAt(0).toUpperCase() : 'U';
  }
}