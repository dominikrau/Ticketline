import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {VenueSelectComponent} from './venue-select.component';

describe('VenueSelectComponent', () => {
  let component: VenueSelectComponent;
  let fixture: ComponentFixture<VenueSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VenueSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VenueSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
