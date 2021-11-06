import {ListingRequest} from './listing-request.model';
import {ListingActionOptions} from './listing-action-options.enum';
export class ListingAction{
   listing : ListingRequest ;
   action : ListingActionOptions ;

   constructor( listing : ListingRequest ,  action : ListingActionOptions) {
     this.listing = listing;
     this.action = action;
   }
}
