import { TestBed } from '@angular/core/testing';

import { ListingRequestService } from './listing-request.service';

describe('ListingRequestService', () => {
  let service: ListingRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListingRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
