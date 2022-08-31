import {Injectable} from '@angular/core';
import {Show} from '../dtos/show';
import {Hall} from '../dtos/hall';

@Injectable({
    providedIn: 'root'
})
export class ShowStateService {

    private show: Show;
    private eventId: number;
    private hall: Hall;
    private returnUrl: string;

    constructor() {
    }

    setEventId(id: number) {
        this.eventId = id;
    }

    getCurrentShow() {
        if (this.show === undefined) {
            const showState = JSON.parse(localStorage.getItem('showState'));
            if (!showState || showState.show === undefined || showState.eventId !== this.eventId) {
                this.show = this.buildEmptyShow();
            } else {
                this.show = showState.show;
                /*if (!this.show.seatingChart?.id) {
                    delete this.show.seatingChart;
                }*/
            }
        }

        return this.show;
    }

    getEventId() {
        return this.eventId;
    }

    setHall(hall: Hall) {
        this.hall = hall;
    }

    getHall() {
        return this.hall;
    }

    updateShow(show: Show) {
        Object.assign(this.show, show);
        localStorage.setItem('showState', JSON.stringify({eventId: this.eventId, show: this.show}));
    }

    getReturnUrl() {
        return this.returnUrl;
    }

    setReturnUrl(url: string) {
        this.returnUrl = url;
    }

    clear() {
        this.show = this.buildEmptyShow();
        this.eventId = undefined;
        this.hall = undefined;
        localStorage.removeItem('showState');
    }

    buildEmptyShow(): Show {
        return {
            endTime: '',
            event: undefined,
            startTime: '',
            venue: undefined,
            seatingChart: undefined,
            pricings: []
        };
    }
}
