import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import firebase from 'firebase';




@Injectable({
  providedIn: 'root'
})
export class PushNotificationService {
  private readonly URL = 'https://fcm.googleapis.com/v1/projects/app-for-pg-hostel-rental/notification/messages:send';
  private readonly KEY = 'AAAA_6RVVtM:APA91bH8g_Q2gOxzT6rhnc9qrAavtlLB4wsFDmvWGYIncwT5400DHoktPKHopsm6eupRLBIDcgqb6S1yAH-hJHKEJzb5eYMJ0Lx2BrG0BwLtXk55STO9vQ7_2SKiQFqVEZd9aFqdkr3m' ;
  private readonly oauth = 'ya29.c.Kp8B-wc-Ytr_CX-QZycWfdFKXac_YXUclTbXezZCG4xHdL-h-0AMdtD-V1Kouuotj2HLup92hnSbvZTXOcL0_Wv0ei1GyC5HtrRpUX3ouI5rAntsRWbnXTqPPr4HVl0Z2YsxRDt8EAbOv1SITkCzhHH1XPmzvWkqzj1RCmWtLZCZuVeofa24FpbGECK7NKj1WTv95dTBBe7cY5eAVoxbCwzh';
  private readonly CONTENT_TYPE = 'application/json';
  // private readonly HEADERS = headers: {'Authorization': 'Bearer ' + accessToken};

  constructor(private http : HttpClient) { }

  notify(userToken: string,title: string, message: string) : void
  {

    console.log("usertoken",userToken);
    const notif = {
    message: {
      token: userToken,
      notification: {
        title,
        body: message
      }
    }
  };
    const notifyUser = firebase.functions().httpsCallable('notifyUser');
    notifyUser({ userToken: userToken,
                      title: title,
                      message: message})
      .then((result) => {
        // Read result of the Cloud Function.
      })
      .catch((error) => {
        // Getting the Error details.
        var code = error.code;
        var message = error.message;
        var details = error.details;
        console.log(code,message,details);
        // ...
      });
    // this.http.post(this.URL,notif,{headers:{Authorization: 'Bearer ' + this.oauth,'Access-Control-Allow-Origin':'*'} }).subscribe(
    //   {
    //     next:(res) => { console.log(res) ;}
    //   }
    //
    // );
  }
}
