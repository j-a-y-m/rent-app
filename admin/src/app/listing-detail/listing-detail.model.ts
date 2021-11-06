/* tslint:disable:no-trailing-whitespace */
export class ListingDetail {

  title : string ;
  address: string;
  description : string;
  img : string[] ;
  price: string;
  city: string;
  area: string;
  occupants: string;
  facilities: string[];
  type: string;
  bhk: string;
  constructor(title : string , address : string , price  : string , description : string ,
              city : string , area : string , occupants : string , facilities : string[] ,
              type : string , bhk : string , img : string[])
  {
    this.price = price;
    this.city = city;
    this.area = area;
    this.occupants = occupants;
    this.facilities = facilities;
    this.type = type;
    this.bhk = bhk;
    this.img = img;
    this.title = title;
    this.address = address;
    this.description = description;

  }


}
