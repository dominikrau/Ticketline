import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CheckoutSeatingChartComponent} from './checkout-seating-chart.component';

describe('CheckoutComponent', () => {
  let component: CheckoutSeatingChartComponent;
  let fixture: ComponentFixture<CheckoutSeatingChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckoutSeatingChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutSeatingChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
