import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ShowCreatorComponent} from './show-creator.component';

describe('ShowCreatorComponent', () => {
  let component: ShowCreatorComponent;
  let fixture: ComponentFixture<ShowCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ShowCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
