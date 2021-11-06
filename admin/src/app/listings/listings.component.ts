/* tslint:disable:no-trailing-whitespace */
import { Component, OnInit } from '@angular/core';
import {ListingRequestService} from './listing-request.service';
import {ListingRequest} from './listing-request.model';
import {Observable} from 'rxjs';
import {ListingAction} from './listing-action.model';
import {ListingActionService} from './listing-action.service';


@Component({
  selector: 'app-listings',
  templateUrl: './listings.component.html',
  styleUrls: ['./listings.component.scss'],
  providers: [ListingRequestService]
})
export class ListingsComponent implements OnInit {

  private containerSizes :  string[] = ['100%', '60%'] ;
  // containerSize: string;
  // cols: number;
  // containerSize = { 'width' : '100%'};



//   stateOnlyList = {
//   'containerSize' : '100%',
//   'cols' : 3
// } ;
  stateOnlyList = new State('100%', 3,'none') ;
  stateListAndDetails = new State('60%', 2,'inline') ;
  // stateListAndDetails = {
  //   'containerSize' : '60%' ,
  //   'cols' : 2
  // } ;
  state = this.stateOnlyList ;
  listingRequests : Observable<ListingRequest[]> ;
  selectedListingRequest : ListingRequest ;
  constructor(private listingRequestService : ListingRequestService, private listingActionService : ListingActionService) {

    this.listingRequests = listingRequestService.getListings();
    this.selectedListingRequest = listingRequestService.getListing();
  }


  ngOnInit(): void {

  }

  click(listingRequest: ListingRequest) : void
  {

      // this.state === this.stateOnlyList ?  this.state = this.stateListAndDetails : this.state = this.stateOnlyList ;
      if (this.state === this.stateListAndDetails && this.selectedListingRequest === listingRequest)
      {
        this.state = this.stateOnlyList;

      }else
      if (this.state === this.stateOnlyList)
      {
        this.state = this.stateListAndDetails;
        this.selectedListingRequest = listingRequest;
      }else
      {
        this.selectedListingRequest = listingRequest;
      }
      this.selectedListingRequest = listingRequest;

  }
  public performAction(action : ListingAction) : void
  {
   this.listingActionService.performAction(action);
  }

}

class State
{
  cols: number;
  listingsStyle : any;
  listingDetailStyle : any;

  constructor(containerSize : string , cols : number , listingDetail : string) {
    this.listingsStyle ={'width' : containerSize}  ;
    this.cols = cols;
    this.listingDetailStyle = {'display' : listingDetail} ;
  }



}
