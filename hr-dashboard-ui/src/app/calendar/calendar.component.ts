import {Component, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {NgIf} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgbModal, NgbModalConfig} from '@ng-bootstrap/ng-bootstrap';
import {config} from "rxjs";

@Component({
        selector: 'app-calendar',
        standalone: true,
        imports: [
                RouterLink,
                NgIf,
                FormsModule,
                ReactiveFormsModule
        ],
        templateUrl: './calendar.component.html',
        styleUrl: './calendar.component.css'
})
export class CalendarComponent implements OnInit{

        pageName: string = 'Calendar';
        loading: boolean = true;
        newHolidayForm!: FormGroup;
        isHolidayExists: boolean = false;
        currentView: 'list' | 'calendar' = 'list';

        constructor(config: NgbModalConfig, private modalService: NgbModal, private formBuilder: FormBuilder) {
                config.backdrop = "static";
                config.keyboard = false;
        }

        ngOnInit() {
                this.newHolidayForm = this.formBuilder.group({
                        name: ['', [Validators.required]],
                        holidayDate: ['', [Validators.required]]
                });
        }

        setView(view: 'list' | 'calendar'): void {
                this.currentView = view;
        }

        openModal(content: any): void {
                this.modalService.open(content, {centered: true});
        }

        onSubmit() {}

}
