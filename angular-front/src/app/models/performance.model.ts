export interface Performance {
    id?: number;
    employeeId: number;
    reviewDate: string; // Will be converted to Date when needed
    rating: number;
    reviewerName?: string;
    comments?: string;
    goals?: string;
    strengths?: string;
    areasForImprovement?: string;
  }