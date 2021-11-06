import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { AngularFireAuth} from '@angular/fire/auth';
import {Observable} from 'rxjs';
import firebase from 'firebase';
import {ListingActionOptions} from './listing-action-options.enum';
import {ListingAction} from './listing-action.model';
import {ListingRequest} from './listing-request.model';
import {PushNotificationService} from './push-notification.service';
import { mapToObject } from '@angular/fire/remote-config';
import { firestore } from 'googleapis/build/src/apis/firestore';


@Injectable({
  providedIn: 'root'
})
export class ListingActionService {

  constructor(private fireStore  : AngularFirestore, private auth : AngularFireAuth,private notificationService: PushNotificationService) { }

  performAction(listingAction : ListingAction) : void
  {

    // this.auth.user.subscribe({next:(user) => console.log(user?.uid)  }) ;

    switch (listingAction.action)
      {
        case ListingActionOptions.APPROVE: this.approve(listingAction.listing); break;
        case ListingActionOptions.REJECT: this.reject(listingAction.listing); break;
      }
  }

  private approve(listing: ListingRequest) : void {

      // const token = listing.token;
      const ownerDetailsObservable = this.fireStore.collection('Owners').doc(listing.createdBy).collection('private').doc('details').get();
      const ownerslistingRef = this.fireStore.collection('Owners').doc(listing.createdBy).collection('listings').doc(listing.id).ref
      const newListingRef = this.fireStore.collection('Listings').doc(listing.id).ref;
      const citiesRef = this.fireStore.collection('Cities').doc(listing.city.toLowerCase()).collection('listings').doc(listing.id).ref;
      // newListingRef.set(listing);
      // newListingRef.set({title : listing.title, address : listing.address, area : listing.area,
      //                    city : listing.city, createdBy : listing, description : listing.description, listingBhk : listing.bhk,
      //                    listingType : listing.type, price : 7000, totalOccupants : listing.occupants,
      //                    facilities : listing.facilities, listingImages : listing.img,});
      //console.log('listing-action-approve',listing);


      // newListingRef.set(Object.assign({},listing));
      // newListingRef.set({currentOccupants: 0});
      // newListingRef.collection('private').doc('Status').set({approved: true, currentOccupants: 0});
      // citiesRef.set(Object.assign({},listing.id));
      // this.fireStore.collection('ListingRequests').doc(listing.id).delete().then(d => console.log(d));


      // this.notificationService.notify('cKhUtkkyTBauMjdk6eJk_n:APA91bHp-uOQRpAEqJH9FE-6B4iWpUpcXZ38LfNOt-68QQhhcd6LrM1M82NyDvIYWPDzYi4-5wpLN6Z7QLpRoWKuP_tX-VoMeslz3ElE91P9KuKA_6myN2mg3Xz0bNiZ2jdqdOpnuDN6','Listing Approved','Your listing was approved');
            
      
      const batch = this.fireStore.firestore.batch();

      // newListingRef.set(Object.assign({},listing));
      // newListingRef.set({currentOccupants: 0});
      // newListingRef.collection('private').doc('Status').set({approved: true, currentOccupants: 0});
      // citiesRef.set(Object.assign({},listing.id));
      // this.fireStore.collection('ListingRequests').doc(listing.id).delete().then(d => console.log(d));
      // console.log("setlisting ",listing);
      // let l =Object.assign({},listing) ;
      // console.log("l",l);
      // firebase.firestore.setLogLevel('debug')

          batch.set(newListingRef,listing)
          .update(newListingRef,{currentOccupants: 0})
          .set(citiesRef,{listingId:listing.id})
          .set(ownerslistingRef,{listingId:listing.id})
          .delete(this.fireStore.collection('ListingRequests').doc(listing.id).ref);
          ownerDetailsObservable.subscribe(
            {
              next: (result)=>{
                batch.commit().then(()=>{
    
                  const fcmToken = result.get('fcmToken');
                this.notificationService.notify(fcmToken,'Listing Approved','Your listing was approved');
                }).catch(
                  (error)=>{console.log(error);}
                );  } } );
        
      
      // batch.set(newListingRef,{currentOccupants: 0});
      // batch.set(citiesRef.ref,Object.assign({},listing.id.toLowerCase));
      // batch.delete(this.fireStore.collection('ListingRequests').doc(listing.id).ref);
      



  }


  private reject(listing: ListingRequest) : void {
    const ownerDetailsObservable = this.fireStore.collection('Owners').doc(listing.createdBy).collection('private').doc('details').get();
    // const token = listing.token;
    const newListingRef = this.fireStore.collection('ListingRequests').doc(listing.id).ref;
    const citiesRef = this.fireStore.collection('Cities').doc(listing.city.toLowerCase()).collection('listings').doc(listing.id).ref;
    console.log("reject")
    const batch = this.fireStore.firestore.batch();

    batch.delete(newListingRef); //.delete(citiesRef)

    //newListingRef.delete();
    // this.notificationService.notify('cKhUtkkyTBauMjdk6eJk_n:APA91bHp-uOQRpAEqJH9FE-6B4iWpUpcXZ38LfNOt-68QQhhcd6LrM1M82NyDvIYWPDzYi4-5wpLN6Z7QLpRoWKuP_tX-VoMeslz3ElE91P9KuKA_6myN2mg3Xz0bNiZ2jdqdOpnuDN6','Listing Rejected','Your listing was rejected ');

    batch.commit().then(()=>{

      ownerDetailsObservable.subscribe(
        {
          next: (result)=>{
            const fcmToken = result.get('fcmToken');
            this.notificationService.notify(fcmToken,'Listing Rejected','Your listing was rejected ');
          }
      }
    );
    }).catch((err)=>{console.log(err);});
    

    // console.log(newListingRef);
    // newListingRef.get().then(res => console.log(res));
    // // toPromise().then();
    //
    //
    // newListingRef.delete().catch(err => console.log(err));  // .delete();
  }
}
