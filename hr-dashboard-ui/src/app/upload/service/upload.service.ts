import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable({
        providedIn: 'root'
})
export class UploadService {

        private templateUrl = "http://localhost:8080/api/template/download";

        constructor(private http: HttpClient) {}

        getTemplate(filename: string): Observable<Blob> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("templateName", filename);
                return this.http.get(this.templateUrl, {headers, params, responseType: 'blob'});
        }

}
