import _ from 'lodash';
export class PickSlip {
    public pick_slip_number: number = 0;
    public customer_name: string;
    public tpl: string;
    public pallet_tare: number = 0;
    public container_type: string;
    public printer: string;

    constructor(ps, cn, tpl, pallet, container) {
        this.pick_slip_number = ps;
        this.customer_name = cn;
        this.tpl = tpl;
        this.container_type = container;
        if (!_.isEmpty(pallet)) this.pallet_tare = _.toNumber(pallet);
    }
}