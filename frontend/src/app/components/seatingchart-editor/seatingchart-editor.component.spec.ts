import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {SeatingchartEditorComponent} from './seatingchart-editor.component';

describe('SeatingchartEditorComponent', () => {
  let component: SeatingchartEditorComponent;
  let fixture: ComponentFixture<SeatingchartEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SeatingchartEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SeatingchartEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
