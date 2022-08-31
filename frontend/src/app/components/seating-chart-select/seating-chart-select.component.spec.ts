import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SeatingChartSelectComponent} from './seating-chart-select.component';

describe('SeatingChartSelectComponent', () => {
  let component: SeatingChartSelectComponent;
  let fixture: ComponentFixture<SeatingChartSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeatingChartSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatingChartSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
