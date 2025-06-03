import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { SubscriptionService } from '../../services/subscription.service';
import { Topic } from '../../interfaces/topic.interface';
import { Observable, EMPTY, catchError, tap, of, forkJoin } from 'rxjs';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  public topics: Topic[] = [];
  public loading = true;
  public error = '';
  public subscriptionStatus: { [topicId: number]: boolean } = {};
  public subscribingTopics: { [topicId: number]: boolean } = {};

  constructor(
    private topicService: TopicService,
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
    this.loading = true;
    this.error = '';

    this.topicService.all().pipe(
      tap(data => {
        console.log('Topics received:', data);
        this.topics = data;
        this.loadSubscriptionStatuses();
      }),
      catchError(error => {
        console.error('Error fetching topics:', error);
        this.loading = false;
        this.error = 'Failed to load topics. Please try again.';

        // For auth errors (401/403), let the ErrorInterceptor handle the redirect
        // For other errors, show the error message
        if (error.status === 401 || error.status === 403) {
          // Don't return EMPTY, let the ErrorInterceptor handle this
          throw error;
        }

        return of([]); // Return empty array for non-auth errors
      })
    ).subscribe(); // Subscribe immediately!
  }

  private loadSubscriptionStatuses(): void {
    if (this.topics.length === 0) {
      this.loading = false;
      return;
    }

    const subscriptionChecks = this.topics.map(topic =>
      this.subscriptionService.checkSubscription(topic.id).pipe(
        tap(isSubscribed => {
          this.subscriptionStatus[topic.id] = isSubscribed;
        }),
        catchError(error => {
          console.error(`Error checking subscription for topic ${topic.id}:`, error);
          this.subscriptionStatus[topic.id] = false;
          return of(false);
        })
      )
    );

    forkJoin(subscriptionChecks).subscribe({
      next: () => {
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  public onSubscribe(topicId: number): void {
    this.subscribingTopics[topicId] = true;

    this.subscriptionService.subscribe(topicId).pipe(
      tap(message => {
        this.subscriptionStatus[topicId] = true;
        console.log(message);
      }),
      catchError(error => {
        console.error('Error subscribing to topic:', error);
        return of(null);
      })
    ).subscribe({
      complete: () => {
        this.subscribingTopics[topicId] = false;
      }
    });
  }

  public isSubscribed(topicId: number): boolean {
    return this.subscriptionStatus[topicId] || false;
  }

  public isSubscribing(topicId: number): boolean {
    return this.subscribingTopics[topicId] || false;
  }
}
