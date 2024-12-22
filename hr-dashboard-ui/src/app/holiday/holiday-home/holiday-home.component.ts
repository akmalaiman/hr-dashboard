import {AfterViewChecked, Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgbModal, NgbModalConfig} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from "../../auth/auth.service";
import {HolidayService} from "../service/holiday.service";
import {Holiday} from "../../common/model/holiday.model";

@Component({
        selector: 'app-calendar',
        standalone: true,
        imports: [
                RouterLink,
                NgIf,
                FormsModule,
                ReactiveFormsModule,
                DatePipe,
                NgForOf
        ],
        templateUrl: './holiday-home.component.html',
        styleUrl: './holiday-home.component.css'
})
export class HolidayHomeComponent implements OnInit, AfterViewChecked, OnDestroy {

        pageName: string = 'Holiday';
        loading: boolean = true;
        isHolidayExists: boolean = false;
        isAdmin: boolean = false;
        isManager: boolean = false;
        newHolidayForm!: FormGroup;
        currentView: 'list' | 'calendar' = 'list';
        holidayList: Holiday[] = [];

        private isDataTableInit: boolean = false;
        private dataTable: any;

        constructor(config: NgbModalConfig, private modalService: NgbModal, private formBuilder: FormBuilder, private authService: AuthService, private holidayService: HolidayService) {
                config.backdrop = "static";
                config.keyboard = false;
                this.isAdmin = this.authService.isAdmin();
                this.isManager = this.authService.isManager();
        }

        ngOnInit() {
                this.fetchData();

                this.newHolidayForm = this.formBuilder.group({
                        name: ['', [Validators.required]],
                        holidayDate: ['', [Validators.required]]
                });
        }

        ngAfterViewChecked() {
                if (!this.isDataTableInit && !this.loading) {
                        this.initDataTable();
                        this.isDataTableInit = true;
                }
        }

        ngOnDestroy() {
                if (this.dataTable) {
                        this.dataTable.destroy();
                }
        }

        fetchData(): void {
                this.holidayService.getHolidayList().subscribe({
                        next: (data: Holiday[]) => {
                                if (!data) {
                                        this.loading = false;
                                        this.holidayList = [];
                                        return;
                                }

                                this.holidayList = data;
                                this.loading = false;

                        },
                        error: (error: any) => {
                                console.error("Failed to fetch holiday list", error);
                                this.loading = false;
                        }
                });
        }

        initDataTable(): void {
                if (this.dataTable) {
                        this.dataTable.destroy();
                }

                this.dataTable = $('#holidayListTable').DataTable({
                        scrollY: "500px",
                        scrollX: true,
                        scrollCollapse: true,
                        paging: true,
                        lengthChange: false,
                        searching: false,
                        info: false,
                        language: {
                                "emptyTable": "There is no active holiday found",
                        }
                });
        }

        refreshData(): void {
                if (this.dataTable) {
                        this.dataTable.destroy();
                        this.isDataTableInit = false;
                }
                this.loading = true;
                this.fetchData();
        }

        setView(view: 'list' | 'calendar'): void {
                this.currentView = view;
        }

        openModal(content: any): void {
                this.modalService.open(content, {centered: true});
        }

        onSubmit() {}

}
