import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ListingRequest} from '../listings/listing-request.model';
import {ifStmt} from '@angular/compiler/src/output/output_ast';
import {ListingRequestService} from '../listings/listing-request.service';
import {ListingAction} from '../listings/listing-action.model';
import {ListingActionOptions} from '../listings/listing-action-options.enum';



@Component({
  selector: 'app-listing-detail',
  templateUrl: './listing-detail.component.html',
  styleUrls: ['./listing-detail.component.scss']
})
export class ListingDetailComponent implements OnInit {
  @Input()
  public declare listingDetail : ListingRequest;

  @Output()
  listingAction = new EventEmitter<ListingAction>();

   actions = ListingActionOptions;

  constructor() {  }

  ngOnInit(): void {
  }

  performAction(listing : ListingRequest, action : ListingActionOptions) : void
  {
    this.listingAction.emit(new ListingAction(listing, action));

  }

}


