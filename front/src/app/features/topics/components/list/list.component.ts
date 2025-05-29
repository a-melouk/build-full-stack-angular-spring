import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { Topic } from '../../interfaces/topic.interface';
import { Observable, EMPTY, catchError, tap, of } from 'rxjs';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  public topics: Topic[] = [];
  public loading = true;
  public error = '';

  constructor(private topicService: TopicService) { }

  ngOnInit(): void {
    this.loading = true;
    this.error = '';

    this.topicService.all().pipe(
      tap(data => {
        console.log('Topics received:', data);
        this.topics = data;
        this.loading = false;
        this.error = '';
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
}
