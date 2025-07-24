import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceDisplayComponent } from './performance-display.component';

describe('PerformanceDisplayComponent', () => {
  let component: PerformanceDisplayComponent;
  let fixture: ComponentFixture<PerformanceDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PerformanceDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PerformanceDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
