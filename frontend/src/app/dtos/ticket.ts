import {UserProfile} from './user-profile';
import {Show} from './show';
import {SeatingChartSeat, SeatingChartSector} from './seating-chart';
import {Status} from 'tslint/lib/runner';
import {Order} from './order';

export interface Ticket {

        ticketId: number;
        createdAt: string;
        user: UserProfile;
        show: Show;
        sector: SeatingChartSector;
        seat?: SeatingChartSeat;
        price: number;
        status: Status;
        reservationNumber: string;
        ticketOrder: Order;
}
