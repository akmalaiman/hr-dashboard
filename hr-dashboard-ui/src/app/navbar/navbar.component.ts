import {Component} from '@angular/core';
import {NavigationExtras, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {AuthService} from "../auth/auth.service";

@Component({
        selector: 'app-navbar',
        standalone: true,
        imports: [
                RouterLink,
                RouterLinkActive,
                NgIf
        ],
        templateUrl: './navbar.component.html',
        styleUrl: './navbar.component.css'
})
export class NavbarComponent {

        isAdmin: boolean = false;
        isManager: boolean = false;

        constructor(private http: HttpClient, private router: Router, public authService: AuthService) {
                this.isAdmin = this.authService.isAdmin();
                this.isManager = this.authService.isManager();
        }

        onLogout() {
                this.http.post('http://localhost:8080/api/auth/logout', {}).subscribe({
                        next: (res: any) => {
                                const navigationExtras: NavigationExtras = {
                                        state: {
                                                successMessage: 'Successfully logged out'
                                        }
                                }
                                localStorage.removeItem("access_token");
                                this.router.navigate(['/login'], navigationExtras);
                        },
                        error: (err) => {
                                console.warn("Error logged out: {}", err);
                        }
                });
        }

}
