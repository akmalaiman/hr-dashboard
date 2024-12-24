import {AfterViewChecked, Component, OnDestroy, OnInit, signal} from '@angular/core';
import {RouterLink} from "@angular/router";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import listPlugin from '@fullcalendar/list';
import interactionPlugin from '@fullcalendar/interaction';
import {NgbModal, NgbModalConfig} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from "../../auth/auth.service";
import {HolidayService} from "../service/holiday.service";
import {Holiday} from "../../common/model/holiday.model";
import {FullCalendarModule} from "@fullcalendar/angular";
import Swal from "sweetalert2";

@Component({
        selector: 'app-calendar',
        standalone: true,
        imports: [
                RouterLink,
                NgIf,
                FormsModule,
                ReactiveFormsModule,
                DatePipe,
                NgForOf,
                FullCalendarModule
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
        currentView: 'list' | 'calendar' = 'calendar';
        holidayList: Holiday[] = [];
        calendarEvents: any[] = [];

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
                                this.holidayList.forEach((holiday: Holiday) => {
                                        this.calendarEvents.push({
                                                id: holiday.id,
                                                title: holiday.name,
                                                start: holiday.holidayDate,
                                                end: holiday.holidayDate
                                        });
                                });
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

        onSubmit() {

                if (this.newHolidayForm?.valid) {
                        const holidayData: Holiday = {
                                ...this.newHolidayForm.value,
                                id: 0,
                                name: this.newHolidayForm.value.name,
                                holidayDate: this.newHolidayForm.value.holidayDate,
                                status: '',
                                createdAt: new Date(),
                                createdBy: 0,
                                updatedAt: new Date(),
                                updatedBy: 0
                        };

                        this.holidayService.createHoliday(holidayData).subscribe({
                                next: (data: Holiday) => {
                                        this.modalService.dismissAll();
                                        this.newHolidayForm.reset();
                                        Swal.fire({
                                                icon: "success",
                                                text: "New Holiday Successfully Created!",
                                                allowOutsideClick: false,
                                                allowEscapeKey: false,
                                                showConfirmButton: false,
                                                timer: 2000,
                                        });
                                        this.refreshData();
                                },
                                error: (error: any) => {
                                        Swal.fire({
                                                title: 'Oops!',
                                                text: 'An error occurred while creating the new Holiday. Please try again.',
                                                icon: 'error',
                                                confirmButtonText: 'OK'
                                        });
                                }
                        });
                }

        }

        calendarOptions = signal<CalendarOptions> ({
                initialView: 'dayGridMonth',
                plugins: [dayGridPlugin, interactionPlugin],
                headerToolbar: {
                        left: 'prev,today,next',
                        center: 'title',
                        right: 'prevYear,dayGridMonth,dayGridWeek,dayGridDay,nextYear'
                },
                buttonText: {
                        today: 'Today',
                        month: 'Month',
                        week: 'Week',
                        day: 'Day'
                },
                firstDay: 1,
                events: this.calendarEvents,
                aspectRatio: 2,
                selectable: true,
        });

        calendarListOptions = signal<CalendarOptions> ({
                initialView: 'listYear',
                plugins: [listPlugin],
                headerToolbar: {
                        left: 'prev,today,next',
                        center: 'title',
                        right: 'prevYear,listYear,listMonth,listWeek,listDay,nextYear'
                },
                buttonText: {
                        today: 'Today',
                        listYear: 'Year',
                        listMonth: 'Month',
                        listWeek: 'Week',
                        listDay: 'Day'
                },
                firstDay: 1,
                events: this.calendarEvents,
                aspectRatio: 2
        });

}
