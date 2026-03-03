import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: `./login.component.html`,
  styleUrls: [`./login.component.css`]
})
export class LoginComponent {
  username = '';
  password = '';
  loading = false;
  errorMsg = '';

  constructor(private authService: AuthService, private router: Router) {}

  fillDemo(u: string, p: string) {
    this.username = u;
    this.password = p;
  }

    onLogin() {
        // Basic validation: do not proceed if username or password is empty
        if (!this.username || !this.password) return;

        // Show a loading indicator while the login request is in progress
        this.loading = true;

        // Clear any previous error messages
        this.errorMsg = '';

        // Call AuthService.login() with the entered credentials
        // login() returns an Observable<LoginResponse>
        this.authService.login({ username: this.username, password: this.password }).subscribe({

            // Success handler
            next: () => {
                // Navigate to the dashboard on successful login
                this.router.navigate(['/dashboard']);
            },

            // Error handler
            error: (err) => {
                // Display error message returned from the backend (if any)
                // Otherwise, show a default message
                this.errorMsg = err.error || 'Invalid credentials. Please try again.';

                // Stop loading indicator since request is complete
                this.loading = false;
            }
        });
    }
}
