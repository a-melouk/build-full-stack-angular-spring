import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CookieService {

  constructor() { }

  /**
   * Set a secure cookie with JWT token
   */
  setSecureCookie(name: string, value: string, days: number = 7): void {
    const expires = new Date();
    expires.setTime(expires.getTime() + (days * 24 * 60 * 60 * 1000));

    // Create secure cookie with httpOnly equivalent properties
    // Note: httpOnly cannot be set from client-side JavaScript for security
    // This should ideally be set from the server
    const cookieString = `${name}=${value}; expires=${expires.toUTCString()}; path=/; SameSite=Strict; Secure`;
    document.cookie = cookieString;
  }

  /**
   * Get cookie value by name
   */
  getCookie(name: string): string | null {
    const nameEQ = name + "=";
    const ca = document.cookie.split(';');

    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) === ' ') {
        c = c.substring(1, c.length);
      }
      if (c.indexOf(nameEQ) === 0) {
        return c.substring(nameEQ.length, c.length);
      }
    }
    return null;
  }

  /**
   * Delete cookie by name
   */
  deleteCookie(name: string): void {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; SameSite=Strict; Secure`;
  }

  /**
   * Check if cookie exists
   */
  hasCookie(name: string): boolean {
    return this.getCookie(name) !== null;
  }
}