export interface Achievement {
  id: String;
  threshold: number;
  cumulative: boolean;
  taskIdRef: String;
  expression: number;
  dummyData: number;
  name: String;
  description: String;
}
