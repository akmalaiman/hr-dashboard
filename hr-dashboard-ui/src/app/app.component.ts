import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

@Component({
        selector: 'app-root',
        standalone: true,
        imports: [
                RouterOutlet,
                ReactiveFormsModule,
                FormsModule,
        ],
        templateUrl: './app.component.html',
        styleUrl: './app.component.css'
})
export class AppComponent {
        title = 'hr-dashboard-ui';
}
