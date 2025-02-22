import {Component, OnInit, signal, TemplateRef, ViewChild} from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CalendarOptions, EventApi, EventClickArg} from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import listPlugin from '@fullcalendar/list';
import interactionPlugin from '@fullcalendar/interaction';
import {NgbModal, NgbModalConfig} from '@ng-bootstrap/ng-bootstrap';
import {AuthService} from "../../auth/auth.service";
import {HolidayService} from "../service/holiday.service";
import {Holiday} from "../../common/model/holiday.model";
import {FullCalendarComponent, FullCalendarModule} from "@fullcalendar/angular";
import Swal from "sweetalert2";

@Component({
    selector: 'app-calendar',
    standalone: true,
    imports: [
        RouterLink,
        NgIf,
        FormsModule,
        ReactiveFormsModule,
        FullCalendarModule
    ],
    templateUrl: './holiday-home.component.html',
    styleUrl: './holiday-home.component.css'
})
export class HolidayHomeComponent implements OnInit {

    pageName: string = 'Holiday';
    loading: boolean = true;
    isHolidayExists: boolean = false;
    isAdmin: boolean = false;
    isManager: boolean = false;
    newHolidayForm!: FormGroup;
    editHolidayForm!: FormGroup;
    currentView: 'list' | 'calendar' = 'calendar';
    holidayDetails!: Holiday;
    holidayList: Holiday[] = [];
    calendarEvents: any[] = [];

    @ViewChild('editForm') editForm!: TemplateRef<any>;
    @ViewChild('calendar') calendarComponent!: FullCalendarComponent;

    constructor(
        config: NgbModalConfig,
        private modalService: NgbModal,
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private holidayService: HolidayService
    ) {
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

        this.editHolidayForm = this.formBuilder.group({
            name: ['', [Validators.required]],
            holidayDate: ['', Validators.required]
        });
    }

    fetchData(): void {
        this.holidayService.getHolidayList().subscribe({
            next: (data: Holiday[]) => {
                if (!data) {
                    this.loading = false;
                    this.holidayList = [];
                    this.calendarEvents = [];
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
                this.refreshCalendar();

            },
            error: (error: any) => {
                console.error("Failed to fetch holiday list", error);
                this.loading = false;
            }
        });
    }

    refreshCalendar(): void {
        const checkInterval = setInterval(() => {
            if (this.calendarComponent) {
                clearInterval(checkInterval);
                const calendarApi = this.calendarComponent.getApi();
                calendarApi.removeAllEvents();
                calendarApi.addEventSource(this.calendarEvents);
                calendarApi.render();
            }
        }, 100);
    }

    refreshData(): void {
        this.loading = true;
        this.holidayList = [];
        this.calendarEvents = [];
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
                    this.refreshData();
                    Swal.fire({
                        icon: "success",
                        text: "New Holiday Successfully Created!",
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        showConfirmButton: false,
                        timer: 2000,
                    });
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

    onEdit() {

        if (this.editHolidayForm?.valid) {
            const holidayData: Holiday = {
                ...this.editHolidayForm.value,
                id: this.holidayDetails.id,
                name: this.editHolidayForm.value.name,
                holidayDate: this.editHolidayForm.value.holidayDate,
                status: '',
                createdAt: new Date(),
                createdBy: 0,
                updatedAt: new Date(),
                updatedBy: 0
            };

            this.holidayService.updateHoliday(holidayData).subscribe({
                next: (data: Holiday) => {
                    this.modalService.dismissAll();
                    this.editHolidayForm.reset();
                    this.refreshData();
                    Swal.fire({
                        icon: "success",
                        text: "Holiday Successfully Updated!",
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        showConfirmButton: false,
                        timer: 2000,
                    });
                },
                error: (error: any) => {
                    Swal.fire({
                        title: 'Oops!',
                        text: 'An error occurred while updating the Holiday. Please try again.',
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        }

    }

    onDelete(holidayId: number) {
        this.holidayService.deleteHoliday(holidayId).subscribe({
            next: (data: boolean) => {
                this.modalService.dismissAll();
                this.editHolidayForm.reset();
                this.refreshData();
                Swal.fire({
                    icon: "success",
                    text: "Holiday Successfully Deleted!",
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    showConfirmButton: false,
                    timer: 2000,
                });
            },
            error: (error: any) => {
                Swal.fire({
                    title: 'Oops!',
                    text: 'An error occurred while deleting the Holiday. Please try again.',
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        });
    }

    handleEventClick(eventInfo: EventClickArg): void {

        if (this.isAdmin) {
            const holidayId = Number(eventInfo.event.id);

            this.holidayService.getHolidayById(holidayId).subscribe({
                next: (data: Holiday) => {
                    this.holidayDetails = data;
                    this.editHolidayForm.patchValue({
                        name: this.holidayDetails.name,
                        holidayDate: this.holidayDetails.holidayDate
                    });

                    this.modalService.open(this.editForm, {centered: true});
                },
                error: (error: any) => {
                    console.error("Failed to fetch holiday details: ", error);
                }
            });
        }

    }

    calendarOptions = signal<CalendarOptions>({
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
        eventClick:this.handleEventClick.bind(this),
        aspectRatio: 2,
        selectable: true,
    });

    calendarListOptions = signal<CalendarOptions>({
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
