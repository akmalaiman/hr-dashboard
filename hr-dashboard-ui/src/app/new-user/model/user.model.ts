import {JobPosition} from "./job-position.model";
import {Role} from "./role.model";

export interface User {
        id: number;
        firstName: string;
        lastName: string;
        username: string;
        email: string;
        password: string;
        address: string;
        city: string;
        state: string;
        postalCode: number;
        country: string;
        jobPositionId: JobPosition;
        roles: Role[];
        status: string;
        createdAt: Date;
        createdBy: number;
        updatedAt: Date;
        updatedBy: number;
}
