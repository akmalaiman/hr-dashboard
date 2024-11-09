import {CanActivateFn, NavigationExtras, Router} from '@angular/router';
import {inject} from "@angular/core";
import {jwtDecode} from "jwt-decode";

export const authGuard: CanActivateFn = (route, state) => {

        const token = localStorage.getItem('access_token');
        const router = inject(Router);

        if (token) {
                const decodedToken: any = jwtDecode(token);
                const expirationTime = decodedToken.exp * 1000;

                if (Date.now() > expirationTime) {
                        const navigationExtras: NavigationExtras = {
                                state: {
                                        errorMessage: 'You have been logged out due to session expiration. Please log in again.'
                                }
                        }

                        localStorage.removeItem('access_token');
                        router.navigate(['/login'], navigationExtras);
                        return false;
                }

                return true;
        } else {
                router.navigate(['/login']);
                return false;
        }

};
