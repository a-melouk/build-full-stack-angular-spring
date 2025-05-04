import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './components/list/list.component';
import { TopicsRoutingModule } from './topics-routing.module';

@NgModule({
  declarations: [
    ListComponent
  ],
  imports: [
    CommonModule,
    TopicsRoutingModule
  ]
})
export class TopicsModule { }