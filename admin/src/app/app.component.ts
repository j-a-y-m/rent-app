import { Component } from '@angular/core';
import { AngularFireAuth} from '@angular/fire/auth';
import firebase from 'firebase/app';
import * as firebaseui from 'firebaseui';
import 'firebaseui/dist/firebaseui.css';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'rent-app';
  constructor(public auth : AngularFireAuth) {
  }


}
