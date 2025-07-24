export interface EmployeeMetrics {
  id?: number;
  employeeId: number;
  metricDate: string; // Will be converted to Date when needed
  tasksCompleted?: number;
  projectsCompleted?: number;
  avgTaskCompletionTime?: number;
  clientSatisfactionScore?: number;
  teamCollaborationScore?: number;
  innovationScore?: number;
  metricNotes?: string;
}