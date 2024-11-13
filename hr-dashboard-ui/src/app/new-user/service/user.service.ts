import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
import {User} from "../model/user.model";
import {catchError, Observable, of, throwError} from "rxjs";
import {JobPosition} from "../model/job-position.model";
import {Role} from "../model/role.model";
import {Injectable} from "@angular/core";

@Injectable({
        providedIn: 'root'
})
export class UserService {

        private jobPositionUrl = "http://localhost:8080/api/jobPosition/all";
        private roleUrl = "http://localhost:8080/api/role/all";
        private createUserUrl = "http://localhost:8080/api/user/add";
        private getUsernameUrl = "http://localhost:8080/api/user/byUsername";

        constructor(private http: HttpClient) { }

        createUser(user: User): Observable<User> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.post<User>(this.createUserUrl, user, {headers});
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

        getUsername(username: string): Observable<any> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("username", username);
                return this.http.get<any>(this.getUsernameUrl, {headers, params});
        }

}
