import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'Employee Portal';
  currentYear: number = new Date().getFullYear();

  ngOnInit(): void {
    // Any initialization logic
  }
}
