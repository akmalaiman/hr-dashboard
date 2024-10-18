import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
        selector: 'app-login',
        standalone: true,
        imports: [FormsModule, NgIf],
        templateUrl: './login.component.html',
        styleUrl: './login.component.css'
})
export class LoginComponent {

        loginObject: Login;
        errorMessage: string | null = null;

        constructor(private http: HttpClient, private router: Router) {
                this.loginObject = new Login();
        }

        onLogin() {
                this.http.post('http://localhost:8080/api/auth/login', this.loginObject).subscribe({
                        next: (res: any) => {
                                if (res.ok) {
                                        this.errorMessage = null;
                                        this.router.navigateByUrl("/home");
                                }
                        },
                        error: (err) => {
                                this.errorMessage = "Invalid credentials. Please try again.";
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
