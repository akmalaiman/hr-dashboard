import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HolidayHomeComponent} from './holiday-home.component';

describe('CalendarComponent', () => {
        let component: HolidayHomeComponent;
        let fixture: ComponentFixture<HolidayHomeComponent>;

        beforeEach(async () => {
                await TestBed.configureTestingModule({
                        imports: [HolidayHomeComponent]
                })
                        .compileComponents();

                fixture = TestBed.createComponent(HolidayHomeComponent);
                component = fixture.componentInstance;
                fixture.detectChanges();
        });

        it('should create', () => {
                expect(component).toBeTruthy();
        });
});
