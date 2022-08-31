import {TestBed} from '@angular/core/testing';

import {ShowStateService} from './show-state.service';

describe('ShowCreatorService', () => {
  let service: ShowStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShowStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
