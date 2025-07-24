export interface Employee {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  jobTitle?: string;
  department?: string;
  hireDate?: string; // Will be converted to Date when needed
  address?: string;
  active?: boolean;
}
