import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../services/comment.service';
import { Post } from '../../interfaces/post.interface';
import { Comment } from '../../interfaces/comment.interface';
import { CreateCommentRequest } from '../../interfaces/create-comment.request';

@Component({
  selector: 'app-article-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {
  post: Post | null = null;
  loading = true;
  error = '';

  // Comments
  comments: Comment[] = [];
  commentsLoading = false;
  commentsError = '';

  // Comment form
  commentForm: FormGroup;
  isSubmittingComment = false;
  commentSubmitSuccess = false;

  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private commentService: CommentService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.commentForm = this.createCommentForm();
  }

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
        this.loadComments(id);
      },
      error: () => {
        this.error = 'Erreur lors du chargement de l\'article.';
        this.loading = false;
      }
    });
  }

  private createCommentForm(): FormGroup {
    return this.formBuilder.group({
      content: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(1000)]]
    });
  }

  private loadComments(postId: number): void {
    this.commentsLoading = true;
    this.commentsError = '';

    this.commentService.getCommentsByPostId(postId).subscribe({
      next: comments => {
        this.comments = comments;
        this.commentsLoading = false;
      },
      error: () => {
        this.commentsError = 'Erreur lors du chargement des commentaires.';
        this.commentsLoading = false;
      }
    });
  }

  onSubmitComment(): void {
    if (this.commentForm.invalid || !this.post) {
      this.commentForm.markAllAsTouched();
      return;
    }

    const commentData: CreateCommentRequest = {
      content: this.commentForm.value.content.trim(),
      postId: this.post.id
    };

    this.isSubmittingComment = true;
    this.commentSubmitSuccess = false;

    this.commentService.createComment(commentData).subscribe({
      next: () => {
        this.isSubmittingComment = false;
        this.commentSubmitSuccess = true;
        this.commentForm.reset();

        // Reload comments to show the new one
        if (this.post) {
          this.loadComments(this.post.id);
        }

        // Hide success message after 3 seconds
        setTimeout(() => {
          this.commentSubmitSuccess = false;
        }, 3000);
      },
      error: () => {
        this.isSubmittingComment = false;
        this.commentsError = 'Erreur lors de l\'ajout du commentaire.';
      }
    });
  }

  trackByCommentId(index: number, comment: Comment): number {
    return comment.id;
  }

  back(): void {
    this.router.navigate(['/articles']);
  }
}