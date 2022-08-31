import {Artist} from './artist';

export interface Event {

    id?: number;
    name: string;
    description: string;
    imageUrl: string;
    eventType: string;
    artists: Artist[];

}
