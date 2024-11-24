import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UploadHomeComponent} from './upload-home.component';

describe('UploadComponent', () => {
        let component: UploadHomeComponent;
        let fixture: ComponentFixture<UploadHomeComponent>;

        beforeEach(async () => {
                await TestBed.configureTestingModule({
                        imports: [UploadHomeComponent]
                })
                        .compileComponents();

                fixture = TestBed.createComponent(UploadHomeComponent);
                component = fixture.componentInstance;
                fixture.detectChanges();
        });

        it('should create', () => {
                expect(component).toBeTruthy();
        });
});
