/* tslint:disable:no-trailing-whitespace */
import {Component, Input, OnInit, OnChanges, SimpleChanges} from '@angular/core';
import {ListingItem} from './listingItem.model';
import {ListingRequestService} from '../listings/listing-request.service';
import {ListingRequest} from '../listings/listing-request.model';

@Component({
  selector: 'app-listing-item',
  templateUrl: './listing-item.component.html',
  styleUrls: ['./listing-item.component.scss']
})
export class ListingItemComponent implements OnInit, OnChanges {

  // @ts-ignore
  @Input()
  listingItem: any;
  declare img: string;

  constructor(private listingRequestService: ListingRequestService) {

    // this.listingItem = listingRequestService.getListing();
    // this.img = this.listingItem.img[0];
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.listingItem = changes['listingItem'].currentValue;
  }

  ngOnInit(): void {

  }



}
