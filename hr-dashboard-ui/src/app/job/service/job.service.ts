import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {JobPosition} from "../../common/model/job-position.model";

@Injectable({
        providedIn: 'root'
})
export class JobService {

        private jobListUrl = 'http://localhost:8080/api/jobPosition/all';

        constructor(private http: HttpClient) { }

        getJobList(): Observable<JobPosition[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<JobPosition[]>(this.jobListUrl, {headers});
        }

}
