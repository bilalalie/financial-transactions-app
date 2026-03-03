import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    // Get an instance of AuthService using Angular's inject function
    // This allows us to access the current user's token without constructor injection
    const authService = inject(AuthService);

    // Retrieve the JWT token of the currently logged-in user
    const token = authService.getToken();

    // If a token exists, clone the original HTTP request and add the Authorization header
    // This attaches the Bearer token to all outgoing HTTP requests for authentication
    if (token) {
        const cloned = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        return next(cloned); // Pass the modified request to the next handler
    }

    // If no token exists, pass the original request without modification
    return next(req);
};
