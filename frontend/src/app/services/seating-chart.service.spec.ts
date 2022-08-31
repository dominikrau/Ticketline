import {TestBed} from '@angular/core/testing';

import {SeatingChartService} from './seating-chart.service';

describe('SeatingChartService', () => {
  let service: SeatingChartService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SeatingChartService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
