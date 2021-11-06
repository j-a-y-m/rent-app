import { Injectable } from '@angular/core';
import {ListingRequest} from './listing-request.model';
import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireAuth} from '@angular/fire/auth';
import {Observable} from 'rxjs';
import firebase from 'firebase';
import firestore = firebase.firestore;
import {mapToObject} from '@angular/fire/remote-config';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ListingRequestService {

  constructor(private fireStore  : AngularFirestore, private auth : AngularFireAuth) { }

 getListings() : Observable<ListingRequest[]>
  {

    // let listingRequests : ListingRequest[];

    const listingRequestsRef = this.fireStore.collection('ListingRequests');

    // const listingRequest = new ListingRequest(' PG for Anyone in Thane West', 'Manpada,Near to Hiranandani Medows, Thane West  ', '5000',
    //   'PG has easy access to market, schools, hospitals, banks and ATMs as' +
    //   ' well.PG has easy access to market, schools, hospitals, banks and ATMs as ' +
    //   'PG has easy access to market, schools, hospitals, banks and ATMs as well. ', 'Thane', '500', '2', Array<string>('facilities') ,
    //   'PG', '3bhk', Array<string>('https://images.nobroker.in/images/8a9f88827827317a0178277279a30f45/8a9f88827827317a0178277279a30f45_91869_56842_large.jpg')) ;
    //
    // return Array(listingRequest, listingRequest, listingRequest, listingRequest, listingRequest, listingRequest);
    const listingRequests = this.fireStore.collection<ListingRequest>('ListingRequests').valueChanges({idField: 'id'});
                              // .pipe(map(collection => collection.docs.map(doc => new ListingRequest(doc.id, doc.get('title'),
                              // doc.get('address'), doc.get('price'), doc.get('description'), doc.get('city'), doc.get('area'),
    // doc.get('totalOccupants'), doc.get('facilities'), doc.get('listingType'), doc.get('listingBhk'), doc.get('listingImages'),
    // doc.get('createdBy')))) );
    listingRequests.subscribe({next:(listing) => {console.log(listing);}});
    console.log(listingRequests);
    return listingRequests;

  }

  getListing() : ListingRequest {
    const listingRequest = new ListingRequest('0', ' PG for Anyone in Thane West', 'Manpada,Near to Hiranandani Medows, Thane West  ', '5000',
      'PG has easy access to market, schools, hospitals, banks and ATMs as' +
      ' well.PG has easy access to market, schools, hospitals, banks and ATMs as ' +
      'PG has easy access to market, schools, hospitals, banks and ATMs as well. ', 'Thane', '500', '2', Array<string>('facilities') , 'PG', '3bhk',
      Array<string>('https://images.nobroker.in/images/8a9f88827827317a0178277279a30f45/8a9f88827827317a0178277279a30f45_91869_56842_large.jpg'),'hardcoded') ;
    return listingRequest;
  }
}
