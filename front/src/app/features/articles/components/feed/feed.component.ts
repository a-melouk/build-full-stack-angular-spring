import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../services/article.service';
import { Post } from '../../interfaces/post.interface';
import { Router } from '@angular/router';
import { TopicService } from '../../../topics/services/topic.service';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss']
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  loading = true;
  error = '';
  sort: 'asc' | 'desc' = 'desc';

  constructor(
    private articleService: ArticleService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    this.loading = true;
    this.error = '';

    this.articleService.feed(this.sort).subscribe({
      next: data => {
        this.posts = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger le fil d\'actualité.';
        this.loading = false;
      }
    });
  }

  toggleSort(): void {
    this.sort = this.sort === 'desc' ? 'asc' : 'desc';
    this.loadFeed();
  }

  trackByPostId(_: number, post: Post): number {
    return post.id;
  }

  openPost(id: number): void {
    this.router.navigate(['/articles', id]);
  }
}