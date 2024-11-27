import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Staff} from "../../common/model/staff.model";
import {Observable} from "rxjs";
import {JobPosition} from "../../common/model/job-position.model";
import {Role} from "../../common/model/role.model";
import {Injectable} from "@angular/core";
import {Department} from "../../common/model/department.model";

@Injectable({
        providedIn: 'root'
})
export class StaffService {

        private jobPositionUrl = "http://localhost:8080/api/jobPosition/all";
        private roleUrl = "http://localhost:8080/api/role/all";
        private createUserUrl = "http://localhost:8080/api/user/add";
        private getUsernameUrl = "http://localhost:8080/api/user/byUsername";
        private getEmailUrl = "http://localhost:8080/api/user/byEmail";
        private deleteUserUrl = "http://localhost:8080/api/user/delete";
        private getDepartmentUrl = "http://localhost:8080/api/department/all";
        private  getStaffListUrl = "http://localhost:8080/api/user/all";
        private getStaffByIdUrl = "http://localhost:8080/api/user/{id}";

        constructor(private http: HttpClient) { }

        createUser(user: Staff): Observable<Staff> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.post<Staff>(this.createUserUrl, user, {headers});
        }

        getJobPosition(): Observable<JobPosition[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<JobPosition[]>(this.jobPositionUrl, {headers});
        }

        getRole(): Observable<Role[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<Role[]>(this.roleUrl, {headers});
        }

        getUsername(username: string): Observable<Staff> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("username", username);
                return this.http.get<Staff>(this.getUsernameUrl, {headers, params});
        }

        getEmail(email: string): Observable<Staff> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("email", email);
                return this.http.get<Staff>(this.getEmailUrl, {headers, params});
        }

        deleteUser(id: number): Observable<any> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("userId", id);
                return this.http.delete<any>(this.deleteUserUrl, {headers, params});
        }

        getDepartment(): Observable<Department[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<Department[]>(this.getDepartmentUrl, {headers});
        }

        getStaffList(): Observable<any[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<any[]>(this.getStaffListUrl, {headers});
        }

        getStaff(id: number): Observable<any> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const url = this.getStaffByIdUrl.replace("{id}", id.toString());
                return this.http.get<any>(url, {headers});
        }

}
