import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {HomeComponent} from "./home/home.component";
import {LayoutComponent} from "./layout/layout.component";
import {authGuard} from "./guards/auth.guard";
import {UserManagementComponent} from "./user-management/user-management.component";
import {NewUserComponent} from "./new-user/new-user.component";

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
                        },
                        {
                                path: "user/new",
                                component: NewUserComponent,
                                canActivate: [authGuard]
                        }
                ]
        }
];
