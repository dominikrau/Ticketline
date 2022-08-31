import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminShowUserComponent } from './admin-show-user.component';

describe('AdminShowUserComponent', () => {
  let component: AdminShowUserComponent;
  let fixture: ComponentFixture<AdminShowUserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminShowUserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminShowUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
