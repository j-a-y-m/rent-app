import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';

import { MatAutocompleteModule  } from '@angular/material/autocomplete';
import { MatIconModule } from '@angular/material/icon';
import {MatCardModule} from '@angular/material/card';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatButtonModule} from '@angular/material/button';

import {firebase, firebaseui, FirebaseUIModule} from 'firebaseui-angular';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavComponent } from './nav/nav.component';
import { environment } from 'src/environments/environment';

import { SearchComponent } from './search/search.component';
import {ReactiveFormsModule} from '@angular/forms';
import { ListingItemComponent } from './listing-item/listing-item.component';
import { ListingsComponent } from './listings/listings.component';
import { ListingDetailComponent } from './listing-detail/listing-detail.component';


import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from '@angular/fire/firestore';
import { AngularFireAuthModule } from '@angular/fire/auth';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    SearchComponent,
    ListingItemComponent,
    ListingsComponent,
    ListingDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    AngularFireModule.initializeApp(environment.firebase),
    FirebaseUIModule.forRoot(environment.firebaseUiAuthConfig),
    AngularFireAuthModule,
    AngularFirestoreModule,
    MatAutocompleteModule,
    MatIconModule,
    MatCardModule,
    MatGridListModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
