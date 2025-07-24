import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EmployeeMetrics } from '../models/employee-metrics.model';

@Injectable({
  providedIn: 'root',
})
export class EmployeeMetricsService {
  private baseUrl: string;

  constructor(private http: HttpClient) {
    const env = (window as any).__env || {};
    this.baseUrl =  env.employeePerformanceServiceEndpoint;
  }

  getMetricsByEmployeeId(employeeId: number): Observable<EmployeeMetrics[]> {
    return this.http.get<EmployeeMetrics[]>(
      `${this.baseUrl}/metrics/employee/${employeeId}`
    );
  }
}
