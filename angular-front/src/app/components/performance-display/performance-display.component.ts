import { Component, Input, OnInit } from '@angular/core';
import { Performance } from '../../models/performance.model';
import { PerformanceService } from '../../services/performance.service';

@Component({
  selector: 'app-performance-display',
  templateUrl: './performance-display.component.html',
  styleUrls: ['./performance-display.component.scss'],
})
export class PerformanceDisplayComponent implements OnInit {
  @Input() employeeId!: number;

  performances: Performance[] = [];
  loading = true;
  error = '';

  constructor(private performanceService: PerformanceService) {}

  ngOnInit(): void {
    this.loadPerformances();
  }

  loadPerformances(): void {
    this.loading = true;
    this.performanceService
      .getPerformancesByEmployeeId(this.employeeId)
      .subscribe({
        next: (data) => {
          this.performances = data;
          this.loading = false;
        },
        error: (err) => {
          this.error =
            'Failed to load performance reviews. Please try again .';
          console.error('Error loading performance reviews:', err);
          this.loading = false;
        },
      });
  }
}
