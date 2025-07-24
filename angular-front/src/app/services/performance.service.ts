import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Performance } from '../models/performance.model';

@Injectable({
  providedIn: 'root',
})
export class PerformanceService {
  private baseUrl: string;

  constructor(private http: HttpClient) {
    const env = (window as any).__env || {};
    this.baseUrl = env.employeePerformanceServiceEndpoint;
  }

  getPerformancesByEmployeeId(employeeId: number): Observable<Performance[]> {
    return this.http.get<Performance[]>(
      `${this.baseUrl}/performances/employee/${employeeId}`
    );
  }
}
