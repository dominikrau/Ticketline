export interface EventType {
    displayName: string;
    value: string;
}

export const EventTypes: EventType[] = [
    {displayName: 'Concerts', value: 'CONCERTS'},
    {displayName: 'Culture', value: 'CULTURE'},
    {displayName: 'Sports', value: 'SPORTS'},
    {displayName: 'Musical', value: 'MUSICAL'},
    {displayName: 'Show', value: 'SHOW'},
    {displayName: 'Cabaret', value: 'CABARET'},
    {displayName: 'Comedy', value: 'COMEDY'},
    {displayName: 'Other', value: 'OTHER'},
];
