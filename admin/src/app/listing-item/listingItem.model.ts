/* tslint:disable:whitespace ban-types */
export class ListingItem{

    title : String ;
    address: String;
    description : String;
    img : string ;
    constructor(title : String , address : String , description : String ,img : string)
    {
        this.img =img;
        this.title=title;
        this.address=address;
        this.description=description;

    }



}
