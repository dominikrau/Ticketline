import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ArtistSelectComponent} from './artist-select.component';

describe('ArtistSelectComponent', () => {
  let component: ArtistSelectComponent;
  let fixture: ComponentFixture<ArtistSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArtistSelectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistSelectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
