import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {HomeComponent} from "./home/home.component";
import {LayoutComponent} from "./layout/layout.component";
import {authGuard} from "./auth.guard";
import {UserManagementComponent} from "./user-management/user-management.component";

export const routes: Routes = [
        {
                path: "",
                redirectTo: "login",
                pathMatch: "full"
        },
        {
                path: "login",
                component: LoginComponent,
        },
        {
                path: "",
                component: LayoutComponent,
                children: [
                        {
                                path: "home",
                                component: HomeComponent,
                                canActivate: [authGuard]
                        },
                        {
                                path: "user",
                                component: UserManagementComponent,
                                canActivate: [authGuard]
                        }
                ]
        }
];
