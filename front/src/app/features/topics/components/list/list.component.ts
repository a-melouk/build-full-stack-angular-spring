import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { SubscriptionService } from '../../services/subscription.service';
import { Topic } from '../../interfaces/topic.interface';
import { Observable, EMPTY, catchError, tap, of, forkJoin, switchMap } from 'rxjs';

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
      tap(topics => {
        this.topics = topics;
      }),
      switchMap(topics => {
        if (topics.length === 0) {
          this.loading = false;
          return of(null);
        }

        let completedChecks = 0;
        const totalTopics = topics.length;

        for (const topic of topics) {
          this.subscriptionService.checkSubscription(topic.id).pipe(
            tap(isSubscribed => {
              this.subscriptionStatus[topic.id] = isSubscribed;
              completedChecks++;

              // Check if all topics have been processed
              if (completedChecks === totalTopics) {
                this.loading = false;
              }
            }),
            catchError(error => {
              this.subscriptionStatus[topic.id] = false;
              completedChecks++;

              if (completedChecks === totalTopics) {
                this.loading = false;
              }

              return of(false);
            })
          ).subscribe();
        }

        return of(null);
      }),
      catchError(error => {
        this.loading = false;
        this.error = 'Échec du chargement des thèmes. Veuillez réessayer.';

        if (error.status === 401 || error.status === 403) {
          throw error;
        }

        return of([]);
      })
    ).subscribe();
  }

  public onSubscribe(topicId: number): void {
    if (this.isSubscribed(topicId)) {
      return;
    }

    this.subscribingTopics[topicId] = true;

    this.subscriptionService.subscribe(topicId).pipe(
      tap(message => {
        this.subscriptionStatus[topicId] = true;
      }),
      catchError(error => {
        return of(null);
      })
    ).subscribe({
      next: () => {
      },
      error: (error) => {
        this.subscribingTopics[topicId] = false;
      },
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
