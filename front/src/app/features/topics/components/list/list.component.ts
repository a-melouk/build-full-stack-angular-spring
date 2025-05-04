import { Component, OnInit } from '@angular/core';
import { TopicService } from '../../services/topic.service';
import { Topic } from '../../interfaces/topic.interface';
import { Observable, EMPTY, catchError, tap } from 'rxjs';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {
  public topics$: Observable<Topic[]> = EMPTY;

  constructor(private topicService: TopicService) { }

  ngOnInit(): void {
    this.topics$ = this.topicService.all().pipe(
      tap(data => console.log('Topics received:', data)),
      catchError(error => {
        console.error('Error fetching topics:', error);
        return EMPTY;
      })
    );
  }

}
