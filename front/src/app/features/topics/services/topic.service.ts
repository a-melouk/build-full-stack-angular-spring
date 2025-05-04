import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Topic } from '../interfaces/topic.interface';

interface ApiResponse {
  result: string;
  message: string;
  data: Topic[];
}

@Injectable({
  providedIn: 'root',
})
export class TopicService {
  private pathService = '/api/topics';

  constructor(private httpClient: HttpClient) { }

  public all(): Observable<Topic[]> {
    return this.httpClient.get<ApiResponse>(this.pathService).pipe(
      map(response => response.data)
    );
  }
}
