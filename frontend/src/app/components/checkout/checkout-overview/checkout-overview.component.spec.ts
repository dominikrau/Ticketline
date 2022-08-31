import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CheckoutOverviewComponent} from './checkout-overview.component';

describe('CheckoutOverviewComponent', () => {
  let component: CheckoutOverviewComponent;
  let fixture: ComponentFixture<CheckoutOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckoutOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckoutOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
