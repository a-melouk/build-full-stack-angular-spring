import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface SubscriptionDto {
  id: number;
  userId: number;
  topicId: number;
  topicName: string;
  topicDescription: string;
  subscribedAt: string;
}

@Injectable({
  providedIn: 'root',
})
export class SubscriptionService {
  private pathService = '/api/subscriptions';

  constructor(private httpClient: HttpClient) { }

  public subscribe(topicId: number): Observable<string> {
    return this.httpClient.post<string>(`${this.pathService}/subscribe/${topicId}`, {});
  }

  public unsubscribe(topicId: number): Observable<string> {
    return this.httpClient.delete<string>(`${this.pathService}/unsubscribe/${topicId}`);
  }

  public getUserSubscriptions(): Observable<SubscriptionDto[]> {
    return this.httpClient.get<SubscriptionDto[]>(`${this.pathService}/user`);
  }

  public checkSubscription(topicId: number): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.pathService}/check/${topicId}`);
  }
}