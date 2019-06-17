import { Phone } from './phone';

export class User {
    userId: string;
    username: string;
    firstName:string;
    lastName: string;

    //FIXME usually lists are just plural. so phones rather than phoneList
    phoneList: Phone[];
}
