import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EventTypeSelectComponent} from './event-type-select.component';

describe('EventTypeSelectComponent', () => {
  let component: EventTypeSelectComponent;
  let fixture: ComponentFixture<EventTypeSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventTypeSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventTypeSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
