import _ from 'lodash';
export class AuthToken {
    public access_token: string;
    public refresh_token: string;
    public expires_in: number;
    public user_id: number;
    public picker_id: string;
    public org_code: string;
    public queue_id: string;
    public username: string;
}