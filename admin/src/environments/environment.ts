// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import {firebase, firebaseui, FirebaseUIModule} from 'firebaseui-angular';


export const environment = {
  production: false,
  firebase: {
    apiKey: 'AIzaSyAt-rlLtk5zwXb3nAbQ7HUWrWyJAQ4jfc0',
    authDomain: 'app-for-pg-hostel-rental.firebaseapp.com',
    projectId: 'app-for-pg-hostel-rental',
    storageBucket: 'app-for-pg-hostel-rental.appspot.com',
    messagingSenderId: '1097973716691',
    appId: '1:1097973716691:web:c7c5dd7aa0bacb531b2626',
  },
  firebaseUiAuthConfig: {
  signInFlow: 'redirect',
  signInOptions: [{
    provider: firebase.auth.EmailAuthProvider.PROVIDER_ID,
    disableSignUp: {status: true, helpLink: undefined}
  }]
}
};



/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
