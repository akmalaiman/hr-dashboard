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
    private getHolidayByIdUrl = 'http://localhost:8080/api/holiday/{id}';
    private updateHolidayUrl = 'http://localhost:8080/api/holiday/update';

    constructor(private http: HttpClient) {
    }

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

    updateHoliday(holiday: Holiday): Observable<Holiday> {
        const token = localStorage.getItem("access_token");
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        return this.http.put<Holiday>(this.updateHolidayUrl, holiday, {headers});
    }

    getHolidayById(id: number): Observable<Holiday> {
        const token = localStorage.getItem("access_token");
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${token}`
        });
        const url = this.getHolidayByIdUrl.replace("{id}", id.toString());
        return this.http.get<Holiday>(url, {headers});
    }

}
