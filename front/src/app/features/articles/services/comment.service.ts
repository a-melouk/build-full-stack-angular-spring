import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../interfaces/comment.interface';
import { CreateCommentRequest } from '../interfaces/create-comment.request';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly basePath = '/api/comments';

  constructor(private http: HttpClient) { }

  /**
   * Get all comments for a specific post
   * @param postId The ID of the post
   */
  getCommentsByPostId(postId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.basePath}/post/${postId}`);
  }

  /**
   * Create a new comment
   * @param comment The comment data
   */
  createComment(comment: CreateCommentRequest): Observable<string> {
    return this.http.post<string>(this.basePath, comment);
  }
}