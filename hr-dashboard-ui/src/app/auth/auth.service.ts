import {Injectable} from "@angular/core";
import {jwtDecode} from "jwt-decode";

@Injectable({
        providedIn: 'root'
})
export class AuthService {

        getRole() {
                const token = localStorage.getItem('access_token');

                if (token) {
                        const decodedToken: any = jwtDecode(token);
                        return decodedToken.roles;
                }

                return null;
        }

        isAdmin(): boolean {
                return this.getRole() === 'ADMIN';
        }

        isManager(): boolean {
                return this.getRole() === 'MANAGER';
        }

}
