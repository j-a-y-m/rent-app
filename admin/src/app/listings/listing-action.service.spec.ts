import { TestBed } from '@angular/core/testing';

import { ListingActionService } from './listing-action.service';

describe('ListingActionService', () => {
  let service: ListingActionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListingActionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
