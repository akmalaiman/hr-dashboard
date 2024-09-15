import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
        selector: 'app-login',
        standalone: true,
        imports: [FormsModule],
        templateUrl: './login.component.html',
        styleUrl: './login.component.css'
})
export class LoginComponent {

        loginObject: Login;

        constructor(private http: HttpClient, private router: Router) {
                this.loginObject = new Login();
        }

        onLogin() {
                this.http.post('http://localhost:8080/api/auth/login', this.loginObject).subscribe({
                        next: (res: any) => {
                                if (res.ok) {
                                        console.log("Successfully logged in");
                                        this.router.navigateByUrl("/home");
                                }
                        },
                        error: (err) => {
                                console.log("Failed to log in");
                        }
                });
        }

}

export class Login {

        username: string;
        password: string;

        constructor() {
                this.username = "";
                this.password = "";
        }
}
