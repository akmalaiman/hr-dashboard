import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Department} from "../../common/model/department.model";

@Injectable({
        providedIn: 'root'
})
export class DepartmentService {

        private getDepartmentNameUrl = 'http://localhost:8080/api/department/byName';
        private createDepartmentUrl = 'http://localhost:8080/api/department/add';
        private getDepartmentWithStaffCountUrl = 'http://localhost:8080/api/department/nameWithCount';
        private deleteDepartmentUrl = 'http://localhost:8080/api/department/delete';

        constructor(private http: HttpClient) { }

        getDepartmentName(departmentName: string): Observable<any[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("name", departmentName);
                return this.http.get<any>(this.getDepartmentNameUrl, {headers, params});
        }

        createDepartment(department: Department): Observable<Department> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.post<Department>(this.createDepartmentUrl, department, {headers});
        }

        getDepartmentWithStaffCount(): Observable<any[]> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                return this.http.get<any[]>(this.getDepartmentWithStaffCountUrl, {headers});
        }

        deleteDepartment(id: number): Observable<any> {
                const token = localStorage.getItem("access_token");
                const headers = new HttpHeaders({
                        'Authorization': `Bearer ${token}`
                });
                const params = new HttpParams().set("id", id);
                return this.http.delete<any>(this.deleteDepartmentUrl, {headers, params});
        }

}
