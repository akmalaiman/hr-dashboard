import {Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {HomeComponent} from "./home/home.component";
import {LayoutComponent} from "./layout/layout.component";
import {authGuard} from "./auth/auth.guard";
import {StaffHomeComponent} from "./staff/staff-home/staff-home.component";
import {StaffNewComponent} from "./staff/staff-new/staff-new.component";
import {JobHomeComponent} from "./job/job-home/job-home.component";
import {UploadHomeComponent} from "./upload/upload-home/upload-home.component";
import {DepartmentHomeComponent} from "./department/department-home/department-home.component";
import {StaffEditComponent} from "./staff/staff-edit/staff-edit.component";
import {HolidayHomeComponent} from "./holiday/holiday-home/holiday-home.component";

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
                                children: [
                                        {
                                                path: "",
                                                component: StaffHomeComponent,
                                                canActivate: [authGuard]
                                        },
                                        {
                                                path: "edit/:id",
                                                component: StaffEditComponent,
                                                canActivate: [authGuard]
                                        },
                                        {
                                                path: "new",
                                                component: StaffNewComponent,
                                                canActivate: [authGuard]
                                        }
                                ]
                        },
                        {
                                path: "job",
                                component:JobHomeComponent,
                                canActivate: [authGuard]
                        },
                        {
                                path: "upload",
                                component: UploadHomeComponent,
                                canActivate: [authGuard]
                        },
                        {
                                path: "department",
                                component: DepartmentHomeComponent,
                                canActivate: [authGuard]
                        },
                        {
                                path: "holiday",
                                component: HolidayHomeComponent,
                                canActivate: [authGuard]
                        }
                ]
        },
        {
                path: "**",
                redirectTo: "login"
        }
];
