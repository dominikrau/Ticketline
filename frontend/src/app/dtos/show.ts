import {Venue} from './venue';
import {Event} from './event';
import {SeatingChart} from './seating-chart';
import {Pricing} from './pricing';
import {Hall} from './hall';

export interface Show {
    showId?: number;
    createdAt?: string;
    startTime: string;
    endTime: string;
    event: Event;
    venue: Venue;
    seatingChart: SeatingChart;
    pricings: Pricing[];
    hall?: Hall;
}
