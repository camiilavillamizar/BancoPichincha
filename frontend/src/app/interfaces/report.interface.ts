
export interface Report{
    id                 : number,
    date               : string, 
    clientName         : string, 
    accountNumber      : string,
    accountType        : string, 
    transactionType    : string, 
    initialBalance     : number, 
    accountState       :boolean,
    transactionAmount  : number, 
    transactionBalance : number
}