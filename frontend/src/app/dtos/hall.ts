import {Venue} from './venue';

export interface Hall {
    id?: number;
    name: string;
    width: number;
    height: number;
    venue: Venue;
}
