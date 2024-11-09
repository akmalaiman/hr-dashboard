import {Component} from '@angular/core';
import {NavigationExtras, Router, RouterLink, RouterLinkActive} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Component({
        selector: 'app-navbar',
        standalone: true,
        imports: [
                RouterLink,
                RouterLinkActive
        ],
        templateUrl: './navbar.component.html',
        styleUrl: './navbar.component.css'
})
export class NavbarComponent {

        constructor(private http: HttpClient, private router: Router) {
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
