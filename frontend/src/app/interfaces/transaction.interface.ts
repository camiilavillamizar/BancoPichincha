import { Account } from "./account.interfaces";

export interface Transaction{
    id        : number, 
    date      : Date, 
    type      : string, 
    amount    : number, 
    balance   : number,
    accountId : number, 
    account   : Account
}