export interface Comment {
  id: number;
  content: string;
  createdAt: string; // ISO date string from backend
  username: string;
}