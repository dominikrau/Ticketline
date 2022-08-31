import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SeatingChartViewerComponent} from './seating-chart-viewer.component';

describe('SeatingChartViewerComponent', () => {
  let component: SeatingChartViewerComponent;
  let fixture: ComponentFixture<SeatingChartViewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeatingChartViewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatingChartViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
