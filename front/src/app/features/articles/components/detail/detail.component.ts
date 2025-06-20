import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { Post } from '../../interfaces/post.interface';

@Component({
  selector: 'app-article-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {
  post: Post | null = null;
  loading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id) {
      this.error = 'Article introuvable.';
      this.loading = false;
      return;
    }

    this.articleService.detail(id).subscribe({
      next: data => {
        this.post = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Erreur lors du chargement de l\'article.';
        this.loading = false;
      }
    });
  }

  back(): void {
    this.router.navigate(['/articles']);
  }
}