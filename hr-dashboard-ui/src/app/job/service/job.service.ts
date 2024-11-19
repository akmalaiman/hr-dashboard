import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {JobPosition} from "../../common/model/job-position.model";

@Injectable({
        providedIn: 'root'
})
export class JobService {

        private getJobListUrl = 'http://localhost:8080/api/jobPosition/all';
        private getJobNameUrl = 'http://localhost:8080/api/jobPosition/byName';
        private createJobUrl = 'http://localhost:8080/api/jobPosition/add';
        private getJobNameWithStaffCountUrl = 'http://localhost:8080/api/jobPosition/nameWithCount';

        constructor(private http: HttpClient) { }

        getJobList(): Observable<JobPosition[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<JobPosition[]>(this.getJobListUrl, {headers});
        }

        getJobPositionName(jobName: string): Observable<any[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("name", jobName);
                return this.http.get<any>(this.getJobNameUrl, {headers, params});
        }

        createJobPosition(jobPosition: JobPosition): Observable<JobPosition> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.post<JobPosition>(this.createJobUrl, jobPosition, {headers});
        }

        getJobNameWithStaffCount(): Observable<any[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<any[]>(this.getJobNameWithStaffCountUrl, {headers});
        }

}
