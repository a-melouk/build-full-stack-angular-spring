import { Comment } from './comment.interface';

export interface Post {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  username: string;
  topicId: number;
  topicName: string;
  comments?: Comment[];
}