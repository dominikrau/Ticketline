import {Component, Input, OnInit} from '@angular/core';
import {Event} from '../../dtos/event';

@Component({
    selector: 'app-home-event-card',
    templateUrl: './home-event-card.component.html',
    styleUrls: ['./home-event-card.component.css']
})
export class HomeEventCardComponent implements OnInit {

    @Input()
    event: Event;

    @Input()
    index: number;

    constructor() {
    }

    ngOnInit(): void {
    }

}
