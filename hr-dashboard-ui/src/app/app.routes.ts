import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {HomeComponent} from "./home/home.component";
import {LayoutComponent} from "./layout/layout.component";
import {authGuard} from "./guards/auth.guard";
import {StaffHomeComponent} from "./staff-home/staff-home.component";
import {StaffNewComponent} from "./staff-new/staff-new.component";
import {JobHomeComponent} from "./job-home/job-home.component";

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
                                component: StaffHomeComponent,
                                canActivate: [authGuard]
                        },
                        {
                                path: "user/new",
                                component: StaffNewComponent,
                                canActivate: [authGuard]
                        },
                        {
                                path: "job",
                                component:JobHomeComponent,
                                canActivate: [authGuard]
                        }
                ]
        }
];
