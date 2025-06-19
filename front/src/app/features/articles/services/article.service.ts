import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../interfaces/post.interface';
import { CreatePostRequest } from '../interfaces/create-post.request';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  private readonly basePath = '/api/posts';

  constructor(private http: HttpClient) { }

  /**
   * Get user feed
   * @param sort 'asc' or 'desc', defaults to 'desc'
   */
  feed(sort: 'asc' | 'desc' = 'desc'): Observable<Post[]> {
    const params = new HttpParams().set('sort', sort);
    return this.http.get<Post[]>(this.basePath, { params });
  }

  /** Get a single post by id */
  detail(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.basePath}/${id}`);
  }

  /** Create a new post */
  create(payload: CreatePostRequest): Observable<Post> {
    return this.http.post<Post>(this.basePath, payload);
  }
}