import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { LoginRequest, LoginResponse } from '../models/transaction.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
         // Private BehaviorSubject holding the current user data or null if not logged in.
        // BehaviorSubject emits the latest value to all subscribers immediately upon subscription.
    private currentUserSubject = new BehaviorSubject<LoginResponse | null>(null);

        // Public observable exposing current user data stream without allowing external emission.
        // Components subscribe to this to get updates about the current user's login state.
    currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
            // Retrieve stored user from localStorage
      const stored = localStorage.getItem('currentUser');

            //  If there is stored data
      if (stored) {
            // Parse JSON and update BehaviorSubject
          this.currentUserSubject.next(JSON.parse(stored));
      }
  }

    login(request: LoginRequest): Observable<LoginResponse> {
        // Sends a POST request to the backend authentication endpoint
        // `environment.apiUrl` holds the base API URL (e.g., http://localhost:8080/api)
        return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request).pipe(

            // 'tap' is an RxJS operator for performing side effects without changing the response
            tap(response => {

                // Persist the user data (including JWT or session info) in browser localStorage
                // This allows session to survive page reloads
                localStorage.setItem('currentUser', JSON.stringify(response));

                // Update the BehaviorSubject with the new user data
                // All components subscribed to currentUser$ will automatically receive the update
                this.currentUserSubject.next(response);
            })
        );
    }

    logout(): void {
        // Remove the stored user data from localStorage to clear session persistence
        localStorage.removeItem('currentUser');

        // Reset the BehaviorSubject to null
        // All components subscribed to currentUser$ will now know that the user is logged out
        this.currentUserSubject.next(null);

        // Navigate the user to the login page after logout
        // Using Angular Router ensures a clean route change without full page reload
        this.router.navigate(['/login']);
    }

    getToken(): string | null {
        // Accesses the current value stored in the BehaviorSubject
        // Returns the user's token if the user is logged in
        // Returns null if no user is logged in
        return this.currentUserSubject.value?.token || null;
        /*If a user is logged in:
                  currentUserSubject.value is LoginResponse → .token exists → returns the token string.
               If no user is logged in:
                  currentUserSubject.value is null → ?.token evaluates to undefined → || null returns null.
         */
    }

    isLoggedIn(): boolean {
        // Returns true if there is a currently logged-in user, false otherwise
        // 'this.currentUserSubject.value' holds the current user object or null
        // Double NOT operator (!!) converts the value into a boolean
        return !!this.currentUserSubject.value;
    }

    getCurrentUser(): LoginResponse | null {
        // Returns the current logged-in user object stored in the BehaviorSubject
        // If no user is logged in, this will return null
        // This allows components or services to synchronously access the user data without subscribing
        return this.currentUserSubject.value;
    }
}
