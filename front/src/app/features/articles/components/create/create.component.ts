import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ArticleService } from '../../services/article.service';
import { TopicService } from '../../../topics/services/topic.service';
import { Topic } from '../../../topics/interfaces/topic.interface';

@Component({
  selector: 'app-create-article',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss']
})
export class CreateComponent implements OnInit {
  topics: Topic[] = [];
  loading = false;
  error = '';

  form = this.fb.group({
    topicId: [null, Validators.required],
    title: ['', [Validators.required, Validators.maxLength(100)]],
    content: ['', Validators.required]
  });

  constructor(
    private fb: FormBuilder,
    private topicService: TopicService,
    private articleService: ArticleService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.topicService.all().subscribe({
      next: data => this.topics = data,
      error: () => this.error = 'Erreur lors du chargement des thèmes.'
    });
  }

  submit(): void {
    if (this.form.invalid) return;

    this.loading = true;
    this.articleService.create(this.form.value as any).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/articles']);
      },
      error: () => {
        this.error = 'Erreur lors de la création de l\'article.';
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/articles']);
  }
}