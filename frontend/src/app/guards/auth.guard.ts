import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard = () => {
    // Get instances of AuthService and Router using Angular's inject function
    const authService = inject(AuthService);
    const router = inject(Router);

    // Check if the user is currently logged in
    if (authService.isLoggedIn()) {
        // User is authenticated, allow access to the route
        return true;
    }

    // User is not logged in, redirect to the login page
    // Returning the router navigation ensures the guard blocks access
    return router.navigate(['/login']);
};
