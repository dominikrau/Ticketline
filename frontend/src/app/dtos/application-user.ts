import {Address} from './address';

export interface ApplicationUser {
    userId: number;
    firstName: string;
    lastName: string;
    emailAddress: string;
    password: string;
    dateOfBirth: string;
    gender: string;
    address: Address;
    roles: string[];
}
