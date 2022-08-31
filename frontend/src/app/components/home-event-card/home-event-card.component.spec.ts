import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {HomeEventCardComponent} from './home-event-card.component';

describe('HomeEventCardComponent', () => {
  let component: HomeEventCardComponent;
  let fixture: ComponentFixture<HomeEventCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HomeEventCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeEventCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
