import { Component, Input, OnInit } from '@angular/core';
import { EmployeeMetrics } from '../../models/employee-metrics.model';
import { EmployeeMetricsService } from '../../services/employee-metrics.service';

@Component({
  selector: 'app-metrics-display',
  templateUrl: './metrics-display.component.html',
  styleUrls: ['./metrics-display.component.scss'],
})
export class MetricsDisplayComponent implements OnInit {
  @Input() employeeId!: number;

  metrics: EmployeeMetrics[] = [];
  loading = true;
  error = '';

  constructor(private metricsService: EmployeeMetricsService) {}

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.loading = true;
    this.metricsService.getMetricsByEmployeeId(this.employeeId).subscribe({
      next: (data) => {
        this.metrics = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load metrics. Please try again .';
        console.error('Error loading metrics:', err);
        this.loading = false;
      },
    });
  }
}
