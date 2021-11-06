import {firebase, firebaseui, FirebaseUIModule} from 'firebaseui-angular';
export const environment = {
  production: true,
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
