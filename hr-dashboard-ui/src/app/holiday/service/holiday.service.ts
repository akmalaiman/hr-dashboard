import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Holiday} from "../../common/model/holiday.model";

@Injectable({
    providedIn: 'root'
})
export class HolidayService {

        private getHolidayListUrl = 'http://localhost:8080/api/holiday/all';
        private createHolidayUrl = 'http://localhost:8080/api/holiday/add';

        constructor(private http: HttpClient) { }

        getHolidayList(): Observable<Holiday[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<Holiday[]>(this.getHolidayListUrl, {headers});
        }

        createHoliday(holiday: Holiday): Observable<Holiday> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.post<Holiday>(this.createHolidayUrl, holiday, {headers});
        }

}
