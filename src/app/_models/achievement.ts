export interface Achievement {
  id: String;
  threshold: Number;
  isCumulative: boolean;
  taskIdRef: String;
  expression: Number;
  dummyData: Number;
  name: String;
  description: String;
}
