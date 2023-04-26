import { Client } from "./client.interfaces";

export interface Account{
    id         : number,
    number     : number,
    balance    : number,
    type       : string, 
    state      : boolean,
    clientName : string, 
    client?    : Client, 
    clientId   : number
}